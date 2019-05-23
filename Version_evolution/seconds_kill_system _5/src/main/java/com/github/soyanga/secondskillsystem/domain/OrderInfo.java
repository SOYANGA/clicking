package com.github.soyanga.secondskillsystem.domain;

import lombok.Data;

import java.util.Date;

/**
 * @program: seconds_kill_system _3
 * @Description: 秒杀订单实体类
 * @Author: SOYANGA
 * @Create: 2019-05-18 14:35
 * @Version 1.0
 */
@Data
public class OrderInfo {
    private Long id; //订单Id
    private Long userId;
    private Long goodsId;
    private Long deliveryAddrId;
    private String goodsName;
    private Integer goodsCount;
    private Double goodsPrice;
    private Integer orderChannel;
    private Integer status;
    private Date createDate;
    private Date payDate;
}
