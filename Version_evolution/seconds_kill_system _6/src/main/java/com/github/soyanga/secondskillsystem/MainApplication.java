package com.github.soyanga.secondskillsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @program: seconds_kill_system
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-05-14 19:27
 * @Version 1.0
 */
@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}


//@SpringBootApplication
//public class MainApplication extends SpringBootServletInitializer {
//
//    public static void main(String[] args) {
//        SpringApplication.run(MainApplication.class, args);
//    }
//
//    //打成war包 Tomcat中会调用该方法启动
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(MainApplication.class);
//    }
//}