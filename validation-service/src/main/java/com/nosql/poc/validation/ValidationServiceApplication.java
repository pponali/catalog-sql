package com.nosql.poc.validation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Main application class for the Validation Service.
 */
@SpringBootApplication
@EnableMongoRepositories
@EnableDiscoveryClient
@EnableFeignClients
public class ValidationServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ValidationServiceApplication.class, args);
    }
}