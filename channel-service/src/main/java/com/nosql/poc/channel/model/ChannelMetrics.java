package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Metrics for channel performance and status.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelMetrics {
    /**
     * Total number of products in the channel.
     */
    private int totalProducts;
    
    /**
     * Number of active products in the channel.
     */
    private int activeProducts;
    
    /**
     * Number of suspended products in the channel.
     */
    private int suspendedProducts;
    
    /**
     * Average quality score of products in the channel.
     */
    private double averageQualityScore;
    
    /**
     * Count of validation errors by type.
     */
    private Map<String, Integer> validationErrors;
    
    /**
     * Count of warnings by type.
     */
    private Map<String, Integer> warningCounts;
    
    /**
     * Last synchronization timestamp.
     */
    private LocalDateTime lastSync;
    
    /**
     * Performance metrics for the channel.
     */
    private Map<String, Object> performanceMetrics;
}
