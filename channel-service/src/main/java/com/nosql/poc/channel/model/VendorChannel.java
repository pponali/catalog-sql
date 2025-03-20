package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Represents a vendor's channel configuration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorChannel {
    
    /**
     * Unique identifier.
     */
    private String id;
    
    /**
     * Reference to the vendor.
     */
    private String vendorId;
    
    /**
     * Reference to the channel.
     */
    private String channelId;
    
    /**
     * Status of the vendor in this channel.
     */
    private String status;
    
    /**
     * Commission rate for this vendor in this channel.
     */
    private BigDecimal channelCommissionRate;
    
    /**
     * Date when the vendor was activated in this channel.
     */
    private LocalDateTime activatedAt;
    
    /**
     * Date when the vendor was last updated in this channel.
     */
    private LocalDateTime updatedAt;
    
    /**
     * Channel-specific vendor configuration.
     */
    private Map<String, Object> channelConfiguration;
    
    /**
     * Whether the vendor is active in this channel.
     */
    private boolean active;
    
    /**
     * Notes or comments about this vendor-channel relationship.
     */
    private String notes;
}