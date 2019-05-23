package com.github.soyanga.secondskillsystem.redis;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
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
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig {
    private String host;  //主机
    private int port;  //端口
    private int timeout;  //超时时间
    private String password;

    @Value("${spring.redis.jedis.pool.max-active}")
    private int poolMaxActive;  //连接池最大线程数

    @Value("${spring.redis.jedis.pool.max-wait}")
    private long poolMaxWait;  //等待时间

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int poolMaxIdle;//最大空闲连接
}
