package com.github.soyanga.secondskillsystem.controller;

import com.github.soyanga.secondskillsystem.domain.SeckillUser;
import com.github.soyanga.secondskillsystem.domain.SeckillOrder;
import com.github.soyanga.secondskillsystem.domain.OrderInfo;
import com.github.soyanga.secondskillsystem.result.CodeMsg;
import com.github.soyanga.secondskillsystem.result.Result;
import com.github.soyanga.secondskillsystem.service.GoodsService;
import com.github.soyanga.secondskillsystem.service.SeckillService;
import com.github.soyanga.secondskillsystem.service.OrderService;
import com.github.soyanga.secondskillsystem.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: seconds_kill_system _3
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-05-18 17:37
 * @Version 1.0
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;


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
    @RequestMapping(value = "/do_seckill", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> doSeckill(Model model, SeckillUser user, @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", user);

        if (user == null) {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
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
    }

}
