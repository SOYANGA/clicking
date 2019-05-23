package com.github.soyanga.secondskillsystem.config;

import com.github.soyanga.secondskillsystem.access.UserContext;
import com.github.soyanga.secondskillsystem.domain.SeckillUser;
import com.github.soyanga.secondskillsystem.service.SeckillUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: seconds_kill_system _2
 * @Description: 处理方法参数解析器（SeckillUser专属）  注释部分代码是因为用AccessInterceptor拦截器部分功能给替代了
 * @Author: SOYANGA
 * @Create: 2019-05-18 01:25
 * @Version 1.0
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {


    @Autowired
    SeckillUserService userService;


    /**
     * 判断秒杀user的对象（作为Controller 参数之前进行拦截）
     * 设定需要解析的类
     *
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        return clazz == SeckillUser.class;
    }

    /**
     * 解析参数，将原生的SeckillUser user对象直接替换为从Redis数据库中所查找的对象User，登陆后续内容直接使用 拦截器先于参数解析器前执行
     *
     * @param parameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        return UserContext.getUser();

//        注释部分代码是因为用AccessInterceptor拦截器部分功能给替代了
//        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
//        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
//        String paramToken = request.getParameter(SeckillUserService.COOKIE_NAME_TOKEN);
//        String cookieToken = getCookieValue(request, SeckillUserService.COOKIE_NAME_TOKEN);
//        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
//            return null;
//        }
//        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
//        return userService.getByToken(response, token);
    }

//    private String getCookieValue(HttpServletRequest request, String cookieName) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies == null || cookies.length <= 0) {
//            return null;
//        }
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals(cookieName)) {
//                return cookie.getValue();
//            }
//        }
//        return null;
//    }
}
