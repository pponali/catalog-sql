package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * A simplified Channel model for client interactions with the channel service.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
    private String id;
    private String name;
    private String code;
    private String type;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Map<String, Object> attributes;
}