package com.nosql.poc.channel.model;

import com.nosql.poc.channel.event.AuditInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import java.util.List;

/**
 * Represents a sales or distribution channel.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "channels")
public class Channel {
    /**
     * Unique identifier.
     */
    @Id
    private String id;
    
    /**
     * Channel ID.
     */
    @NotBlank(message = "Channel ID is required")
    private String channelId;
    
    /**
     * Channel name.
     */
    @NotBlank(message = "Channel name is required")
    private String name;
    
    /**
     * Channel type.
     */
    @NotBlank(message = "Channel type is required")
    private String type; // QUICK_COMMERCE, POS, MARKETPLACE, MOBILE_APP, SOCIAL_COMMERCE, PHYSICAL_STORE, ECOMMERCE
    
    /**
     * Channel description.
     */
    private String description;
    
    /**
     * Whether the channel is active.
     */
    private boolean active;
    
    /**
     * Channel configuration.
     */
    private Map<String, Object> configuration;
    
    /**
     * Channel catalog configuration.
     */
    @Valid
    private ChannelCatalog channelCatalog;
    
    /**
     * Validation rules.
     */
    private List<ValidationRule> validationRules;
    
    /**
     * Integration configurations.
     */
    private List<IntegrationConfig> integrations;
    
    /**
     * Channel metrics.
     */
    private ChannelMetrics metrics;
    
    /**
     * Additional attributes.
     */
    private Map<String, Object> attributes;
    
    /**
     * Audit information.
     */
    private AuditInfo auditInfo;
}
