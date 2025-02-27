package com.scaler.builder;

import com.scaler.entity.ProductFeatureMapping;
import com.scaler.entity.ProductFeatureValue;
import com.scaler.entity.ProductFeatureValueMapping;

public class ProductFeatureValueMappingBuilder {
    
    public static ProductFeatureValueMapping createMapping(ProductFeatureMapping featureMapping, ProductFeatureValue featureValue, Integer displayOrder, Boolean isPrimary) {
        return ProductFeatureValueMapping.builder()
                .featureMapping(featureMapping)
                .featureValue(featureValue)
                .createdBy("SYSTEM")
                .displayOrder(displayOrder)
                .isPrimary(isPrimary)
                .isActive(true)
                .build();
    }
}
