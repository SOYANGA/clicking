package com.github.soyanga.secondskillsystem.redis;

/**
 * @program: seconds_kill_system_7
 * @Description: 限制访问次数的key
 * @Author: SOYANGA
 * @Create: 2019-05-23 00:44
 * @Version 1.0
 */
public class AccessKey extends BasePrefix {
    public AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }


    /**
     * 动态限制访问次数
     * @param expireSeconds key的有效期，即限制用户一段时间内登陆次数的时间设置
     * @return
     */
    public static AccessKey withExpire(int expireSeconds){
        return new AccessKey(expireSeconds,"access");
    }
}
