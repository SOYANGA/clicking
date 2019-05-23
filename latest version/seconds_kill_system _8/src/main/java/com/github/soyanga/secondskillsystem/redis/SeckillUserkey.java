package com.github.soyanga.secondskillsystem.redis;

/**
 * @program: seconds_kill_system
 * @Description: seckillUser 添加前缀的实现类 User缓存Key封装
 * @Author: SOYANGA
 * @Create: 2019-05-15 13:12
 * @Version 1.0
 */
public class SeckillUserkey extends BasePrefix {
    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;//2天

    public SeckillUserkey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    /**
     * 通过token(SessionId) 缓存key封装
     */
    public static SeckillUserkey token = new SeckillUserkey(TOKEN_EXPIRE, "tk");
    //用户对象缓存，永久有效
    public static SeckillUserkey getById = new SeckillUserkey(0, "id");

}
