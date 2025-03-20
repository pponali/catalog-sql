package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCapabilities {
    private List<String> deliveryModes;
    private Double maxDeliveryRadius;
    private List<String> serviceablePincodes;
    private Integer maxOrdersPerDay;
    private Map<String, Object> deliveryConstraints;
}