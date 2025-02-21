package com.nosql.poc.channel.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class FulfillmentRules {
    private List<String> serviceableAreas;
    private DeliveryCapabilities deliveryCapabilities;
    private Map<String, Object> channelRestrictions;
    private List<String> fulfillmentModes; // DELIVERY, PICKUP, SHIP_TO_HOME
    private Map<String, Object> deliveryTimeEstimates;
    private Map<String, Object> fulfillmentCosts;
}
