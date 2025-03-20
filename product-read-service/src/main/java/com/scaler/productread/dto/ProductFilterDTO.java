package com.scaler.productread.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for filtering products.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterDTO {
    private Set<String> categoryIds;
    private Set<String> brands;
    private PriceRangeDTO priceRange;
    private Set<UUID> sellerIds;
    private Set<UUID> channelIds;
    private List<FeatureFilterDTO> features;
    private Boolean active;
    private Boolean inStock;
    private String search;
    
    /**
     * Price range filter criteria.
     */
    @Data
    @lombok.Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceRangeDTO {
        private Double min;
        private Double max;
    }
    
    /**
     * Feature filter criteria.
     */
    @Data
    @lombok.Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeatureFilterDTO {
        private String code;
        private String value;
    }
}