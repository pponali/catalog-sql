package com.nosql.poc.channel.service;

import com.nosql.poc.channel.dto.ChannelPrice;
import com.nosql.poc.channel.exception.ValidationException;
import com.nosql.poc.channel.model.*;
import com.nosql.poc.channel.validation.ValidationResult;
import org.springframework.stereotype.Service;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for channel-related validation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelValidationService {
    
    private final Validator validator;
    
    /**
     * Validate a channel configuration.
     * 
     * @param channel the channel to validate
     * @throws ValidationException if validation fails
     */
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
            throw new ValidationException("Channel configuration validation failed", errors);
        }
    }
    
    /**
     * Validate a product for a specific channel.
     * 
     * @param product the product to validate
     * @param channelId the channel ID
     * @return validation result
     */
    public ValidationResult validateProductForChannel(Product product, String channelId) {
        List<String> errors = new ArrayList<>();
        
        // Check if product is null
        if (product == null) {
            errors.add("Product cannot be null");
            return new ValidationResult(false, errors);
        }
        
        // Check required fields
        if (product.getId() == null || product.getId().trim().isEmpty()) {
            errors.add("Product ID is required");
        }
        
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            errors.add("Product name is required");
        }
        
        if (product.getBasePrice() == null) {
            errors.add("Product base price is required");
        } else if (product.getBasePrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Product base price must be greater than zero");
        }
        
        // If we already have validation errors, no need to continue
        if (!errors.isEmpty()) {
            return new ValidationResult(false, errors);
        }
        
        // Add more specific channel validation rules here
        // For example, check if the product meets channel-specific requirements
        // like minimum price, required attributes, etc.
        
        return new ValidationResult(errors.isEmpty(), errors);
    }
    
    /**
     * Validate a channel price.
     * 
     * @param price the price to validate
     * @throws ValidationException if validation fails
     */
    public void validateChannelPrice(ChannelPrice price) {
        List<String> errors = new ArrayList<>();
        
        if (price == null) {
            throw new ValidationException("Price cannot be null", List.of("Price object is required"));
        }
        
        if (price.getAmount() == null) {
            errors.add("Price amount is required");
        } else if (price.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Price amount must be greater than zero");
        }
        
        if (price.getCurrency() == null || price.getCurrency().trim().isEmpty()) {
            errors.add("Currency is required");
        } else if (price.getCurrency().length() != 3) {
            errors.add("Currency must be a 3-letter ISO currency code");
        }
        
        if (!errors.isEmpty()) {
            throw new ValidationException("Channel price validation failed", errors);
        }
    }
    
    /**
     * Validate a channel activation.
     * 
     * @param channel the channel to validate
     * @throws ValidationException if validation fails
     */
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
            throw new ValidationException("Channel activation validation failed", errors);
        }
    }
    
    /**
     * Check if a channel type is valid.
     * 
     * @param type the channel type
     * @return true if valid
     */
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
    
    /**
     * Validate an integration configuration.
     * 
     * @param integration the integration to validate
     * @param errors list to collect validation errors
     */
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
    
    /**
     * Validate an authentication configuration.
     * 
     * @param auth the auth config to validate
     * @param errors list to collect validation errors
     */
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
    
    /**
     * Validate a retry configuration.
     * 
     * @param retry the retry config to validate
     * @param errors list to collect validation errors
     */
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
    
    /**
     * Validate a validation rule.
     * 
     * @param rule the validation rule to validate
     * @param errors list to collect validation errors
     */
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
