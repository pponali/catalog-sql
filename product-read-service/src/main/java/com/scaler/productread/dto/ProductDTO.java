package com.scaler.productread.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * DTO representing a product from the catalog service.
 * This matches the structure of ProductDTO in the catalog-service.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {
    private UUID id;
    private String name;
    private String description;
    private String sku;
    private String brand;
    private Set<UUID> categories;
    private List<Map<String, Object>> categoryDetails;
    private List<Map<String, Object>> features;
    private List<Map<String, Object>> sellers;
    private List<Map<String, Object>> channels;
    private Map<String, Object> attributes;
    private List<Map<String, Object>> images;
    private boolean active;
    private Double minPrice;
    private Double maxPrice;
    private Integer totalStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}