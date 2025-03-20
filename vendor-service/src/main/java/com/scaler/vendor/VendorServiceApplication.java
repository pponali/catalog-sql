package com.scaler.vendor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class VendorServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(VendorServiceApplication.class, args);
    }
}