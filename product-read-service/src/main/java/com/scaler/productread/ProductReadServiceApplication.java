package com.scaler.productread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for the Product Read Service.
 * This service provides a read-optimized GraphQL API for product data using Elasticsearch.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCaching
@EnableScheduling
@EnableAsync
public class ProductReadServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ProductReadServiceApplication.class, args);
    }
}