package com.github.soyanga.secondskillsystem.controller;

import com.github.soyanga.secondskillsystem.access.AccessLimit;
import com.github.soyanga.secondskillsystem.domain.SeckillUser;
import com.github.soyanga.secondskillsystem.domain.SeckillOrder;
import com.github.soyanga.secondskillsystem.domain.OrderInfo;
import com.github.soyanga.secondskillsystem.rabbitmq.MQSender;
import com.github.soyanga.secondskillsystem.rabbitmq.SeckillMessage;
import com.github.soyanga.secondskillsystem.redis.*;
import com.github.soyanga.secondskillsystem.result.CodeMsg;
import com.github.soyanga.secondskillsystem.result.Result;
import com.github.soyanga.secondskillsystem.service.GoodsService;
import com.github.soyanga.secondskillsystem.service.SeckillService;
import com.github.soyanga.secondskillsystem.service.OrderService;
import com.github.soyanga.secondskillsystem.util.MD5Util;
import com.github.soyanga.secondskillsystem.util.UUIDUtil;
import com.github.soyanga.secondskillsystem.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: seconds_kill_system _3
 * @Description: 秒杀业务处理
 * @Author: SOYANGA
 * @Create: 2019-05-18 17:37
 * @Version 1.0
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender sender;

    /**
     * 判断redis库存的一个map标志 表示商品秒杀是否需要结束
     */
    private Map<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();


    /**
     * 容器启动时发现实现InitializingBean的类，则会调用此类中的afterPropertiesSet方法
     * <p>
     * 系统初始化的时候将商品数量加载到Redis中
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }
        for (GoodsVo goodsVo : goodsList) {
            redisService.set(GoodsKey.getSeckillGoodsStock, "" + goodsVo.getId(), goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(), false);
        }
    }


    //qps: 547.0   2500*5     error:0.00%
    //qps: 456.9   5000*10    error:0.21%

    /**
     * 执行秒杀
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/{path}/do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doSeckill(Model model, SeckillUser user,
                                     @RequestParam("goodsId") long goodsId,
                                     @PathVariable("path") String path) {
        //判断用户登陆与否
        if (user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        //验证path
        boolean check = seckillService.checkPath(user, goodsId, path);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        //1.系统初始化的时候将商品数量加载到缓存中去，并且带有一个商品是否秒杀完毕的标志位 （内存标记减少redis访问）
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //2.收到请求后首先减Redis中的库存 预减库存
        long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId);
        if (stock == 0) {
            //当前秒杀商品库存已经没有了
            localOverMap.put(goodsId, true);
        }
        if (stock < 0) {
            //当库存不够时将是否秒杀完毕标志修改
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        //3.有库存的化，判断是否已经秒杀过来了，防止重复缓存
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }
        //4.请求入队，立即返回排队中
        SeckillMessage message = new SeckillMessage();
        message.setUser(user);
        message.setGoodsId(goodsId);
        sender.sendSeckillMessage(message);
        //返回0代表就是排队中
        return Result.success(0);
        //5.在RabbitMq队列的recvice后执行生成订单，减库存



        /*
        //判断商品有无库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        //已经没有库存了
        if (stock <= 0) {
            return Result.error(CodeMsg.SECKILL_OVER);
        }

        //判断是否已经秒杀到了
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goods.getId());
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }

        //此时用户已经成功秒杀接下来进行一些列事务操作  1.减库存 2.下订单 3.写入秒杀（事务中的做的三步）
        //返回订单页
        OrderInfo orderInfo = seckillService.seckill(user, goods);
        return Result.success(orderInfo);
        */
    }

    /**
     * 客户端秒杀后 服务端将返回前端的结果进行封装返回给前端
     * <p>
     * 客户端成功秒杀后进行轮询 判断当前秒杀的状态
     * orderId：成功
     * -1：秒杀失败 库存不足
     * 0： 排队中
     *
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(Model model, SeckillUser user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        long result = seckillService.getSeckillResult(user.getId(), goodsId);
        return Result.success(result);
    }


    /**
     * 辅助功能对数据库进行进行还原 未测试提供便利 将所有数据回归原样
     *
     * @param model
     * @return
     */

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        for (GoodsVo goods : goodsList) {
            goods.setStockCount(10);
            redisService.set(GoodsKey.getSeckillGoodsStock, "" + goods.getId(), 10);
            localOverMap.put(goods.getId(), false);
        }
        redisService.delete(OrderKey.getSeckillOrderByUidGid);
        redisService.delete(SeckillKey.isGoodsOver);
        seckillService.reset(goodsList);
        return Result.success(true);
    }


    //qps: 547.0   2500*5     error:0.00%
    //qps: 456.9   5000*10    error:0.21%
    //qps: 1200.3  5000*10    error:0.07%

    /**
     * 先需要验证码校验成功才能 获取秒杀地址
     *
     * @param user
     * @param goodsId
     * @return
     */
    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getSeckillPath(HttpServletRequest request, SeckillUser user,
                                         @RequestParam("goodsId") long goodsId,
                                         @RequestParam(value = "verifyCode") int verifyCode) {
        //判断用户登陆与否
        if (user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }

        //此段注释代码逻辑-->转变为注解形式（AccessInterecptor拦截器中实现）
//        //防刷限流 查询访问次数 每5秒中只能访问5次
//        String uri = request.getRequestURI();//访问路径
//        String key = uri + "_" + user.getId();
//        Integer count = redisService.get(AccessKey.accessKey, "" + key, Integer.class);
//        if (count == null) {
//            redisService.set(AccessKey.accessKey, "" + key, 1);
//        } else if (count < 5) {
//            redisService.incr(AccessKey.accessKey, key);
//        } else {
//            return Result.error(CodeMsg.ACCESS_LIMIT_REACHED);
//        }

        //校验验证码
        boolean check = seckillService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check) {
            return Result.error(CodeMsg.VERIFYCODE_ERROE);
        }
        //获取路径
        String path = seckillService.createSeckillPath(user, goodsId);
        if (path == null) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        return Result.success(path);
    }


    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getSeckillVerifyCode(HttpServletResponse response, SeckillUser user, @RequestParam("goodsId") long goodsId) {
        //判断用户登陆与否
        if (user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
        BufferedImage image = seckillService.createVerifyCode(user, goodsId);
        try {
            //返回验证码
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SECKILL_FAIL);
        }
    }


}
