package com.github.soyanga.secondskillsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @program: seconds_kill_system _2
 * @Description: webMvc的配置（主要用于添加参数解析器）
 * @Author: SOYANGA
 * @Create: 2019-05-18 01:08
 * @Version 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * SeckillUser参数解析器
     */
    @Autowired
    UserArgumentResolver userArgumentResolver;

    /**
     * 添加参数解析器 到解析器队列中，对SeckillUser user对象进行拦截以及再次解析，返回从Redis中查找出来的user对象
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userArgumentResolver);
    }
}
