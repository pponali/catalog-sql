package com.nosql.poc.channel.model;

import com.nosql.poc.channel.validation.constraints.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Represents a catalog configuration for a specific channel.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "channel_catalogs")
public class ChannelCatalog {
    /**
     * Unique identifier.
     */
    @Id
    private String id;
    
    /**
     * Channel ID reference.
     */
    @NotBlank(message = "Channel ID is required")
    private String channelId;
    
    /**
     * Channel name.
     */
    @NotBlank(message = "Channel name is required")
    private String channelName;
    
    /**
     * Channel type.
     */
    @NotNull(message = "Channel type is required")
    @ChannelType
    private String channelType;
    
    /**
     * Catalog configuration.
     */
    @Valid
    @NotNull(message = "Channel configuration is required")
    private CatalogConfiguration configuration;
    
    /**
     * Category mappings.
     */
    @Valid
    private List<CategoryMapping> categoryMappings;
    
    /**
     * Attribute mappings.
     */
    @Valid
    private List<AttributeMapping> attributeMappings;
    
    /**
     * Validation rules.
     */
    @Valid
    private ValidationRules validationRules;
    
    /**
     * Transformation rules.
     */
    @Valid
    private TransformationRules transformationRules;
    
    /**
     * Product types supported by this channel.
     */
    @NotNull
    private List<String> supportedProductTypes;
    
    /**
     * Channel-specific attributes.
     */
    private Map<String, Object> channelAttributes;
    
    /**
     * Default currency for this channel.
     */
    @NotBlank(message = "Default currency is required")
    private String defaultCurrency;
    
    /**
     * Price multiplier for this channel.
     */
    private BigDecimal priceMultiplier;
    
    /**
     * Prefix to add to product descriptions.
     */
    private String descriptionPrefix;
    
    /**
     * Image requirements for this channel.
     */
    @Valid
    private ImageRequirements imageRequirements;
    
    /**
     * Content requirements for this channel.
     */
    @Valid
    private ContentRequirements contentRequirements;
    
    /**
     * Pricing rules for this channel.
     */
    @Valid
    private PricingRules pricingRules;
    
    /**
     * Inventory rules for this channel.
     */
    @Valid
    private InventoryRules inventoryRules;
    
    /**
     * Fulfillment restrictions for this channel.
     */
    private Map<String, Object> fulfillmentRestrictions;
}
