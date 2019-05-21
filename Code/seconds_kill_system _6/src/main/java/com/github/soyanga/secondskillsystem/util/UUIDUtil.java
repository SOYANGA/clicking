package com.github.soyanga.secondskillsystem.util;

import java.util.UUID;

/**
 * @program: seconds_kill_system _2
 * @Description: 随机生成一个ID
 * @Author: SOYANGA
 * @Create: 2019-05-17 17:06
 * @Version 1.0
 */
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
