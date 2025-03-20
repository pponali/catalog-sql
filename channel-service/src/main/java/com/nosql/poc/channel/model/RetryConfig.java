package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

/**
 * Configuration for retry logic in API calls.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetryConfig {
    private int maxAttempts;
    private long initialDelay;
    private long maxDelay;
    private double multiplier;
    private List<String> retryableExceptions;
    private Integer maxRetries;
    private Integer retryInterval;
    private List<String> retryableErrors;
    private Map<String, Object> retryStrategy;
}
