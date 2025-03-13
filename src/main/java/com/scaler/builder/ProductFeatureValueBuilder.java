package com.scaler.builder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scaler.entity.ProductFeature;
import com.scaler.entity.ProductFeatureMapping;
import com.scaler.entity.ProductFeatureValue;
import com.scaler.entity.ProductFeature;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class ProductFeatureValueBuilder {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static JsonNode createSimpleValueJson(String featureCode, String value) {
        try {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("value", value);
            node.put("featureCode", featureCode);
            node.put("timestamp", LocalDateTime.now().toString());
            return node;
        } catch (Exception e) {
            throw new RuntimeException("Error creating JSON for feature value: " + value, e);
        }
    }

    private static JsonNode createAttributeValues(String value, String displayValue, ObjectNode additionalInfo) {
        try {
            ObjectNode attributeValues = objectMapper.createObjectNode()
                    .put("value", value)
                    .put("displayValue", displayValue);
            if (additionalInfo != null) {
                attributeValues.set("additionalInfo", additionalInfo);
            }
            return attributeValues;
        } catch (Exception e) {
            log.error("Error creating attribute values: {}", e.getMessage());
            return null;
        }
    }

    public static ProductFeatureValue createProcessorValue(ProductFeature feature, String value) {
        ObjectNode additionalInfo = objectMapper.createObjectNode();
        if (value.contains("M2")) {
            additionalInfo.put("cores", "12-core")
                    .put("architecture", "ARM")
                    .put("generation", "2nd Gen");
        } else if (value.contains("i9")) {
            additionalInfo.put("cores", "14-core")
                    .put("architecture", "x86")
                    .put("generation", "13th Gen");
        }

        return ProductFeatureValue.builder()
                .feature(feature)
                .type("SPECIFICATION")
                .attributeValues(createAttributeValues(value, value, additionalInfo))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static ProductFeatureValue createIphoneFeature(ProductFeature feature, String value){
        ObjectNode additionalInfo = objectMapper.createObjectNode()
                .put("type", value.contains("Unified") ? "Unified Memory" : "DDR5")
                .put("speed", "6400MHz");

        return ProductFeatureValue.builder()
                .feature(feature)
                .type("SPECIFICATION")
                .attributeValues(createAttributeValues(value.replace("GB", ""), value, additionalInfo))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static ProductFeatureValue createRamValue(ProductFeature feature, String value) {
        ObjectNode additionalInfo = objectMapper.createObjectNode()
                .put("type", value.contains("Unified") ? "Unified Memory" : "DDR5")
                .put("speed", "6400MHz");

        return ProductFeatureValue.builder()
                .feature(feature)
                .type("SPECIFICATION")
                .attributeValues(createAttributeValues(value.replace("GB", ""), value, additionalInfo))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static ProductFeatureValue createStorageValue(ProductFeature feature, String value) {
        ObjectNode additionalInfo = objectMapper.createObjectNode()
                .put("type", "SSD")
                .put("technology", "NVMe")
                .put("readSpeed", "7000 MB/s")
                .put("writeSpeed", "5000 MB/s");

        return ProductFeatureValue.builder()
                .feature(feature)
                .type("SPECIFICATION")
                .attributeValues(createAttributeValues(value.replace("TB", ""), value, additionalInfo))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static ProductFeatureValue createFeatureValue(ProductFeature feature, String value) {
        ObjectNode additionalInfo = objectMapper.createObjectNode();
        if (feature.getCode().equals("GOLD-PURITY")) {
            additionalInfo.put("purityPercentage", value.replace("K", "").equals("24") ? "99.9" : "91.6");
        } else if (feature.getCode().equals("GOLD-WEIGHT")) {
            additionalInfo.put("unit", "grams");
        }

        return ProductFeatureValue.builder()
                .feature(feature)
                .type("SPECIFICATION")
                .attributeValues(createAttributeValues(value, value, additionalInfo))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static ProductFeatureValue createPurityValue(ProductFeature goldPurity, String invalidPurity) {
        return ProductFeatureValue.builder()
                .type("VALUE")
                .feature(goldPurity)
                .attributeValues(createSimpleValueJson("value", invalidPurity))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }
}
