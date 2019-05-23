package com.github.soyanga.secondskillsystem.access;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @program: seconds_kill_system_7
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-05-23 01:04
 * @Version 1.0
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {
    int seconds();

    int maxCount();

    //默认需要登陆
    boolean needLogin() default true;
}
