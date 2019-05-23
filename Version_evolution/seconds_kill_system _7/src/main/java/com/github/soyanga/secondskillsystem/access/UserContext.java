package com.github.soyanga.secondskillsystem.access;

import com.github.soyanga.secondskillsystem.domain.SeckillUser;

/**
 * @program: seconds_kill_system_7
 * @Description: 临时保存从redis中获取的用户（之前登陆过的用户）-->将其变成user参数
 * @Author: SOYANGA
 * @Create: 2019-05-23 01:27
 * @Version 1.0
 */
public class UserContext {

    private static ThreadLocal<SeckillUser> userHolder = new ThreadLocal<SeckillUser>();

    public static void setUser(SeckillUser user) {
        userHolder.set(user);
    }

    public static SeckillUser getUser() {
        return userHolder.get();
    }
}
