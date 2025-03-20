package com.scaler.productread.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.scaler.productread.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a product event published by the catalog service.
 * This event contains information about changes to a product.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductEvent {
    
    /**
     * Type of product event.
     */
    public enum EventType {
        PRODUCT_CREATED,
        PRODUCT_UPDATED,
        PRODUCT_DELETED
    }
    
    /**
     * Unique identifier for the event.
     */
    private UUID eventId;
    
    /**
     * ID of the product affected by this event.
     */
    private UUID productId;
    
    /**
     * Type of event (created, updated, deleted).
     */
    private EventType eventType;
    
    /**
     * Timestamp when the event occurred.
     */
    private LocalDateTime timestamp;
    
    /**
     * The complete product data (for created and updated events).
     */
    private ProductDTO productData;
    
    /**
     * Additional metadata related to the event.
     */
    private Map<String, Object> metadata;
    
    /**
     * Optional user ID of the user who triggered the event.
     */
    private String userId;
}