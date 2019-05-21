package com.github.soyanga.secondskillsystem.service;

import com.github.soyanga.secondskillsystem.dao.OrderDao;
import com.github.soyanga.secondskillsystem.domain.SeckillUser;
import com.github.soyanga.secondskillsystem.domain.SeckillOrder;
import com.github.soyanga.secondskillsystem.domain.OrderInfo;
import com.github.soyanga.secondskillsystem.redis.OrderKey;
import com.github.soyanga.secondskillsystem.redis.RedisService;
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

    @Autowired
    RedisService redisService;

    /**
     * 根据订单号获得订单信息
     *
     * @param orderId
     * @return
     */
    public OrderInfo getOrderById(long orderId) {
        return orderDao.getOrderById(orderId);
    }

    /**
     * 通过用户id 商品id 查看秒杀订单表中是否已经有秒杀，判断用户是否已将秒杀成功过
     * 优化通过缓存进行查找
     *
     * @param userid
     * @param goodsId
     * @return
     */
    public SeckillOrder getSeckillOrderByUserIdGoodsId(long userid, long goodsId) {
        //return orderDao.getSeckillOrderByUserIdGoodsId(id, goodsId);
        return redisService.get(OrderKey.getSeckillOrderByUidGid, "" + userid + "_" + goodsId, SeckillOrder.class);
    }

    /**
     * 创建 订单表并返回订单ID 创建秒杀订单表（使用返回的订单ID）
     * <p>
     * 解决超买问题
     * 数据库天然的对数据的写进行加锁 配个 库存数目使用即可
     * 防止一个用户一次下请求两次同时减库存
     * 使用数据库唯一索引 订单表和秒杀订单表中有一个唯一主键相连
     *
     * @param user
     * @param goods
     * @return
     */
    @Transactional
    public OrderInfo createOrder(SeckillUser user, GoodsVo goods) {
        //写OderInfo
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSeckillPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0); //支付表示未支付（新建订单）  用枚举表示一下
        orderInfo.setUserId(user.getId());
        orderDao.insert(orderInfo);
        //写SeckillOrder
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderInfo.getId());
        seckillOrder.setUserId(user.getId());
        orderDao.insertSeckillOrder(seckillOrder);
        //生成订单完成后，要将订单信息写进缓存中
        redisService.set(OrderKey.getSeckillOrderByUidGid, "" + user.getId() + "_" + goods.getId(), seckillOrder);
        return orderInfo;

    }

    public void deleteOrders() {
        orderDao.deleteOrders();
        orderDao.deleteSeckillOrders();
    }
}
