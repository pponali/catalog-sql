package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

/**
 * Configuration for API-based channel integration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfiguration {
    @NotBlank(message = "Base URL is required")
    private String baseUrl;
    
    @NotNull(message = "Endpoints configuration is required")
    private Map<String, String> endpoints;
    
    @NotBlank(message = "Auth type is required")
    private String authType;
    
    private Map<String, String> credentials;
    private Map<String, String> headers;
    
    @Valid
    private RetryConfig retryConfig;
}