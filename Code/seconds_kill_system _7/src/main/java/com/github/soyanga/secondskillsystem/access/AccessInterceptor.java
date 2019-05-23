package com.github.soyanga.secondskillsystem.access;

import com.alibaba.fastjson.JSON;
import com.github.soyanga.secondskillsystem.domain.SeckillUser;
import com.github.soyanga.secondskillsystem.redis.AccessKey;
import com.github.soyanga.secondskillsystem.redis.RedisService;
import com.github.soyanga.secondskillsystem.result.CodeMsg;
import com.github.soyanga.secondskillsystem.result.Result;
import com.github.soyanga.secondskillsystem.service.SeckillService;
import com.github.soyanga.secondskillsystem.service.SeckillUserService;
import com.github.soyanga.secondskillsystem.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: seconds_kill_system_7
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-05-23 01:08
 * @Version 1.0
 */
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {


    @Autowired
    SeckillUserService userService;

    @Autowired
    RedisService redisService;

    /**
     * 方法执行之前
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            //将方法上已经登陆的用户的参数转变成从Redis中按照之前token存放的用户。
            SeckillUser user = getUser(request, response);
            //将查找出来的user放入ThreadLocl中
            UserContext.setUser(user);

            //访问次数限制 注解配置
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取方法上的注解
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();

            //判断用户是否需要登陆才能使用该操作
            if (needLogin) {
                if (user == null) {
                    //给客户端提醒且不会进入注解标识的页面（拦截器进行拦截了）
                    render(response, CodeMsg.SESSION_ERROR);
                    return false;
                }
                key += "_" + user.getId();
            } else {
                //do nothing
            }
            AccessKey accessKeyPrefix = AccessKey.withExpire(seconds);
            //此时限制用户一段时间内登陆次数
            Integer count = redisService.get(accessKeyPrefix, "" + key, Integer.class);
            if (count == null) {
                redisService.set(accessKeyPrefix, "" + key, 1);
            } else if (count < maxCount) {
                redisService.incr(accessKeyPrefix, key);
            } else {
                render(response, CodeMsg.ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return true;
    }

    /**
     * 给客户端发出提醒未登录提醒
     *
     * @param response
     * @param sessionErrorMessage
     * @throws Exception
     */
    private void render(HttpServletResponse response, CodeMsg sessionErrorMessage) throws Exception {
        response.setContentType("application/json;charset=UTF-8");   //返回的数据的编码方式
        ServletOutputStream out = response.getOutputStream();
        String errorMessage = JSON.toJSONString(sessionErrorMessage);
        out.write(errorMessage.getBytes("UTF-8"));
        out.flush();
        out.close();
//        ServletOutputStream out = null;
//        try {
//            out = response.getOutputStream();
//            String errorMessage = JSON.toJSONString(sessionErrorMessage);
//            out.write(errorMessage.getBytes("UTF-8"));
//        } finally {
//            if (out != null) {
//                out.flush();
//            }
//            if (out != null) {
//                out.close();
//            }
//        }
    }

    /**
     * 将方法上已经登陆的用户的参数转变成从Redis中按照之前登陆后token存放的用户，分布式Session
     *
     * @return
     */
    private SeckillUser getUser(HttpServletRequest request, HttpServletResponse response) {
        String paramToken = request.getParameter(SeckillUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, SeckillUserService.COOKIE_NAME_TOKEN);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return null;
        }
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        return userService.getByToken(response, token);
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
