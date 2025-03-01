package com.scaler.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.entity.*;

public class ProductFeatureValueMappingBuilder {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static ProductFeatureValueMapping createMapping(Product product, ProductFeature feature, 
            ProductFeatureValue featureValue, CategoryFeatureTemplate template, Integer displayOrder) {
        return ProductFeatureValueMapping.builder()
                .product(product)
                .feature(feature)
                .featureValue(featureValue)
                .template(template)
                .displayOrder(displayOrder)
                .visible(true)
                .enabled(true)
                .isPrimary(false)
                .isActive(true)
                .metadata(objectMapper.createObjectNode())
                .createdBy("SYSTEM")
                .build();
    }

    public static ProductFeatureValueMapping createMappingSomeOther(Product product, ProductFeature feature,
            ProductFeatureValue featureValue, CategoryFeatureTemplate template, Integer displayOrder,
            Boolean visible, Boolean enabled, Boolean isPrimary, JsonNode metadata) {
        return ProductFeatureValueMapping.builder()
                .product(product)
                .feature(feature)
                .featureValue(featureValue)
                .template(template)
                .displayOrder(displayOrder)
                .visible(visible)
                .enabled(enabled)
                .isPrimary(isPrimary)
                .isActive(true)
                .metadata(metadata)
                .createdBy("SYSTEM")
                .build();
    }


}
