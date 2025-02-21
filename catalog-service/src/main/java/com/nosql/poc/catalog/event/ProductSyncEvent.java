package com.nosql.poc.catalog.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSyncEvent {
    private String productId;
    private String channelId;
    private String vendorId;
    private String syncType; // FULL, PARTIAL, PRICE_ONLY, INVENTORY_ONLY
    private String userId;
    private long timestamp;
}
