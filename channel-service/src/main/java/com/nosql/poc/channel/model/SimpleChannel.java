package com.nosql.poc.channel.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Simplified channel model for CSV imports
 */
@Data
@Document(collection = "channels")
public class SimpleChannel {
    @Id
    private String id;
    private String channelId;
    private String name;
    private String type; // WEB, MOBILE_APP, MARKETPLACE, PHYSICAL_STORE
    private String description;
    private String integrationEndpoint;
    private Boolean active;
    private String region;
    private String apiKey;
    private String apiSecret;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Getter and setter methods are provided by Lombok @Data
}