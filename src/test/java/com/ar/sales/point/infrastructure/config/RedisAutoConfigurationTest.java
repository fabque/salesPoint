package com.ar.sales.point.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisAutoConfigurationTest {

    @Autowired
    ApplicationContext ctx;

    @Test
    void redisBeansShouldBePresentWhenConfigured() {
        // If the classes are on the classpath and properties are set, the beans should be present
        boolean hasRedisConnFactory = !ctx.getBeansOfType(RedisConnectionFactory.class).isEmpty();
        boolean hasCacheManager = !ctx.getBeansOfType(CacheManager.class).isEmpty();

        assertThat(hasRedisConnFactory).isTrue();
        assertThat(hasCacheManager).isTrue();
    }
}
