package com.github.soyanga.secondskillsystem.redis;

/**
 * @program: seconds_kill_system
 * @Description: User添加前缀的实现类
 * @Author: SOYANGA
 * @Create: 2019-05-15 13:12
 * @Version 1.0
 */
public class UserKey extends BasePrefix {
    public UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey geyById = new UserKey("id");
    public static UserKey geyByName = new UserKey("name");
}
