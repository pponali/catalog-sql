package com.scaler.dto;

import com.scaler.entity.StoreType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.UUID;

/**
 * DTO for Store entities
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class StoreDTO extends BaseDTO {
    
    /**
     * The merchant ID that this store belongs to
     */
    @NotNull(message = "Merchant ID is required")
    private UUID merchantId;
    
    /**
     * Name of the store
     */
    @NotBlank(message = "Name is required")
    private String name;
    
    /**
     * Unique code for the store
     */
    private String code;
    
    /**
     * Domain name for the store
     */
    @NotBlank(message = "Domain is required")
    private String domain;
    
    /**
     * Locale for the store (e.g., "en-US")
     */
    @NotBlank(message = "Locale is required")
    @Pattern(regexp = "^[a-z]{2}-[A-Z]{2}$", message = "Locale must be in format: xx-XX")
    private String locale;
    
    /**
     * Currency code for the store (e.g., "USD")
     */
    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter ISO code")
    private String currency;
    
    /**
     * Description of the store
     */
    private String description;
    
    /**
     * Whether the store is active
     */
    private boolean active;
    
    /**
     * Timezone for the store
     */
    @NotBlank(message = "Timezone is required")
    private String timezone;
    
    /**
     * Type of store
     */
    @NotNull(message = "Store type is required")
    private StoreType storeType;
    
    /**
     * Type of store as string (used for gRPC communication)
     */
    private String storeTypeString;
    
    /**
     * Status of the store (e.g., "active", "inactive", "maintenance")
     */
    private String status;
    
    /**
     * The channel ID that this store belongs to
     */
    private UUID channelId;
    
    /**
     * Physical address of the store
     */
    private String address;
    
    /**
     * City where the store is located
     */
    private String city;
    
    /**
     * State/province where the store is located
     */
    private String state;
    
    /**
     * Country where the store is located
     */
    private String country;
    
    /**
     * Postal/ZIP code for the store
     */
    private String pincode;

    /**
     * Additional metadata for the store
     */
    private Map<String, Object> metadata;  // Using Object to handle dynamic JSON structure
}
