package com.github.soyanga.secondskillsystem.redis;

/**
 * @program: seconds_kill_system_5
 * @Description: 关于商品详情页,商品秒杀页，以及商品库存 进行缓存 处理的key值填充
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
    public static GoodsKey getSeckillGoodsStock = new GoodsKey(0, "gs");
}
