package com.endava.parkinglot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@Slf4j
@ComponentScan(basePackages = "com.endava.parkinglot")
public class HealthCheckConfig {

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public HealthIndicator applicationHealthIndicator() {
        return () -> {
            String databaseStatus = checkDatabaseStatus();
            if (databaseStatus.equals("available")) {
                return Health.up().withDetail("database", "available").build();
            } else {
                return Health.down().withDetail("database", "unavailable").build();
            }
        };
    }

    private String checkDatabaseStatus() {
        try(Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", username, password)) {
            if (connection.isValid(1)) {
                return "available";
            } else {
                return "unavailable";
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            return "unavailable";
        }
    }
}
