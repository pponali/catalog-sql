package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Represents a vendor from the vendor service.
 * Combines fields from prior Vendor and VendorInfo classes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {
    
    /**
     * Unique identifier for the vendor.
     */
    private String id;
    
    /**
     * Vendor name.
     */
    private String name;
    
    /**
     * Vendor code.
     */
    private String code;
    
    /**
     * Vendor type.
     */
    private String vendorType;
    
    /**
     * Whether the vendor is currently active.
     */
    private boolean active;
    
    /**
     * Commission rate for this vendor.
     */
    private BigDecimal commissionRate;
    
    /**
     * Rating score (1-5).
     */
    private Double rating;
    
    /**
     * Number of reviews.
     */
    private Integer reviewCount;
    
    /**
     * Delivery capabilities of the vendor.
     */
    private DeliveryCapabilities deliveryCapabilities;
    
    /**
     * Geographic areas where the vendor can deliver.
     */
    private List<String> serviceableAreas;
    
    /**
     * Additional attributes as key-value pairs.
     */
    private Map<String, Object> attributes;
    
    /**
     * Getter for the vendor ID.
     * @return the vendor ID
     */
    public String getVendorId() {
        return id;
    }
}