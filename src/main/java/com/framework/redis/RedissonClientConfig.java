package com.framework.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.redisson.config.Config;

/**
 * @ClassName RedissonClient
 * @Description TODO
 * @Author hxh
 * @Date 3/28/21 5:47 下午
 * @Version 1.0
 */
@Component
public class RedissonClientConfig {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Bean
    public RedissonClient redissonClient(){
        RedissonClient redissonClient;

        Config config = new Config();
        String url = "redis://" + host + ":" + port;
        config.useSingleServer().setAddress(url);

        try {
            redissonClient = Redisson.create(config);
            return redissonClient;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
