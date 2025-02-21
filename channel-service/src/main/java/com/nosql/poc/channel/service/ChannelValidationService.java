package com.nosql.poc.channel.service;

import com.nosql.poc.channel.model.*;
import org.springframework.stereotype.Service;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelValidationService {
    
    private final Validator validator;
    
    public void validateChannelConfiguration(Channel channel) {
        List<String> errors = new ArrayList<>();
        
        // Basic validation using Bean Validation
        validator.validate(channel).forEach(violation -> 
            errors.add(violation.getPropertyPath() + ": " + violation.getMessage()));
        
        // Validate channel type
        if (!isValidChannelType(channel.getType())) {
            errors.add("Invalid channel type: " + channel.getType());
        }
        
        // Validate integrations
        if (channel.getIntegrations() != null) {
            channel.getIntegrations().forEach(integration -> 
                validateIntegrationConfig(integration, errors));
        }
        
        // Validate validation rules
        if (channel.getValidationRules() != null) {
            channel.getValidationRules().forEach(rule -> 
                validateValidationRule(rule, errors));
        }
        
        if (!errors.isEmpty()) {
            throw new ValidationException("Channel configuration validation failed: " + errors);
        }
    }
    
    public void validateChannelActivation(Channel channel) {
        List<String> errors = new ArrayList<>();
        
        // Check if integrations are configured and tested
        if (channel.getIntegrations() != null && !channel.getIntegrations().isEmpty()) {
            channel.getIntegrations().forEach(integration -> {
                if (!integration.isActive()) {
                    errors.add("Integration " + integration.getType() + " is not active");
                }
            });
        }
        
        // Check if required validation rules are present
        if (channel.getValidationRules() == null || channel.getValidationRules().isEmpty()) {
            errors.add("No validation rules configured for channel");
        }
        
        if (!errors.isEmpty()) {
            throw new ValidationException("Channel activation validation failed: " + errors);
        }
    }
    
    private boolean isValidChannelType(String type) {
        return type != null && (
            type.equals("QUICK_COMMERCE") ||
            type.equals("POS") ||
            type.equals("MARKETPLACE") ||
            type.equals("MOBILE_APP") ||
            type.equals("SOCIAL_COMMERCE") ||
            type.equals("PHYSICAL_STORE") ||
            type.equals("ECOMMERCE")
        );
    }
    
    private void validateIntegrationConfig(IntegrationConfig integration, List<String> errors) {
        if (integration.getEndpoint() == null || integration.getEndpoint().trim().isEmpty()) {
            errors.add("Integration endpoint is required");
        }
        
        if (integration.getAuthentication() != null) {
            validateAuthConfig(integration.getAuthentication(), errors);
        }
        
        if (integration.getRetryConfig() != null) {
            validateRetryConfig(integration.getRetryConfig(), errors);
        }
    }
    
    private void validateAuthConfig(AuthConfig auth, List<String> errors) {
        if (auth.getType() == null) {
            errors.add("Authentication type is required");
            return;
        }
        
        switch (auth.getType()) {
            case "BASIC":
                if (auth.getUsername() == null || auth.getPassword() == null) {
                    errors.add("Username and password required for BASIC auth");
                }
                break;
            case "OAUTH2":
                if (auth.getClientId() == null || auth.getClientSecret() == null || 
                    auth.getTokenUrl() == null) {
                    errors.add("ClientId, ClientSecret and TokenUrl required for OAUTH2");
                }
                break;
            case "API_KEY":
                if (auth.getApiKey() == null) {
                    errors.add("API Key required for API_KEY auth");
                }
                break;
            default:
                errors.add("Invalid authentication type: " + auth.getType());
        }
    }
    
    private void validateRetryConfig(RetryConfig retry, List<String> errors) {
        if (retry.getMaxAttempts() <= 0) {
            errors.add("Max attempts must be greater than 0");
        }
        
        if (retry.getInitialDelay() <= 0) {
            errors.add("Initial delay must be greater than 0");
        }
        
        if (retry.getMaxDelay() <= retry.getInitialDelay()) {
            errors.add("Max delay must be greater than initial delay");
        }
    }
    
    private void validateValidationRule(ValidationRule rule, List<String> errors) {
        if (rule.getName() == null || rule.getName().trim().isEmpty()) {
            errors.add("Validation rule name is required");
        }
        
        if (rule.getType() == null || rule.getType().trim().isEmpty()) {
            errors.add("Validation rule type is required");
        }
        
        if (rule.getSeverity() == null || 
            (!rule.getSeverity().equals("ERROR") && !rule.getSeverity().equals("WARNING"))) {
            errors.add("Invalid severity level: " + rule.getSeverity());
        }
    }
}
