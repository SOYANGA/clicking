package com.github.soyanga.secondskillsystem.vo;

import com.github.soyanga.secondskillsystem.domain.OrderInfo;
import lombok.Data;

/**
 * @program: seconds_kill_system_5
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-05-21 00:05
 * @Version 1.0
 */
@Data
public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;
}
