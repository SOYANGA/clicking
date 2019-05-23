package com.github.soyanga.secondskillsystem.redis;

/**
 * @program: seconds_kill_system
 * @Description: 添加前缀的顺序实现
 * @Author: SOYANGA
 * @Create: 2019-05-15 13:12
 * @Version 1.0
 */
public class OrderKey extends BasePrefix {

    public OrderKey(int expreSeconds, String prefix) {
        super(expreSeconds, prefix);
    }
}
