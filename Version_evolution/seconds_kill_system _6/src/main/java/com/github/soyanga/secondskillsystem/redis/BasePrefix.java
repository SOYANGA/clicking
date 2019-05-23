package com.github.soyanga.secondskillsystem.redis;

/**
 * @program: seconds_kill_system
 * @Description: redis前缀的抽象类
 * @Author: SOYANGA
 * @Create: 2019-05-15 13:08
 * @Version 1.0
 */
public abstract class BasePrefix implements KeyPrefix {
    /**
     * 过期时间
     */
    private int expireSeconds;

    /**
     * 前缀
     */
    private String prefix;

    public BasePrefix(String prefix) { //默认0代表永不过期
        this(0, prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }


    @Override
    public int expireSeconds() { //默认0代表永不过期
        return expireSeconds;
    }


    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
