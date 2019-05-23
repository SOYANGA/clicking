package com.github.soyanga.secondskillsystem.domain;

import lombok.Data;

import java.util.Date;

/**
 * @program: seconds_kill_system _3
 * @Description: 秒杀商品实体类
 * @Author: SOYANGA
 * @Create: 2019-05-18 14:34
 * @Version 1.0
 */
@Data
public class SeckillGoods {
    private Long id;
    private Long goodsId;
    private Double seckillPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
