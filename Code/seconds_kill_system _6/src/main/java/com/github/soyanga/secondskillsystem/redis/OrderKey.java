package com.github.soyanga.secondskillsystem.redis;

/**
 * @program: seconds_kill_system
 * @Description: 添加前缀的订单实现
 * @Author: SOYANGA
 * @Create: 2019-05-15 13:12
 * @Version 1.0
 */
public class OrderKey extends BasePrefix {

    public OrderKey(String prefix) {
        super(prefix);
    }

    public static OrderKey getSeckillOrderByUidGid = new OrderKey("Soug");
}
