package com.scaler.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scaler.entity.UnitOfMeasure;

public class UnitOfMeasureBuilder {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static UnitOfMeasure createGBUnit() {
        ObjectNode metadata = objectMapper.createObjectNode()
                .put("category", "digital")
                .put("system", "binary")
                .put("bytes", 1073741824);

        return UnitOfMeasure.builder()
                .code("GB")
                .name("Gigabyte")
                .description("Unit for digital storage capacity")
                .type("DIGITAL")
                .active(true)
                .metadata(metadata)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static UnitOfMeasure createTBUnit() {
        ObjectNode metadata = objectMapper.createObjectNode()
                .put("category", "digital")
                .put("system", "binary")
                .put("bytes", 1099511627776L);

        return UnitOfMeasure.builder()
                .code("TB")
                .name("Terabyte")
                .description("Unit for large digital storage capacity")
                .type("DIGITAL")
                .active(true)
                .metadata(metadata)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static UnitOfMeasure createKGUnit() {
        ObjectNode metadata = objectMapper.createObjectNode()
                .put("category", "weight")
                .put("system", "metric")
                .put("base", true);

        return UnitOfMeasure.builder()
                .code("KG")
                .name("Kilogram")
                .description("Unit for weight measurement")
                .type("WEIGHT")
                .active(true)
                .metadata(metadata)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static UnitOfMeasure createPiecesUnit() {
        ObjectNode metadata = objectMapper.createObjectNode()
                .put("category", "count")
                .put("system", "discrete")
                .put("base", true);

        return UnitOfMeasure.builder()
                .code("PCS")
                .name("Pieces")
                .description("Count of individual items")
                .type("COUNT")
                .active(true)
                .metadata(metadata)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static UnitOfMeasure createGramUnit() {
        ObjectNode metadata = objectMapper.createObjectNode()
                .put("category", "weight")
                .put("system", "metric")
                .put("base", false)
                .put("conversionToBase", 0.001);

        return UnitOfMeasure.builder()
                .code("G")
                .name("Grams")
                .description("Weight in grams")
                .type("WEIGHT")
                .active(true)
                .metadata(metadata)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }
}
