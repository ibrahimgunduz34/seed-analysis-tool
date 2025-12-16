package com.seed.core;

import org.hibernate.boot.registry.StandardServiceRegistry;
import org.redisson.api.RedissonClient;
import org.redisson.hibernate.RedissonRegionFactory;

import java.util.Map;

public class ExposedRedissonRegionFactory extends RedissonRegionFactory {
    private static RedissonClient redissonClient;

    public static RedissonClient getRedissonClient() {
        return redissonClient;
    }

    @Override
    protected RedissonClient createRedissonClient(StandardServiceRegistry registry, Map properties) {
        redissonClient = super.createRedissonClient(registry, properties);
        return redissonClient;
    }
}
