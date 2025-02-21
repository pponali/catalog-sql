package com.nosql.poc.channel.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChannelMetrics {
    private int totalProducts;
    private int activeProducts;
    private int suspendedProducts;
    private double averageQualityScore;
    private Map<String, Integer> validationErrors;
    private Map<String, Integer> warningCounts;
    private LocalDateTime lastSync;
    private Map<String, Object> performanceMetrics;
}
