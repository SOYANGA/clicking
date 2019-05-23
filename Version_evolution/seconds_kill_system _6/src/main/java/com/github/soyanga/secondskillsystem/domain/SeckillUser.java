package com.github.soyanga.secondskillsystem.domain;

import lombok.Data;

import java.util.Date;

/**
 * @program: seconds_kill_system _2
 * @Description: 秒杀用户实体类
 * @Author: SOYANGA
 * @Create: 2019-05-16 14:53
 * @Version 1.0
 */
@Data
public class SeckillUser {
    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;
}
