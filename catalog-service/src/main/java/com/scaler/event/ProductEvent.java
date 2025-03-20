package com.scaler.event;

import com.scaler.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEvent {
    
    public enum EventType {
        PRODUCT_CREATED,
        PRODUCT_UPDATED,
        PRODUCT_DELETED
    }
    
    private UUID eventId;
    private EventType eventType;
    private LocalDateTime timestamp;
    private UUID productId;
    private ProductDTO productData; // Full product data for created/updated events
}