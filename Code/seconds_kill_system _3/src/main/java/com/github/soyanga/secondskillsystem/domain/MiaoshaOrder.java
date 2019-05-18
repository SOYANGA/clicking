package com.github.soyanga.secondskillsystem.domain;

import lombok.Data;

/**
 * @program: seconds_kill_system _3
 * @Description: 秒杀订单实体类
 * @Author: SOYANGA
 * @Create: 2019-05-18 14:35
 * @Version 1.0
 */
@Data
public class MiaoshaOrder {
    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;
}
