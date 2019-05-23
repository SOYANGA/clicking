package com.github.soyanga.secondskillsystem.redis;

/**
 * @program: seconds_kill_system
 * @Description: key值前添加前缀 顶层接口 通用缓存Key封装
 * @Author: SOYANGA
 * @Create: 2019-05-15 13:02
 * @Version 1.0
 */
public interface KeyPrefix {
    /**
     * 过期时间
     * @return
     */
    public int expireSeconds();

    /**
     * 前缀
     * @return
     */
    public String getPrefix();

}
