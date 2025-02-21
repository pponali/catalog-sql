package com.nosql.poc.channel.model;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
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

@Data
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

@Data
public class FeedConfiguration {
    private String feedType; // XML, CSV, JSON
    private String feedFormat;
    private String feedDeliveryMethod; // FTP, API, S3
    private String feedSchedule;
    private Map<String, String> feedCredentials;
    private List<String> requiredFields;
}

@Data
public class ContentRequirements {
    @NotNull(message = "Minimum title length is required")
    private Integer minTitleLength;
    
    @NotNull(message = "Maximum title length is required")
    private Integer maxTitleLength;
    
    @NotNull(message = "Minimum description length is required")
    private Integer minDescriptionLength;
    
    @NotNull(message = "Maximum description length is required")
    private Integer maxDescriptionLength;
    
    @NotNull(message = "Mandatory attributes list is required")
    private List<String> mandatoryAttributes;
    
    private List<String> optionalAttributes;
    private Map<String, String> formatRequirements;
}

@Data
public class ImageRequirements {
    @NotNull(message = "Supported formats are required")
    private List<String> supportedFormats;
    
    @NotNull(message = "Minimum width is required")
    private Integer minWidth;
    
    @NotNull(message = "Minimum height is required")
    private Integer minHeight;
    
    @NotNull(message = "Maximum file size is required")
    private Integer maxFileSize;
    
    @NotNull(message = "Minimum number of images is required")
    private Integer minImages;
    
    private Integer maxImages;
    private List<String> requiredAngles;
    
    @Valid
    private Map<String, ImageSpec> imageSpecs;
}

@Data
public class ImageSpec {
    private String type; // PRIMARY, SECONDARY, THUMBNAIL
    private Integer width;
    private Integer height;
    private String format;
    private Integer quality;
    private Boolean required;
}

@Data
public class PricingRules {
    private String pricingModel;
    private String currency;
    private Double minPrice;
    private Double maxPrice;
    private Integer decimalPlaces;
    private List<String> allowedPricePoints;
    private Map<String, Object> priceCalculationRules;
}

@Data
public class InventoryRules {
    private String inventoryModel;
    private Boolean allowBackorder;
    private Integer minInventoryLevel;
    private Integer maxInventoryLevel;
    private Integer bufferQuantity;
    private String updateFrequency;
    private Map<String, Object> inventoryCalculationRules;
}

@Data
public class RetryConfig {
    private Integer maxRetries;
    private Integer retryInterval;
    private List<String> retryableErrors;
    private Map<String, Object> retryStrategy;
}
