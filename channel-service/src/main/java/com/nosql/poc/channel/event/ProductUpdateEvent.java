package com.nosql.poc.channel.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateEvent {
    private String productId;
    private String channelId;
    private String updateType; // CREATE, UPDATE, DELETE
    private String userId;
    private long timestamp;
}
