package com.nosql.poc.channel.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class IntegrationConfig {
    @NotBlank
    private String type; // API, WEBHOOK, FTP, etc.
    
    @NotBlank
    private String endpoint;
    
    private String method; // GET, POST, PUT, etc.
    
    private Map<String, String> headers;
    
    private Map<String, Object> configuration;
    
    private AuthConfig authentication;
    
    private RetryConfig retryConfig;
    
    private boolean active;
}
