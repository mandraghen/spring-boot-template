package com.template.sbtemplate.integration;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

@ActiveProfiles({"test", "local"})
public abstract class TestContainers {

    @ServiceConnection
    static PostgreSQLContainer<?> postgres;
    @ServiceConnection
    static RedisContainer redis;


    static {
        //init postgres test container
        postgres = new PostgreSQLContainer<>("postgres:17.4");
        postgres.withReuse(true).start();

        //init redis test container
        redis = new RedisContainer("redis:8.0.1");
        redis.start();
        System.setProperty("REDIS_HOST", redis.getRedisHost());
        System.setProperty("REDIS_PORT", String.valueOf(redis.getRedisPort()));
    }
}
