package com.nosql.poc.channel.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class DeliveryCapabilities {
    private boolean sameDay;
    private boolean nextDay;
    private boolean standardDelivery;
    private List<String> supportedDeliveryModes;
    private Map<String, Object> deliveryConstraints;
    private Map<String, Object> specialHandlingCapabilities;
}
