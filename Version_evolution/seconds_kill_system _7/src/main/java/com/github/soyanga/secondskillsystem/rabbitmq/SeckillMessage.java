package com.github.soyanga.secondskillsystem.rabbitmq;

import com.github.soyanga.secondskillsystem.domain.SeckillUser;
import lombok.Data;

/**
 * @Author: SOYANGA
 * @Descripition: 秒杀信息的入队封装
 * @Date:0:49 2019/5/22
 */
@Data
public class SeckillMessage {
    private SeckillUser user;
    private long goodsId;
}
