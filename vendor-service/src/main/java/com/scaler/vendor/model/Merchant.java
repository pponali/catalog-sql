package com.scaler.vendor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "merchants")
public class Merchant {
    @Id
    private String id;
    private String name;
    private String code;
    private String description;
    private String status;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();
    
    @Builder.Default
    private LocalDateTime lastModifiedDate = LocalDateTime.now();
}