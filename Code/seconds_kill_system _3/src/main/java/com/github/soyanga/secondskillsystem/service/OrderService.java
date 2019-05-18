package com.github.soyanga.secondskillsystem.service;

import com.github.soyanga.secondskillsystem.dao.OrderDao;
import com.github.soyanga.secondskillsystem.domain.MiaoShaUser;
import com.github.soyanga.secondskillsystem.domain.MiaoshaOrder;
import com.github.soyanga.secondskillsystem.domain.OrderInfo;
import com.github.soyanga.secondskillsystem.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @program: seconds_kill_system _3
 * @Description: 订单服务器
 * @Author: SOYANGA
 * @Create: 2019-05-18 17:47
 * @Version 1.0
 */
@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;

    /**
     * 通过用户id 商品id 查看秒杀订单表中是否已经有秒杀，判断用户是否已将秒杀成功过
     * @param id
     * @param goodsId
     * @return
     */
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long id, long goodsId) {
        return orderDao.getMiaoshaOrderByUserIdGoodsId(id, goodsId);
    }

    /**
     * 创建 订单表并返回订单ID 创建秒杀订单表（使用返回的订单ID）
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    public OrderInfo createOrder(MiaoShaUser user, GoodsVo goods) {
        //写OderInfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0); //支付表示未支付（新建订单）  用枚举表示一下
        orderInfo.setUserId(user.getId());
        long orderId = orderDao.insert(orderInfo);
        System.out.println(orderId);
        //写MiaoshaOrder
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderId);
        miaoshaOrder.setUserId(user.getId());
        System.out.println(miaoshaOrder);
        orderDao.insertMiaoshaOrder(miaoshaOrder);
        return orderInfo;

    }
}
