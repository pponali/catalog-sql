package com.scaler.dto;

import com.scaler.entity.enums.PlatformType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * DTO for Platform entities from the Channel Service
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PlatformDTO extends BaseDTO {
    
    /**
     * Name of the platform
     */
    private String name;
    
    /**
     * Unique code for the platform
     */
    private String code;
    
    /**
     * Description of the platform
     */
    private String description;
    
    /**
     * Type of platform (e.g., "web", "mobile", "pos", etc.)
     */
    private String platformType;
    
    /**
     * Type as an enum for internal use
     */
    private PlatformType type;
    
    /**
     * Status of the platform (e.g., "active", "inactive")
     */
    private String status;
    
    /**
     * Whether the platform is enabled
     */
    private Boolean enabled;
    
    /**
     * Platform-specific configuration
     */
    private String platformConfig;
}