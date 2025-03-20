package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorChannel {
    private String channelId;
    
    private String channelName;
    
    private String channelType; // MARKETPLACE, QUICK_COMMERCE, POS
    
    private LocalDateTime activationDate;
    
    private Boolean isActive;
    
    private ChannelConfiguration configuration;
    
    private List<String> enabledCategories;
    
    private List<String> serviceableRegions;
    
    private Map<String, Object> channelAttributes;
}
