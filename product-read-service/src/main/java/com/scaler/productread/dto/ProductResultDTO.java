package com.scaler.productread.dto;

import com.scaler.productread.document.ProductDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for paginated product results with facets.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResultDTO {
    private List<ProductDocument> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private List<FacetDTO> facets;
    
    /**
     * Facet information for filtered searches.
     */
    @Data
    @lombok.Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacetDTO {
        private String name;
        private List<FacetValueDTO> values;
    }
    
    /**
     * Facet value with count.
     */
    @Data
    @lombok.Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FacetValueDTO {
        private String value;
        private long count;
    }
}