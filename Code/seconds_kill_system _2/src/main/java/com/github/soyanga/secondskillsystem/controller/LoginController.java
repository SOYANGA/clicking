package com.github.soyanga.secondskillsystem.controller;

import com.github.soyanga.secondskillsystem.redis.RedisService;
import com.github.soyanga.secondskillsystem.result.CodeMsg;
import com.github.soyanga.secondskillsystem.result.Result;
import com.github.soyanga.secondskillsystem.service.MiaoshaUserService;
import com.github.soyanga.secondskillsystem.service.UserService;
import com.github.soyanga.secondskillsystem.util.ValidatorUtil;
import com.github.soyanga.secondskillsystem.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @program: seconds_kill_system _2
 * @Description: 登陆页面控制器
 * @Author: SOYANGA
 * @Create: 2019-05-16 11:34
 * @Version 1.0
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    RedisService redisService;

    @Autowired
    MiaoshaUserService userService;

    /**
     * 登陆页面
     *
     * @return
     */
    @RequestMapping("/to_login")
    public String toLoagin() {
        return "login";
    }

    /**
     * 登陆具体操作
     *
     * @return
     */
    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        //在前端讲参数传输到后台时 参数校验注解生效 就会进行参数校验，异常处理都会在统一的异常处理器中去处理
        logger.info(loginVo.toString());
        //登陆+前后端数据库参数校验 出现异常也会在统一的异常处理器中去处理+生成在Redis中存储，并发送给浏览器Session
        userService.login(response, loginVo);
        return Result.success(true);
    }
}

//参数校验

//        //1.密码是否为空
//        String mobile = loginVo.getMobile();
//        String passInput = loginVo.getPassword();
//        if (StringUtils.isEmpty(passInput)) {
//            return Result.error(CodeMsg.PASSWORD_EMPTY);
//        }
//        //2.手机号是否为空
//        if (StringUtils.isEmpty(mobile)) {
//            return Result.error(CodeMsg.MOBILE_EMPTY);
//        }
//        //3.手机号输入非法
//        if (!ValidatorUtil.isMobile(mobile)) {
//            return Result.error(CodeMsg.MOBILE_ERROR);
//        }
//登陆 获取二次校验（数据库校验）登陆信息 返回请求结果信息