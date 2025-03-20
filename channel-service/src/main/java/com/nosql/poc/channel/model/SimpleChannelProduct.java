package com.nosql.poc.channel.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Simplified channel product model for CSV imports
 */
@Data
@Document(collection = "channel_products")
public class SimpleChannelProduct {
    @Id
    private String id;
    private String productId;
    private String channelId;
    private String channelProductId; // Product ID specific to the channel
    private String status; // ACTIVE, INACTIVE, PENDING_APPROVAL
    private BigDecimal channelPrice;
    private Integer channelInventory;
    private String categoryMapping; // Channel-specific category
    private String fulfillmentType; // Channel-specific fulfillment
    private LocalDateTime listedDate;
    private LocalDateTime lastUpdated;
    private LocalDateTime lastSyncAttempt;
    private Boolean syncSuccess;
    private String syncMessage;
    private String channelUrl; // URL to product on channel

    // Getter and setter methods are provided by Lombok @Data
}