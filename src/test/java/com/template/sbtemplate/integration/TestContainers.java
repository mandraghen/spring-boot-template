package com.template.sbtemplate.integration;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

abstract public class TestContainers {
    @ServiceConnection
    static PostgreSQLContainer<?> postgres;

    static {
        postgres = new PostgreSQLContainer<>("postgres:17.4");
        postgres.withReuse(true).start();
    }
}
