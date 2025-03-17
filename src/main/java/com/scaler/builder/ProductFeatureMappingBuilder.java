package com.scaler.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scaler.entity.Merchant;
import com.scaler.entity.Product;
import com.scaler.entity.ProductFeature;
import com.scaler.entity.ProductFeatureMapping;

import java.time.LocalDateTime;

public class ProductFeatureMappingBuilder {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String SYSTEM_USER = "system";

    /**
     * Creates a ProductFeatureMapping with the necessary fields including merchant from the product
     * 
     * @param product The product to associate with the feature
     * @param feature The feature to map to the product
     * @return A new ProductFeatureMapping entity
     * @throws JsonProcessingException If there's an error processing JSON metadata
     */
    public static ProductFeatureMapping createFeatureMapping(Product product, ProductFeature feature) throws JsonProcessingException {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        
        if (feature == null) {
            throw new IllegalArgumentException("Feature cannot be null");
        }
        
        // Get merchant from product to ensure it's set properly
        Merchant merchant = product.getMerchant();
        
        // If product doesn't have a merchant, try to get it from the product's category
        if (merchant == null && product.getProductCategories() != null && !product.getProductCategories().isEmpty()) {
            merchant = product.getProductCategories().iterator().next().getCategory().getMerchant();
        }
        
        if (merchant == null) {
            throw new IllegalArgumentException("Product must have a merchant or be associated with a category that has a merchant");
        }
        
        ObjectNode metadata = objectMapper.createObjectNode()
                .put("mappedAt", System.currentTimeMillis())
                .put("mappedBy", SYSTEM_USER);

        return ProductFeatureMapping.builder()
                .product(product)
                .feature(feature)
                .merchant(merchant)  // Set the merchant from the product
                .displayOrder(1)
                .visible(true)
                .enabled(true)
                .metadata(metadata)
                .createdBy(SYSTEM_USER)
                .lastModifiedBy(SYSTEM_USER)
                .createdDate(LocalDateTime.now())
                .build();
    }
}
