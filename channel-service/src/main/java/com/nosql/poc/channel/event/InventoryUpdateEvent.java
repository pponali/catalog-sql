package com.nosql.poc.channel.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryUpdateEvent {
    private String productId;
    private String channelId;
    private int quantity;
    private String locationId;
    private String userId;
    private long timestamp;
}
