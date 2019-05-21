package com.github.soyanga.secondskillsystem.redis;

/**
 * @program: seconds_kill_system_6
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-05-22 01:41
 * @Version 1.0
 */
public class SeckillKey extends BasePrefix {
    public SeckillKey(String prefix) {
        super(prefix);
    }
    public static SeckillKey isGoodsOver = new SeckillKey("go");
}
