package com.github.soyanga.secondskillsystem.vo;

import com.github.soyanga.secondskillsystem.domain.Goods;
import lombok.Data;

import java.util.Date;

/**
 * @program: seconds_kill_system _3
 * @Description: 将Goods表的信息和MiaoshaGoods表的信息合并起来
 * @Author: SOYANGA
 * @Create: 2019-05-18 14:41
 * @Version 1.0
 */
@Data
public class GoodsVo extends Goods {
    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
