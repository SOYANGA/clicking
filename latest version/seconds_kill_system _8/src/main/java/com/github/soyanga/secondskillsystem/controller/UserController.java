package com.github.soyanga.secondskillsystem.controller;

import com.github.soyanga.secondskillsystem.domain.SeckillUser;
import com.github.soyanga.secondskillsystem.redis.RedisService;
import com.github.soyanga.secondskillsystem.result.Result;
import com.github.soyanga.secondskillsystem.service.SeckillUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: seconds_kill_system_4
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-05-19 00:55
 * @Version 1.0
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    RedisService redisService;

    @Autowired
    SeckillUserService userService;


    @RequestMapping("info")
    @ResponseBody
    public Result<SeckillUser> info(Model model, SeckillUser user) {
        return Result.success(user);
    }
}
