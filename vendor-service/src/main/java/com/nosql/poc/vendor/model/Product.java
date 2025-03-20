package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

/**
 * A simplified Product model for client interactions with the catalog service.
 * This is a representation of the product data that comes from the catalog service.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String id;
    private String sku;
    private String name;
    private String description;
    private String brandName;
    private String categoryId;
    private String categoryName;
    private BigDecimal basePrice;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> images;
    private Map<String, Object> attributes;
    private List<String> tags;
}