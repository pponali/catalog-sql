package com.nosql.poc.vendor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Main application class for the Vendor Service.
 */
@SpringBootApplication
@EnableMongoRepositories
@EnableFeignClients(basePackages = "com.nosql.poc.vendor.client")
@EnableDiscoveryClient
public class VendorServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(VendorServiceApplication.class, args);
    }
}