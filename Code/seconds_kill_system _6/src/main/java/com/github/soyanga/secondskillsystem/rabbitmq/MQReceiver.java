package com.github.soyanga.secondskillsystem.rabbitmq;

import com.github.soyanga.secondskillsystem.domain.OrderInfo;
import com.github.soyanga.secondskillsystem.domain.SeckillOrder;
import com.github.soyanga.secondskillsystem.domain.SeckillUser;
import com.github.soyanga.secondskillsystem.redis.RedisService;
import com.github.soyanga.secondskillsystem.result.CodeMsg;
import com.github.soyanga.secondskillsystem.result.Result;
import com.github.soyanga.secondskillsystem.service.GoodsService;
import com.github.soyanga.secondskillsystem.service.OrderService;
import com.github.soyanga.secondskillsystem.service.SeckillService;
import com.github.soyanga.secondskillsystem.service.SeckillUserService;
import com.github.soyanga.secondskillsystem.vo.GoodsVo;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

/**
 * @program: seconds_kill_system_6
 * @Description: 接收者容器（消费者）
 * @Author: SOYANGA
 * @Create: 2019-05-21 21:51
 * @Version 1.0
 */
@Service
public class MQReceiver {


//    public static Logger logger = LoggerFactory.getLogger(MQReceiver.class);


    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    SeckillService seckillService;

    @Autowired
    RedisService redisService;

    /**
     * Direct模式 直接指定队列名 交换机Exchage 秒杀的消费者（接收者）
     *
     * @param message
     */
    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receive(String message) {
//        logger.info("receive message:{}", message);
        SeckillMessage seckillMessage = RedisService.stringToBean(message, SeckillMessage.class);
        SeckillUser user = seckillMessage.getUser();
        long goodsId = seckillMessage.getGoodsId();

        //判断商品有无库存 准确数字是从mysql数库中取得的
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        //已经没有库存了
        if (stock <= 0) {
            return;
        }

        //再次 判断是否已经秒杀到了
        SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(), goods.getId());
        if (order != null) {
            return;
        }

        //此时用户已经成功秒杀接下来进行一些列事务操作  1.减库存 2.下订单 3.写入秒杀（事务中的做的三步）
        //返回订单页
        seckillService.seckill(user, goods);
    }

//    /**
//     * Direct模式 直接指定队列名 交换机Exchage
//     *
//     * @param message
//     */
//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void receive(String message) {
//        logger.info("receive message:{}", message);
//    }
//
//    /**
//     * Topic模式
//     *
//     * @param topicMessage
//     */
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
//    public void receiveTopic1(String topicMessage) {
//        logger.info("receive topic queue1 message:{}", topicMessage);
//    }
//
//    /**
//     * Topic模式
//     *
//     * @param topicMessage
//     */
//    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
//    public void receiveTopic2(String topicMessage) {
//        logger.info("receive topic queue2 message:{}", topicMessage);
//    }
//
//    /**
//     * Headers 模式 Map(key,value)匹配模式
//     *
//     * @param message
//     */
//    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
//    public void receiveHeaderQueue(byte[] message) {
//        logger.info("receive header queue message:{}", new String(message));
//    }

}
