package com.scaler.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configuration for Resilience4j circuit breakers and retry mechanisms
 */
@Configuration
public class Resilience4jConfig {
    
    /**
     * Creates a CircuitBreakerRegistry with configurations for each service
     */
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.ofDefaults();
        
        // Configure CircuitBreaker for ValidationService
        CircuitBreakerConfig validationServiceConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .permittedNumberOfCallsInHalfOpenState(5)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
        registry.addConfiguration("validationService", validationServiceConfig);
        
        // Configure CircuitBreaker for VendorService
        CircuitBreakerConfig vendorServiceConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .permittedNumberOfCallsInHalfOpenState(5)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
        registry.addConfiguration("vendorService", vendorServiceConfig);
        
        // Configure CircuitBreaker for ChannelService
        CircuitBreakerConfig channelServiceConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .permittedNumberOfCallsInHalfOpenState(5)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
        registry.addConfiguration("channelService", channelServiceConfig);
        
        // Configure CircuitBreaker for PartnerService
        CircuitBreakerConfig partnerServiceConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .permittedNumberOfCallsInHalfOpenState(5)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
        registry.addConfiguration("partnerService", partnerServiceConfig);
        
        // Configure CircuitBreaker for RulesService
        CircuitBreakerConfig rulesServiceConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .permittedNumberOfCallsInHalfOpenState(5)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();
        registry.addConfiguration("rulesService", rulesServiceConfig);
        
        return registry;
    }
    
    /**
     * Creates a RetryRegistry with configurations for each service
     */
    @Bean
    public RetryRegistry retryRegistry() {
        RetryRegistry registry = RetryRegistry.ofDefaults();
        
        // Configure Retry for ValidationService
        RetryConfig validationServiceConfig = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(1000))
                .retryExceptions(Exception.class)
                .build();
        registry.addConfiguration("validationService", validationServiceConfig);
        
        // Configure Retry for VendorService
        RetryConfig vendorServiceConfig = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(1000))
                .retryExceptions(Exception.class)
                .build();
        registry.addConfiguration("vendorService", vendorServiceConfig);
        
        // Configure Retry for ChannelService
        RetryConfig channelServiceConfig = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(1000))
                .retryExceptions(Exception.class)
                .build();
        registry.addConfiguration("channelService", channelServiceConfig);
        
        // Configure Retry for PartnerService
        RetryConfig partnerServiceConfig = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(1000))
                .retryExceptions(Exception.class)
                .build();
        registry.addConfiguration("partnerService", partnerServiceConfig);
        
        // Configure Retry for RulesService
        RetryConfig rulesServiceConfig = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(1000))
                .retryExceptions(Exception.class)
                .build();
        registry.addConfiguration("rulesService", rulesServiceConfig);
        
        return registry;
    }
}