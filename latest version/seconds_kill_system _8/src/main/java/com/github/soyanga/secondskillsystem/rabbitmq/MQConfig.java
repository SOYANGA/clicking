package com.github.soyanga.secondskillsystem.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: seconds_kill_system_6
 * @Description: RabbitMq配置
 * @Author: SOYANGA
 * @Create: 2019-05-21 21:52
 * @Version 1.0
 */
@Configuration
public class MQConfig {


    /**
     * 队列名称
     */
    public static final String SECKILL_QUEUE = "seckill.queue";
    public static final String QUEUE = "queue";
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String TOPIC_EXCHANGE = "topicExchange";
    public static final String FANOUT_EXCHANGE = "fanoutExchange";
    public static final String HEADERS_EXCHANGE = "headersExchage";
    public static final String HEADER_QUEUE = "header.queue";


    /**
     * 秒杀队列
     * @return
     */
    @Bean
    public Queue seckillQueue() {
        return new Queue(SECKILL_QUEUE, true);
    }


    /**
     * 核心队列
     * Direct模式 直接指定队列名 交换机Exchage
     *
     * @return
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }

    /**
     * Topic模式 交换机Exchage 模糊匹配
     */
    @Bean
    public Queue topicQueue1() {
        return new Queue(TOPIC_QUEUE1, true);
    }

    /**
     * Topic模式 交换机Exchage 模糊匹配
     */
    @Bean
    public Queue topicQueue2() {
        return new Queue(TOPIC_QUEUE2, true);
    }

    /**
     * 创建交换机
     *
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    /**
     * 将其进行绑定routingkey -> exchange ->queue->consumer
     *
     * @return
     */
    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
    }

    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
    }


    /**
     * Fanout 模式 交换机Exchange 广播模式
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding fanoutBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }


    /**
     * Header模式 交换机Exchange
     */
    @Bean
    public HeadersExchange headersExchage() {
        return new HeadersExchange(HEADERS_EXCHANGE);
    }

    @Bean
    public Queue headerQueue1() {
        return new Queue(HEADER_QUEUE, true);
    }

    @Bean
    public Binding headerBinding() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("header1", "value1");
        map.put("header2", "value2");
        return BindingBuilder.bind(headerQueue1()).to(headersExchage()).whereAll(map).match();
    }


}
