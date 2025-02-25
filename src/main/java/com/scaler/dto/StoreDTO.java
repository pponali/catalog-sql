package com.scaler.dto;

import com.scaler.entity.StoreType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class StoreDTO extends BaseDTO {
    
    @NotNull(message = "Merchant ID is required")
    private UUID merchantId;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Domain is required")
    private String domain;
    
    @NotBlank(message = "Locale is required")
    @Pattern(regexp = "^[a-z]{2}-[A-Z]{2}$", message = "Locale must be in format: xx-XX")
    private String locale;
    
    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter ISO code")
    private String currency;
    
    private String description;
    private boolean active;
    
    @NotBlank(message = "Timezone is required")
    private String timezone;
    
    @NotNull(message = "Store type is required")
    private StoreType storeType;
    
    private String status;

    private Map<String, Object> metadata;  // Using Object to handle dynamic JSON structure
}
