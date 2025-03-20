package com.scaler.vendor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sellers")
public class Seller {
    @Id
    private String id;
    private String name;
    private String code;
    private String description;
    private String status;
    private String merchantId;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Builder.Default
    private LocalDateTime createdDate = LocalDateTime.now();
    
    @Builder.Default
    private LocalDateTime lastModifiedDate = LocalDateTime.now();
}