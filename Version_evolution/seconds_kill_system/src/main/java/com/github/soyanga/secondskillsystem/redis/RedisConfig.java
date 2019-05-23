package com.github.soyanga.secondskillsystem.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: seconds_kill_system
 * @Description: Redis配置
 * @Author: SOYANGA
 * @Create: 2019-05-15 10:46
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {
    private String host;
    private int port;
    private int timeout;//单位是秒
    private String password;
    private int poolMaxTotal;
    private int poolMaxIdle;
    private int poolMaxWait;
}
