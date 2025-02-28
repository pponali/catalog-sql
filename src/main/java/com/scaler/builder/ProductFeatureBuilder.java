package com.scaler.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scaler.entity.CategoryFeatureTemplate;
import com.scaler.entity.ProductFeature;
import com.scaler.entity.FeatureType;
import com.scaler.entity.UnitOfMeasure;
import lombok.extern.slf4j.Slf4j;

import static com.scaler.constants.FeatureConstants.*;

/**
 * Utility class for building product features.
 * Provides reusable methods to create common product features.
 */
@Slf4j
public class ProductFeatureBuilder {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    private ProductFeatureBuilder() {
        // Private constructor to prevent instantiation
    }


    /**
     * Creates metadata for a product feature
     * @param displayOrder Display order of the feature
     * @param group Feature group name
     * @param tooltip Tooltip text
     * @return JSON string containing metadata
     */
    private static JsonNode createMetadata(int displayOrder, String group, String tooltip) {
        try {
            ObjectNode metadata = objectMapper.createObjectNode()
                    .put("displayOrder", displayOrder)
                    .put("group", group)
                    .put("tooltip", tooltip);
            return metadata;
        } catch (Exception e) {
            log.error("Error creating metadata: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Creates a laptop processor product feature
     * @return ProductFeature for laptop processor
     */
    public static ProductFeature createLaptopProcessorFeature(CategoryFeatureTemplate categoryFeatureTemplate) {
        return ProductFeature.builder()
                .code(LAPTOP_PROCESSOR)
                .name("Processor")
                .description("Laptop Processor Specifications")
                .attributeType(SPECIFICATION)
                .template(categoryFeatureTemplate)
                .validationPattern("")
                .minValue("")
                .maxValue("")
                .allowedValues(PROCESSOR_VALUES)
                .defaultValue("")
                .featureType(FeatureType.ENUM)
                .visible(true)
                .comparable(true)
                .editable(true)
                .searchable(true)
                .required(true)
                .multiValued(false)
                .metadata(createMetadata(1, "Technical Specifications", "Processor model that powers the laptop"))
                .createdBy(SYSTEM_USER)
                .lastModifiedBy(SYSTEM_USER)
                .build();
    }

    /**
     * Creates a laptop RAM product feature
     * @return ProductFeature for laptop RAM
     */
    public static ProductFeature createLaptopRamFeature(CategoryFeatureTemplate categoryFeatureTemplate, UnitOfMeasure gbUnit) {
        return ProductFeature.builder()
                .code(LAPTOP_RAM)
                .name("RAM")
                .unitOfMeasure(gbUnit)
                .description("Laptop Memory (RAM) Specifications")
                .attributeType(SPECIFICATION)
                .validationPattern(RAM_PATTERN)
                .template(categoryFeatureTemplate)
                .minValue("4")
                .maxValue("128")
                .allowedValues(RAM_VALUES)
                .defaultValue(DEFAULT_RAM)
                .featureType(FeatureType.ENUM)
                .visible(true)
                .comparable(true)
                .editable(true)
                .searchable(true)
                .required(true)
                .multiValued(false)
                .metadata(createMetadata(2, "Technical Specifications", "Amount of RAM installed in the laptop"))
                .createdBy(SYSTEM_USER)
                .lastModifiedBy(SYSTEM_USER)
                .build();
    }

    /**
     * Creates a laptop storage product feature
     * @return ProductFeature for laptop storage
     */
    public static ProductFeature createLaptopStorageFeature(CategoryFeatureTemplate categoryFeatureTemplate, UnitOfMeasure tbUnit) {
        return ProductFeature.builder()
                .code(LAPTOP_STORAGE)
                .name("Storage")
                .description("Laptop Storage Specifications")
                .attributeType(SPECIFICATION)
                .validationPattern(STORAGE_PATTERN)
                .minValue("128")
                .maxValue("4096")
                .allowedValues(STORAGE_VALUES)
                .defaultValue(DEFAULT_STORAGE)
                .featureType(FeatureType.ENUM)
                .template(categoryFeatureTemplate)
                .visible(true)
                .comparable(true)
                .editable(true)
                .searchable(true)
                .required(true)
                .multiValued(false)
                .metadata(createMetadata(3, "Technical Specifications", "Storage capacity of the laptop"))
                .createdBy(SYSTEM_USER)
                .lastModifiedBy(SYSTEM_USER)
                .build();
    }

    public static ProductFeature createGoldPurityFeature(CategoryFeatureTemplate categoryFeatureTemplate) {
        return ProductFeature.builder()
                .code("GOLD-PURITY")
                .name("Gold Purity")
                .description("Gold Purity in Karats")
                .attributeType(SPECIFICATION)
                .template(categoryFeatureTemplate)
                .validationPattern("")
                .minValue("14")
                .maxValue("24")
                .allowedValues("14K,18K,22K,24K")
                .defaultValue("22K")
                .featureType(FeatureType.ENUM)
                .visible(true)
                .comparable(true)
                .editable(true)
                .searchable(true)
                .required(true)
                .multiValued(false)
                .metadata(createMetadata(1, "Technical Specifications", "Purity of gold in Karats"))
                .createdBy(SYSTEM_USER)
                .lastModifiedBy(SYSTEM_USER)
                .build();
    }

    public static ProductFeature createGoldWeightFeature(CategoryFeatureTemplate categoryFeatureTemplate, UnitOfMeasure gramUnit) {
        return ProductFeature.builder()
                .code("GOLD-WEIGHT")
                .name("Gold Weight")
                .description("Weight of Gold")
                .attributeType(SPECIFICATION)
                .template(categoryFeatureTemplate)
                .validationPattern("")
                .minValue("1")
                .maxValue("1000")
                .featureType(FeatureType.NUMBER)
                .visible(true)
                .comparable(true)
                .editable(true)
                .searchable(true)
                .required(true)
                .multiValued(false)
                .unitOfMeasure(gramUnit)
                .metadata(createMetadata(2, "Technical Specifications", "Weight of gold in grams"))
                .createdBy(SYSTEM_USER)
                .lastModifiedBy(SYSTEM_USER)
                .build();
    }

    public static ProductFeature createProcessorFeature(CategoryFeatureTemplate categoryFeatureTemplate){
        return  ProductFeature.builder()
                .code(LAPTOP_PROCESSOR)
                .name("Processor")
                .description("Laptop Processor Specifications")
                .attributeType(SPECIFICATION)
                .validationPattern("")
                .minValue("")
                .maxValue("")
                .allowedValues(PROCESSOR_VALUES)
                .defaultValue("")
                .featureType(FeatureType.ENUM)
                .visible(true)
                .comparable(true)
                .editable(true)
                .searchable(true)
                .required(true)
                .multiValued(false)
                .metadata(readTree("{"
                        + "\"displayOrder\": 1,"
                        + "\"group\": \"Technical Specifications\","
                        + "\"tooltip\": \"Processor model that powers the laptop\""
                        + "}"))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static ProductFeature createRamFeature(CategoryFeatureTemplate categoryFeatureTemplate, UnitOfMeasure unitOfMeasure){
        return ProductFeature.builder()
                .code(LAPTOP_RAM)
                .name("RAM")
                .description("Laptop Memory (RAM) Specifications")
                .attributeType(SPECIFICATION)
                .validationPattern(RAM_PATTERN)
                .minValue("4")
                .maxValue("128")
                .allowedValues(RAM_VALUES)
                .defaultValue(DEFAULT_RAM)
                .featureType(FeatureType.ENUM)
                .visible(true)
                .comparable(true)
                .editable(true)
                .searchable(true)
                .required(true)
                .multiValued(false)
                .metadata(readTree("{"
                        + "\"displayOrder\": 2,"
                        + "\"group\": \"Technical Specifications\","
                        + "\"tooltip\": \"Amount of RAM installed in the laptop\""
                        + "}"))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static ProductFeature createStorageFeature(CategoryFeatureTemplate categoryFeatureTemplate, UnitOfMeasure unitOfMeasure){
        return ProductFeature.builder()
                .code(LAPTOP_STORAGE)
                .name("Storage")
                .description("Laptop Storage Specifications")
                .attributeType(SPECIFICATION)
                .validationPattern(STORAGE_PATTERN)
                .minValue("128")
                .maxValue("4096")
                .unitOfMeasure(unitOfMeasure)
                .allowedValues(STORAGE_VALUES)
                .defaultValue(DEFAULT_STORAGE)
                .featureType(FeatureType.ENUM)
                .visible(true)
                .comparable(true)
                .editable(true)
                .searchable(true)
                .required(true)
                .multiValued(false)
                .metadata(readTree("{"
                        + "\"displayOrder\": 3,"
                        + "\"group\": \"Technical Specifications\","
                        + "\"tooltip\": \"Storage capacity of the laptop\""
                        + "}"))
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static JsonNode readTree(String value) {
        if (value == null || value.isEmpty() || value.equals("\"\"")) return null;
        try {
            return objectMapper.readTree(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting String to JsonNode: " + value, e);
        }
    }







}