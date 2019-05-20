package com.github.soyanga.secondskillsystem.redis;

/**
 * @program: seconds_kill_system_5
 * @Description: 关于商品详情页进行缓存 处理的key值填充
 * @Author: SOYANGA
 * @Create: 2019-05-20 14:28
 * @Version 1.0
 */

public class GoodsKey extends BasePrefix {

    private GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60, "gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(60, "gd");
}
