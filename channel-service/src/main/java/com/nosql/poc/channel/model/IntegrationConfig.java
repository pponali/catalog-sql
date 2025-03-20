package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;

/**
 * Configuration for channel integrations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntegrationConfig {
    /**
     * Integration type (API, WEBHOOK, FTP, etc).
     */
    @NotBlank
    private String type;
    
    /**
     * Integration endpoint URL or connection string.
     */
    @NotBlank
    private String endpoint;
    
    /**
     * HTTP method (GET, POST, PUT, etc).
     */
    private String method;
    
    /**
     * HTTP headers for API integrations.
     */
    private Map<String, String> headers;
    
    /**
     * Additional configuration parameters.
     */
    private Map<String, Object> configuration;
    
    /**
     * Authentication configuration.
     */
    private AuthConfig authentication;
    
    /**
     * Retry configuration.
     */
    private RetryConfig retryConfig;
    
    /**
     * Whether the integration is currently active.
     */
    private boolean active;
}
