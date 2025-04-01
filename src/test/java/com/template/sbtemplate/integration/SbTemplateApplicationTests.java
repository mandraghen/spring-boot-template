package com.template.sbtemplate.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.TestcontainersConfiguration;

@Testcontainers
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class SbTemplateApplicationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.4");

    @Test
    void contextLoads() {
        // This test is used to check if the application context loads successfully
        // You can add more specific tests here as needed
    }

}
