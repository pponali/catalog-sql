package com.scaler.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scaler.entity.Product;
import com.scaler.entity.ProductFeature;
import com.scaler.entity.ProductFeatureValue;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class ProductFeatureValueBuilder {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();

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

    public static ProductFeatureValue createProcessorValue(Product product,ProductFeature feature, String value) {
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
                .product(product)
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

    public static ProductFeatureValue createRamValue(Product product, ProductFeature feature, String value) {
        ObjectNode additionalInfo = objectMapper.createObjectNode()
                .put("type", value.contains("Unified") ? "Unified Memory" : "DDR5")
                .put("speed", "6400MHz");

        return ProductFeatureValue.builder()
                .feature(feature)
                .product(product)
                .type("SPECIFICATION")
                .attributeValues(createAttributeValues(value.replace("GB", ""), value, additionalInfo))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static ProductFeatureValue createStorageValue(Product product, ProductFeature feature, String value) {
        ObjectNode additionalInfo = objectMapper.createObjectNode()
                .put("type", "SSD")
                .put("technology", "NVMe")
                .put("readSpeed", "7000 MB/s")
                .put("writeSpeed", "5000 MB/s");

        return ProductFeatureValue.builder()
                .feature(feature)
                .type("SPECIFICATION")
                .product(product)
                .attributeValues(createAttributeValues(value.replace("TB", ""), value, additionalInfo))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static ProductFeatureValue createFeatureValue(Product product, ProductFeature feature, String value) {
        return ProductFeatureValue.builder()
                .feature(feature)
                .type("SPECIFICATION")
                .product(product)
                .attributeValues(createSimpleValueJson(feature.getCode(), value))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

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

    public static ProductFeatureValue createProcessorFeatureValue(ProductFeature productFeature) throws JsonProcessingException {
        return ProductFeatureValue.builder()
                .type("SPECIFICATION")
                .feature(productFeature)
                .attributeValues(objectMapper.readTree("""
                    {
                        "processor": {
                            "brand": "Intel",
                            "model": "i9-13900H",
                            "cores": 14,
                            "threads": 20,
                            "baseSpeed": 2.6,
                            "turboSpeed": 5.4,
                            "cache": {
                                "l1": "1.75MB",
                                "l2": "24MB",
                                "l3": "36MB"
                            },
                            "features": {
                                "virtualization": true,
                                "hyperthreading": true,
                                "integratedGraphics": "Intel Iris Xe"
                            }
                        }
                    }
                    """))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static ProductFeatureValue createGoldSpecsFeatureValue(ProductFeature productFeature) throws JsonProcessingException {
        return ProductFeatureValue.builder()
                .type("SPECIFICATION")
                .feature(productFeature)
                .attributeValues(objectMapper.readTree("""
                    {
                        "gold": {
                            "purity": "22K",
                            "weight": {
                                "value": 50.5,
                                "unit": "grams"
                            },
                            "certification": {
                                "type": "BIS",
                                "hallmark": true,
                                "certNumber": "BIS123456",
                                "certificationDate": "2025-01-15"
                            },
                            "design": {
                                "style": "Traditional",
                                "collection": "Divyam",
                                "occasion": "Wedding",
                                "stoneWork": {
                                    "type": "Diamond",
                                    "count": 12,
                                    "totalCarats": 1.5,
                                    "clarity": "VS1",
                                    "color": "F"
                                },
                                "craftsmanship": {
                                    "technique": "Hand Crafted",
                                    "finish": "Matte",
                                    "detailing": "Filigree Work"
                                }
                            },
                            "pricing": {
                                "makingCharges": {
                                    "value": 12,
                                    "unit": "percentage"
                                },
                                "wastage": {
                                    "value": 8,
                                    "unit": "percentage"
                                }
                            }
                        }
                    }
                    """))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    private static JsonNode readTree(String value) {
        if (value == null || value.isEmpty() || value.equals("\"\"")) return null;
        try {
            if (value.startsWith("{") || value.startsWith("[")) {
                return objectMapper.readTree(value);
            } else {
                return createSimpleValueJson("value", value);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting String to JsonNode: " + value, e);
        }
    }

    public static ProductFeatureValue createNutritionFeatureValue(ProductFeature productFeature) throws JsonProcessingException {
        return ProductFeatureValue.builder()
                .type("SPECIFICATION")
                .feature(productFeature)
                .attributeValues(readTree("""
                    {
                        "nutrition": {
                            "servingSize": {
                                "value": 100,
                                "unit": "grams"
                            },
                            "calories": 50,
                            "macronutrients": {
                                "proteins": {
                                    "value": 2.5,
                                    "unit": "g"
                                },
                                "carbohydrates": {
                                    "value": 10.5,
                                    "unit": "g",
                                    "details": {
                                        "fiber": 2.1,
                                        "sugar": 5.2
                                    }
                                },
                                "fats": {
                                    "value": 0.5,
                                    "unit": "g",
                                    "types": {
                                        "saturated": 0.1,
                                        "unsaturated": 0.4,
                                        "trans": 0
                                    }
                                }
                            },
                            "vitamins": [
                                {
                                    "name": "Vitamin C",
                                    "value": 85,
                                    "unit": "mg",
                                    "dailyValue": 94
                                },
                                {
                                    "name": "Vitamin A",
                                    "value": 100,
                                    "unit": "IU",
                                    "dailyValue": 2
                                }
                            ]
                        }
                    }
                    """))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }
}
