package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

/**
 * Authentication configuration for channel integrations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthConfig {
    /**
     * Authentication type (BASIC, OAUTH2, API_KEY).
     */
    private String type;
    
    /**
     * Username for basic auth.
     */
    private String username;
    
    /**
     * Password for basic auth.
     */
    private String password;
    
    /**
     * API key for API_KEY auth.
     */
    private String apiKey;
    
    /**
     * Client ID for OAuth2.
     */
    private String clientId;
    
    /**
     * Client secret for OAuth2.
     */
    private String clientSecret;
    
    /**
     * Token URL for OAuth2.
     */
    private String tokenUrl;
    
    /**
     * Additional auth parameters.
     */
    private Map<String, String> additionalParams;
}
