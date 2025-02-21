package com.nosql.poc.channel.model;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class VendorInfo {
    private String vendorId;
    private String vendorName;
    private String vendorType;
    private Double rating;
    private Integer reviewCount;
    private Map<String, Object> vendorAttributes;
    private BigDecimal commissionRate;
    private Boolean active;
}
