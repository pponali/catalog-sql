package com.nosql.poc.channel.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import java.util.List;

@Data
@Document(collection = "channels")
public class Channel {
    @Id
    private String id;
    
    @NotBlank
    private String name;
    
    @NotBlank
    private String type; // QUICK_COMMERCE, POS, MARKETPLACE, MOBILE_APP, SOCIAL_COMMERCE, PHYSICAL_STORE, ECOMMERCE
    
    private String description;
    
    private boolean active;
    
    private Map<String, Object> configuration;
    
    private List<ValidationRule> validationRules;
    
    private List<IntegrationConfig> integrations;
    
    private ChannelMetrics metrics;
    
    private Map<String, Object> attributes;
    
    private AuditInfo auditInfo;
}
