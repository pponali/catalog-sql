package com.nosql.poc.vendor.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelUpdateEvent {
    private String channelId;
    private String vendorId;
    private String productId;
    private String eventType = "CHANNEL_UPDATE";
    private LocalDateTime timestamp = LocalDateTime.now();
    private String userId = "system";
    
    public ChannelUpdateEvent(String channelId, String vendorId, String productId) {
        this.channelId = channelId;
        this.vendorId = vendorId;
        this.productId = productId;
        this.timestamp = LocalDateTime.now();
    }
}