package com.github.soyanga.secondskillsystem.controller;

import com.github.soyanga.secondskillsystem.domain.User;
import com.github.soyanga.secondskillsystem.rabbitmq.MQSender;
import com.github.soyanga.secondskillsystem.redis.RedisService;
import com.github.soyanga.secondskillsystem.redis.UserKey;
import com.github.soyanga.secondskillsystem.result.CodeMsg;
import com.github.soyanga.secondskillsystem.result.Result;
import com.github.soyanga.secondskillsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: seconds_kill_system
 * @Description: SpringBoot项目搭建练习
 * @Author: SOYANGA
 * @Create: 2019-05-14 19:29
 * @Version 1.0
 */
@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender sender;

    //简单验证
    @RequestMapping("/")
    @ResponseBody
    public String home() {
        return "Hello World";
    }

    //result正确结果封装验证
    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello() {
        return Result.success("hello,imooc");
    }

    //result错误结果封装验证
    @RequestMapping("/helloError")
    @ResponseBody
    public Result<String> helloError() {
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    //thymeleaf模板引擎使用
    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "SOYANGA");
        return "hello";
    }

    //数据库查询测试
    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet() {
        User user = userService.getById(1);
        return Result.success(user);
    }

    //数据库事务测试
    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx() {
        userService.tx();
        return Result.success(true);
    }

//    @RequestMapping("/redis/get")
//    @ResponseBody
//    public Result<Long> redisGet() {
//        Long value1 = redisService.get("key1", Long.class);
//        return Result.success(value1);
//    }

    //Redis缓存查询测试
    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet2() {
        User user = redisService.get(UserKey.geyById, "" + 1, User.class);
        return Result.success(user);
    }


//    @RequestMapping("/redis/set")
//    @ResponseBody
//    public Result<String> redisSet() {
//        Boolean ret = redisService.set("key2", "hello Redis");
//        String str = redisService.get("key2", String.class);
//        return Result.success(str);
//    }

    //Redis缓存插入测试
    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
        User user = new User(1, "soyanga");
        Boolean ret = redisService.set(UserKey.geyById, "" + 1, user);
        return Result.success(true);
    }

//    /**
//     * 验证RabbitMq Dricet模式
//     *
//     * @return
//     */
//    @RequestMapping("/mq")
//    @ResponseBody
//    public Result<String> mq() {
//        sender.send("Hello RabbitMq");
//        return Result.success("hello,RabbitMq");
//    }
//
//
//    /**
//     * 验证RabbitMq Topic模式
//     *
//     * @return
//     */
//    @RequestMapping("/mq/topic")
//    @ResponseBody
//    public Result<String> topicMq() {
//        sender.sendTopic("Hello RabbitMq topic");
//        return Result.success("hello,RabbitMq topic");
//    }
//
//    /**
//     * 验证RabbitMq Topic模式
//     * swagger
//     *
//     * @return
//     */
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public Result<String> fanoutMq() {
//        sender.sendFanout("Hello RabbitMq fanout");
//        return Result.success("hello,RabbitMq fanout");
//    }
//
//    /**
//     * 验证Headers模式
//     * @return
//     */
//    @RequestMapping("/mq/header")
//    @ResponseBody
//    public Result<String> header() {
//        sender.sendHeader("Hello RabbitMq headers");
//        return Result.success("hello，world");
//    }

}
