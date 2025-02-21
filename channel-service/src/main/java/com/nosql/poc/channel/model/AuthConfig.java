package com.nosql.poc.channel.model;

import lombok.Data;

@Data
public class AuthConfig {
    private String type; // BASIC, OAUTH2, API_KEY
    private String username;
    private String password;
    private String apiKey;
    private String clientId;
    private String clientSecret;
    private String tokenUrl;
    private Map<String, String> additionalParams;
}
