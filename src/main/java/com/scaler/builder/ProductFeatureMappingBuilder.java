package com.scaler.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scaler.entity.Product;
import com.scaler.entity.ProductFeature;
import com.scaler.entity.ProductFeatureMapping;

public class ProductFeatureMappingBuilder {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String SYSTEM_USER = "system";

    public static ProductFeatureMapping createFeatureMapping(Product product, ProductFeature feature) {
        ObjectNode metadata = objectMapper.createObjectNode()
                .put("mappedAt", System.currentTimeMillis())
                .put("mappedBy", SYSTEM_USER);

        return ProductFeatureMapping.builder()
                .product(product)
                .feature(feature)
                .displayOrder(1)
                .visible(true)
                .enabled(true)
                .metadata(metadata)
                .createdBy(SYSTEM_USER)
                .lastModifiedBy(SYSTEM_USER)
                .build();
    }
}
