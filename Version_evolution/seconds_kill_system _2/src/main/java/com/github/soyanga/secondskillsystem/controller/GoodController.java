package com.github.soyanga.secondskillsystem.controller;

import com.github.soyanga.secondskillsystem.domain.MiaoShaUser;
import com.github.soyanga.secondskillsystem.redis.RedisService;
import com.github.soyanga.secondskillsystem.result.Result;
import com.github.soyanga.secondskillsystem.service.MiaoshaUserService;
import com.github.soyanga.secondskillsystem.util.ValidatorUtil;
import com.github.soyanga.secondskillsystem.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @program: seconds_kill_system _2
 * @Description: 登陆成功挑转到商品页面
 * @Author: SOYANGA
 * @Create: 2019-05-17 17:34
 * @Version 1.0
 */
@Controller
@RequestMapping("/goods")
public class GoodController {


    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService userService;

    /**
     * 商品列表页 单点登陆 User已将通过UserArgumentResolver解析并通过WebConfig加入到解析队列中
     *
     * @return
     */
    @RequestMapping("/to_list")
    public String toGoodsList(MiaoShaUser user, Model model) {
        model.addAttribute("user", user);
        return "goods_list";
    }

//    /**
//     * 商品列表页 单点登陆
//     *
//     * @return
//     */
//    @RequestMapping("/to_list")
//    public String toGoodsList(HttpServletResponse response,
//                              Model model,
//                              @CookieValue(value = MiaoshaUserService.COOKIE_NAME_TOKEN, required = false) String cookieToken,
//                              @RequestParam(value = MiaoshaUserService.COOKIE_NAME_TOKEN, required = false) String paramToken) {
//        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//            return "login";
//        }
//        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
//        MiaoShaUser user = userService.getByToken(response, token);
//        model.addAttribute("user", user);
//        return "goods_list";
//    }

}