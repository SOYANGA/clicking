package com.github.soyanga.secondskillsystem.service;

import com.github.soyanga.secondskillsystem.dao.GoodsDao;
import com.github.soyanga.secondskillsystem.domain.Goods;
import com.github.soyanga.secondskillsystem.domain.MiaoShaUser;
import com.github.soyanga.secondskillsystem.domain.OrderInfo;
import com.github.soyanga.secondskillsystem.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: seconds_kill_system _3
 * @Description: 秒杀服务
 * @Author: SOYANGA
 * @Create: 2019-05-18 17:53
 * @Version 1.0
 */
@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    /**
     * 执行秒杀 //1.减库存 2.下订单 3.写入秒杀（生成order_Info  miaosha_order）（事务中的做的三步）
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    public OrderInfo miaosha(MiaoShaUser user, GoodsVo goods) {
        //1.减库存 2.下订单 3.写入秒杀（事务中的做的三步）
        goodsService.reduceStock(goods);

        //生成order_Info  miaosha_order
        return orderService.createOrder(user, goods);
    }
}
