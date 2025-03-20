package com.scaler.rules.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "seller_product_rules")
public class SellerProductRule {
    
    @Id
    private String id;
    
    private String sellerId;
    private String productId;
    
    private String ruleType;   // e.g., "INCLUSION", "EXCLUSION", "PRICING", "INVENTORY"
    private String ruleCondition; // e.g., JSON or expression representing the condition
    private Integer priority;
    private Boolean active;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}