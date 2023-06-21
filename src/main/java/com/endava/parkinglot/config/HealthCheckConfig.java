package com.endava.parkinglot.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@ComponentScan(basePackages = "com.endava.parkinglot")
public class HealthCheckConfig {
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
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
            if (connection.isValid(1)) {
                connection.close();
                return "available";
            } else {
                connection.close();
                return "unavailable";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "unavailable";
        }
    }
}
