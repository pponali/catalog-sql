package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Configuration for a catalog in the channel system.
 * Contains all the necessary settings to integrate with external systems.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogConfiguration {
    @NotBlank(message = "Catalog version is required")
    private String catalogVersion;
    
    @NotBlank(message = "Integration mode is required")
    private String integrationMode; // API, FEED, MANUAL
    
    @Valid
    private ApiConfiguration apiConfig;
    
    @Valid
    private FeedConfiguration feedConfig;
    
    private List<String> supportedFeatures;
    
    @Valid
    @NotNull(message = "Content requirements are required")
    private ContentRequirements contentRequirements;
    
    @Valid
    @NotNull(message = "Image requirements are required")
    private ImageRequirements imageRequirements;
    
    @Valid
    @NotNull(message = "Pricing rules are required")
    private PricingRules pricingRules;
    
    @Valid
    @NotNull(message = "Inventory rules are required")
    private InventoryRules inventoryRules;
    
    private Map<String, Object> additionalSettings;
}
