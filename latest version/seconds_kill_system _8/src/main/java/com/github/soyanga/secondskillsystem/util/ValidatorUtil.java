package com.github.soyanga.secondskillsystem.util;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: seconds_kill_system _2
 * @Description: 验证手机号的格式（正则表达式）
 * @Author: SOYANGA
 * @Create: 2019-05-16 14:43
 * @Version 1.0
 */
public class ValidatorUtil {

    private static final Pattern MOBILE_PATTEN = Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src) {
        if (StringUtils.isEmpty(src)) {
            return false;
        }
        Matcher matcher = MOBILE_PATTEN.matcher(src);
        return matcher.matches();
    }
//    public static void main(String[] args) {
//        System.out.println(isMobile("17729506670"));
//        System.out.println(isMobile("1772950667A"));
//        System.out.println(isMobile("17729506"));
//        System.out.println(isMobile("87729506"));
//    }
}
