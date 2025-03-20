package com.nosql.poc.vendor.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSyncEvent {
    private String productId;
    private String vendorId;
    private String channelId;
    private String eventType;
    private LocalDateTime timestamp;
    private String userId;
}