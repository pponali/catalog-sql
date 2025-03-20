package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Represents a product from the catalog service.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    /**
     * Unique identifier for the product.
     */
    private String id;
    
    /**
     * The product name.
     */
    private String name;
    
    /**
     * Product description.
     */
    private String description;
    
    /**
     * The category of the product.
     */
    private String category;
    
    /**
     * Reference to the vendor that supplies this product.
     */
    private String vendorId;
    
    /**
     * Base price of the product.
     */
    private BigDecimal basePrice;
    
    /**
     * Currency of the base price.
     */
    private String baseCurrency;
    
    /**
     * Product inventory information.
     */
    private ProductInventory inventory;
    
    /**
     * Media associated with the product (images, videos, etc).
     */
    private List<ProductMedia> media;
    
    /**
     * Additional product attributes as key-value pairs.
     */
    private Map<String, Object> attributes;
    
    /**
     * Quality score and ranking information.
     */
    private QualityScore qualityScore;
    
    /**
     * Represents product inventory information.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductInventory {
        
        /**
         * Available quantity.
         */
        private Integer quantity;
        
        /**
         * Whether the product is in stock.
         */
        private boolean inStock;
        
        /**
         * Threshold for low stock warning.
         */
        private Integer lowStockThreshold;
        
        /**
         * Builder for ProductInventory.
         */
        public static ProductInventoryBuilder builder() {
            return new ProductInventoryBuilder();
        }
        
        /**
         * Builder class for ProductInventory.
         */
        public static class ProductInventoryBuilder {
            private Integer quantity;
            private boolean inStock;
            private Integer lowStockThreshold;
            
            ProductInventoryBuilder() {
            }
            
            public ProductInventoryBuilder quantity(Integer quantity) {
                this.quantity = quantity;
                return this;
            }
            
            public ProductInventoryBuilder inStock(boolean inStock) {
                this.inStock = inStock;
                return this;
            }
            
            public ProductInventoryBuilder lowStockThreshold(Integer lowStockThreshold) {
                this.lowStockThreshold = lowStockThreshold;
                return this;
            }
            
            public ProductInventory build() {
                return new ProductInventory(quantity, inStock, lowStockThreshold);
            }
        }
    }
}