package com.scaler.rules.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "business_rules")
public class BusinessRule {
    
    @Id
    private String id;
    
    private String name;
    private String description;
    private String ruleType;  // e.g., "CATEGORY", "PRODUCT", "PRICING", "PROMOTION"
    private String domain;    // The domain this rule applies to: "PRODUCT", "SELLER", "CHANNEL"
    
    private String ruleContent; // The rule content in Drools DRL format or JSON
    private Integer priority;
    private Boolean active;
    
    private List<String> categories; // Categories this rule applies to
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}