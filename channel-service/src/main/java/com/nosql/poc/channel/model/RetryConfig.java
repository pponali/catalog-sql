package com.nosql.poc.channel.model;

import lombok.Data;

@Data
public class RetryConfig {
    private int maxAttempts;
    private long initialDelay;
    private long maxDelay;
    private double multiplier;
    private List<String> retryableExceptions;
}
