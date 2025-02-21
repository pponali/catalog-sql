package com.nosql.poc.vendor.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class VendorChannel {
    private String channelId;
    
    private String channelName;
    
    private String channelType; // MARKETPLACE, QUICK_COMMERCE, POS
    
    private LocalDateTime activationDate;
    
    private Boolean isActive;
    
    private ChannelConfiguration configuration;
    
    private List<String> enabledCategories;
    
    private List<String> serviceableRegions;
    
    private Map<String, Object> channelAttributes;
}

@Data
public class ChannelConfiguration {
    private String integrationMode;
    private Map<String, String> apiCredentials;
    private Map<String, String> endpoints;
    private Map<String, Object> channelSettings;
    private List<String> supportedFeatures;
    private Map<String, ValidationRule> validationRules;
}
