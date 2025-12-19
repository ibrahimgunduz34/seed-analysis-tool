package com.seed.configuration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    private final String address;
    private final int redisPort;

    public RedissonConfig(
            @Value("${REDIS_HOST:localhost}") String address,
            @Value("${REDIS_HOST:6379}") int redisPort
    ) {
        this.address = address;
        this.redisPort = redisPort;
    }

    @Bean
    public RedissonClient createRedissonClient() {
        Config config = new Config();

        config.useSingleServer()
                .setAddress("redis://" + address + ":" + redisPort)
                .setDatabase(0)
                .setTimeout(1000)
                .setIdleConnectionTimeout(1000)
                .setRetryAttempts(3);

        return Redisson.create(config);
    }
}
