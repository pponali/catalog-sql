package com.nosql.poc.vendor.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateEvent {
    private String productId;
    private String vendorId;
    private String eventType = "PRODUCT_UPDATE";
    private LocalDateTime timestamp = LocalDateTime.now();
    private String userId = "system";
    
    public ProductUpdateEvent(String productId, String vendorId) {
        this.productId = productId;
        this.vendorId = vendorId;
        this.timestamp = LocalDateTime.now();
    }
}