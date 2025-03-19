package com.scaler.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Configuration class to enable Spring Retry functionality
 */
@Configuration
@EnableRetry
public class RetryConfig {
    // No additional configuration needed, just enabling retry functionality
}
