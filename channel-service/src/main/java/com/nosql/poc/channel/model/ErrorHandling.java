package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

/**
 * Error handling configuration for transformation rules.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorHandling {
    private String errorStrategy; // SKIP, RETRY, FALLBACK
    
    private Integer maxRetries;
    
    private String fallbackValue;
    
    private Map<String, Object> errorConfig;
}