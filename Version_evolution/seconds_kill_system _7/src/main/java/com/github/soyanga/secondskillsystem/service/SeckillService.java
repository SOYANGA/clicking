package com.github.soyanga.secondskillsystem.service;

import com.github.soyanga.secondskillsystem.domain.SeckillOrder;
import com.github.soyanga.secondskillsystem.domain.SeckillUser;
import com.github.soyanga.secondskillsystem.domain.OrderInfo;
import com.github.soyanga.secondskillsystem.redis.RedisService;
import com.github.soyanga.secondskillsystem.redis.SeckillKey;
import com.github.soyanga.secondskillsystem.util.MD5Util;
import com.github.soyanga.secondskillsystem.util.UUIDUtil;
import com.github.soyanga.secondskillsystem.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

/**
 * @program: seconds_kill_system _3
 * @Description: 秒杀服务
 * @Author: SOYANGA
 * @Create: 2019-05-18 17:53
 * @Version 1.0
 */
@Service
public class SeckillService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    /**
     * 执行秒杀 //1.减库存 2.下订单 3.写入秒杀（生成order_Info  seckill_order）（事务中的做的三步）
     *
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    public OrderInfo seckill(SeckillUser user, GoodsVo goods) {
        //1.减库存 2.下订单 3.写入秒杀（事务中的做的三步）数据库唯一索引防止用户重复下单（一个用户同时发出两个请求）
        //SQL语句中再添加一个判断库存的条件防止超卖
        // 且减库存失败回滚则返回false 就会去生成订单了 反之则生成订单秒杀成功
        boolean isSuccess = goodsService.reduceStock(goods);
        if (isSuccess) {
            //生成order_Info  seckill_order
            return orderService.createOrder(user, goods);
        } else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    /**
     * 设置一个标志位标识商品是否秒杀结束 表示一个当前状态 0秒杀正在进行中前端轮询（会再次来调用此方法） -1秒杀结束 id秒杀成功
     *
     * @param goodsId
     */
    private void setGoodsOver(Long goodsId) {
        redisService.set(SeckillKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exit(SeckillKey.isGoodsOver, "" + goodsId);
    }


    public long getSeckillResult(Long userId, long goodsId) {
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(userId, goodsId);
        if (order != null) {//秒杀成功
            return order.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId); //判断商品是否买完
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
     * 清除数据库的数据 以便于测试
     *
     * @param goodsList
     */
    public void reset(List<GoodsVo> goodsList) {
        goodsService.resetStock(goodsList);
        orderService.deleteOrders();
    }


    /**
     * 检查 秒杀路径
     *
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    public boolean checkPath(SeckillUser user, long goodsId, String path) {
        if (user == null || path == null) {
            return false;
        }
        String pathOld = redisService.get(SeckillKey.getSeckillPath, "" + user.getId() + "_" + goodsId, String.class);
        return path.equals(pathOld);
    }

    /**
     * 生成 秒杀路径
     *
     * @param user
     * @param goodsId
     * @return
     */
    public String createSeckillPath(SeckillUser user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(SeckillKey.getSeckillPath, "" + user.getId() + "_" + goodsId, str);
        return str;
    }


    /**
     * 后台 生成验证码
     *
     * @param user
     * @param goodsId
     * @return
     */
    public BufferedImage createVerifyCode(SeckillUser user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(SeckillKey.getSeckillVerifyCode, user.getId() + "," + goodsId, rnd);
        //输出图片
        return image;
    }

    /**
     * 操作数组
     */
    private static char[] ops = new char[]{'+', '-', '*'};

    /**
     * 根据随机数 生成验证码的字符串
     * 只做加 减 乘 + - *
     *
     * @param rdm
     * @return
     */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = "" + num1 + op1 + num2 + op2 + num3;
        return exp;
    }


    /**
     * 检验验证码的值
     *
     * @param user
     * @param goodsId
     * @param verifyCode
     * @return
     */
    public boolean checkVerifyCode(SeckillUser user, long goodsId, int verifyCode) {
        if (user == null || goodsId <= 0) {
            return false;
        }
        Integer codeOld = redisService.get(SeckillKey.getSeckillVerifyCode, user.getId() + "," + goodsId, Integer.class);
        if (codeOld == null || codeOld - verifyCode != 0) {
            return false;
        }
        //验证成功后从redis中删除
        redisService.delete(SeckillKey.getSeckillVerifyCode, user.getId() + "," + goodsId);
        return true;
    }

    /**
     * 计算验证码的值
     *
     * @param exp
     * @return
     */
    private static int calc(String exp) {
        try {
            //表达式引擎
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(exp);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
//    public static void main(String[] args) {
//        System.out.println(calc("1+3-8"));
//    }
}
