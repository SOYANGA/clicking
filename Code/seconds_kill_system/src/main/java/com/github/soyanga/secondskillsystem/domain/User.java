package com.github.soyanga.secondskillsystem.domain;

import lombok.Data;

/**
 * @program: seconds_kill_system
 * @Description: User实体类
 * @Author: SOYANGA
 * @Create: 2019-05-14 21:29
 * @Version 1.0
 */
@Data
public class User {
    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
