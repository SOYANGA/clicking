package com.github.soyanga.secondskillsystem.domain;

import lombok.Data;

/**
 * @program: seconds_kill_system _3
 * @Description: 商品实体类
 * @Author: SOYANGA
 * @Create: 2019-05-18 14:33
 * @Version 1.0
 */
@Data
public class Goods {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
}
