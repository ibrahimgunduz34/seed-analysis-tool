package org.seed.config;

import org.redisson.api.RedissonClient;
import org.seed.ExposedRedissonRegionFactory;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class RedissonContextCloser implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        RedissonClient redissonClient = ExposedRedissonRegionFactory.getRedissonClient();

        if (redissonClient != null && !redissonClient.isShutdown()) {
            System.out.println("Gracefully shutting down Redisson client on ContextClosedEvent...");
            redissonClient.shutdown();
        }
    }
}
