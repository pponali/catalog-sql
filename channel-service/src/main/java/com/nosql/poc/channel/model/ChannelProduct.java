package com.nosql.poc.channel.model;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ChannelProduct {
    private String productId;
    private String channelId;
    private String name;
    private String sku;
    private BigDecimal channelPrice;
    private String channelCurrency;
    private String channelDescription;
    private List<ProductMedia> channelMedia;
    private String availabilityStatus; // IN_STOCK, OUT_OF_STOCK, BACK_ORDER, PREORDER
    private FulfillmentRules fulfillmentRules;
    private ChannelInventory channelInventory;
    private Integer displayPriority;
    private Map<String, Object> channelAttributes;
    private ValidationStatus channelValidation;
    private Boolean active;
    private VendorInfo vendorInfo;
}
