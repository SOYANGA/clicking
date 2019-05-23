package com.github.soyanga.secondskillsystem.rabbitmq;

import com.github.soyanga.secondskillsystem.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: seconds_kill_system_6
 * @Description: 发送者容器（生产者）
 * @Author: SOYANGA
 * @Create: 2019-05-21 21:50
 * @Version 1.0
 */
@Service
public class MQSender {

//    public static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    /**
     * 注入模板
     */
    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendSeckillMessage(SeckillMessage message) {
        String msg = RedisService.beanToString(message);
//        logger.info("send message" + msg);
        amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE, msg);
    }


//    public void send(Object message) {
//        String msg = RedisService.beanToString(Object.class);
//        logger.info("send message" + msg);
//        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
//    }
//
//
//    public void sendTopic(Object message) {
//        String topidMsg = RedisService.beanToString(message);
//        logger.info("send topic message{}", topidMsg);
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", topidMsg + "1");
//        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", topidMsg + "2");
//    }
//
//    public void sendFanout(Object message) {
//        String fanoutMsg = RedisService.beanToString(message);
//        logger.info("send fanout message{}", fanoutMsg);
//        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", fanoutMsg);
//    }
//
//    public void sendHeader(Object message) {
//        String msg = RedisService.beanToString(message);
//        logger.info("send fanout message:" + msg);
//        MessageProperties properties = new MessageProperties();
//        properties.setHeader("header1", "value1");
//        properties.setHeader("header2", "value2");
//        Message obj = new Message(msg.getBytes(), properties);
//        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);
//    }
}
