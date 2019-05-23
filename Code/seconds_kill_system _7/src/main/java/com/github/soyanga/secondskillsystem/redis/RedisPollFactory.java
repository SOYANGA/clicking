package com.github.soyanga.secondskillsystem.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @program: seconds_kill_system
 * @Description: Redis连接池工厂产生Redis连接池
 * @Author: SOYANGA
 * @Create: 2019-05-15 12:03
 * @Version 1.0
 */
@Service
public class RedisPollFactory {

    @Autowired
    RedisConfig redisConfig;

    @Bean
    public JedisPool jedisPoolFactory() {
        JedisPoolConfig poolconfig = new JedisPoolConfig();
        poolconfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        poolconfig.setMaxTotal(redisConfig.getPoolMaxActive());
        poolconfig.setMaxWaitMillis(redisConfig.getPoolMaxWait()); //毫秒配置的
        JedisPool jedisPool = new JedisPool(poolconfig, redisConfig.getHost(),
                redisConfig.getPort(),
                redisConfig.getTimeout() * 1000, redisConfig.getPassword(), 0);
        return jedisPool;
    }

}
