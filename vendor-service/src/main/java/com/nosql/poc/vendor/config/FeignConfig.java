package com.nosql.poc.vendor.config;

import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Feign client configuration for the vendor service.
 */
@Configuration
public class FeignConfig {
    
    /**
     * Configure feign retry behavior.
     * This sets up a backoff policy for retrying failed requests.
     */
    @Bean
    public Retryer retryer() {
        // Initial backoff of 100ms, maximum backoff of 3s, retry up to 3 times
        return new Retryer.Default(100, TimeUnit.MILLISECONDS.toMillis(3000), 3);
    }
    
    /**
     * Custom error decoder for Feign clients.
     * This converts HTTP error responses into appropriate exceptions.
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new VendorServiceErrorDecoder();
    }
}