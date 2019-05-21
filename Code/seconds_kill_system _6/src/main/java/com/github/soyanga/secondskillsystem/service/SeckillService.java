package com.github.soyanga.secondskillsystem.service;

import com.github.soyanga.secondskillsystem.dao.GoodsDao;
import com.github.soyanga.secondskillsystem.domain.Goods;
import com.github.soyanga.secondskillsystem.domain.SeckillOrder;
import com.github.soyanga.secondskillsystem.domain.SeckillUser;
import com.github.soyanga.secondskillsystem.domain.OrderInfo;
import com.github.soyanga.secondskillsystem.redis.RedisService;
import com.github.soyanga.secondskillsystem.redis.SeckillKey;
import com.github.soyanga.secondskillsystem.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        System.out.println(order);
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

    public void reset(List<GoodsVo> goodsList) {
        goodsService.resetStock(goodsList);
        orderService.deleteOrders();
    }


}
