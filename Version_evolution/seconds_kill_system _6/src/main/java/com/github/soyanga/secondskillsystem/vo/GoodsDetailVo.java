package com.github.soyanga.secondskillsystem.vo;

import com.github.soyanga.secondskillsystem.domain.SeckillUser;
import lombok.Data;

/**
 * @program: seconds_kill_system_5
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-05-20 21:30
 * @Version 1.0
 */
@Data
public class GoodsDetailVo {
    //表示秒杀的状态 0没有开始，1已经开始 2已将结束了
    private int seckillStatus = 0;
    //剩余多少时间
    private long remainSeconds = 0;
    //商品详情
    private GoodsVo goods;
    //用户地址
    private SeckillUser user;
}
