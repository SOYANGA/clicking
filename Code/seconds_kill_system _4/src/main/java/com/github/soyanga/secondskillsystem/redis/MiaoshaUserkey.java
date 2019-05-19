package com.github.soyanga.secondskillsystem.redis;

/**
 * @program: seconds_kill_system
 * @Description: miaoshaUser 添加前缀的实现类 User缓存Key封装
 * @Author: SOYANGA
 * @Create: 2019-05-15 13:12
 * @Version 1.0
 */
public class MiaoshaUserkey extends BasePrefix {
    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;//2天

    public MiaoshaUserkey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    /**
     * 通过token(SessionId) 缓存key封装
     */
    public static MiaoshaUserkey token = new MiaoshaUserkey(TOKEN_EXPIRE, "tk");

}
