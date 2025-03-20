package com.scaler.dto;

import com.scaler.entity.enums.ChannelType;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * DTO for Channel entities
 */
@Getter
@Setter
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelDTO extends BaseDTO {
    /**
     * Unique code for the channel
     */
    private String code;
    
    /**
     * Name of the channel
     */
    private String name;
    
    /**
     * Description of the channel
     */
    private String description;
    
    /**
     * Status of the channel (e.g., "active", "inactive")
     */
    private String status;
    
    /**
     * Type of channel
     */
    private ChannelType type;
    
    /**
     * Type of channel as string (used for gRPC communication)
     */
    private String channelType;
    
    /**
     * Metadata in JSON format
     */
    private String metadata;
    
    /**
     * Whether the channel is enabled
     */
    private Boolean enabled;
    
    /**
     * Display order for UI presentation
     */
    private Integer displayOrder;
    
    /**
     * Whether the channel is visible
     */
    private Boolean visibility;
    
    /**
     * Channel-specific configuration
     */
    private String channelConfig;
}
