package com.scaler.productread.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for sorting product results.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSortDTO {
    
    /**
     * Fields that can be used for sorting.
     */
    public enum ProductSortField {
        NAME,
        PRICE,
        CREATED_AT,
        UPDATED_AT
    }
    
    /**
     * Sort direction.
     */
    public enum SortDirection {
        ASC,
        DESC
    }
    
    private ProductSortField field;
    private SortDirection direction;
}