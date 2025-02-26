package com.scaler.builder;

import static com.scaler.constants.FeatureConstants.*;
import com.scaler.constants.FeatureConstants;
import com.scaler.entity.Category;
import com.scaler.entity.CategoryFeatureTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scaler.entity.FeatureType;
import com.scaler.entity.UnitOfMeasure;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

/**
 * Utility class for building feature templates.
 * Provides reusable methods to create common feature templates.
 */
@Slf4j
public class FeatureTemplateBuilder {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private FeatureTemplateBuilder() {
        // Private constructor to prevent instantiation
    }

    /**
     * Creates metadata for a feature template
     * @param displayOrder Display order of the feature
     * @param group Feature group name
     * @param tooltip Tooltip text
     * @return JSON string containing metadata
     */
    private static String createMetadata(int displayOrder, String group, String tooltip) {
        try {
            ObjectNode metadata = objectMapper.createObjectNode()
                .put("displayOrder", displayOrder)
                .put("group", group)
                .put("tooltip", tooltip);
            return metadata.toString();
        } catch (Exception e) {
            log.error("Error creating metadata: {}", e.getMessage());
            return "{}";
        }
    }

    /**
     * Creates a laptop processor feature template
     * @param category Parent category
     * @return CategoryFeatureTemplate for laptop processor
     */
    public static CategoryFeatureTemplate createLaptopProcessorTemplate(Category category) {
        return CategoryFeatureTemplate.builder()
                .code(LAPTOP_PROCESSOR)
                .name("Processor")  // Required in parent
                .description("Laptop Processor Specifications")
                .attributeType(SPECIFICATION)
                .validationPattern("")
                .minValue("")
                .maxValue("")
                .allowedValues(PROCESSOR_VALUES)
                .defaultValue("")
                .featureType(FeatureType.ENUM)  // Required in parent
                .dataType(STRING)  // Required in parent
                .inputType(DROPDOWN)  // Required in parent
                .visible(true)
                .filterable(true)
                .inherited(false)
                .hidden(false)
                .editable(true)
                .searchable(true)
                .comparable(true)
                .mandatory(true)
                .multiValued(false)
                .createdBy(FeatureConstants.SYSTEM_USER)
                .lastModifiedBy(FeatureConstants.SYSTEM_USER)
                .category(category)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }

    /**
     * Creates a laptop RAM feature template
     * @param category Parent category
     * @return CategoryFeatureTemplate for laptop RAM
     */
    public static CategoryFeatureTemplate createLaptopRamTemplate(Category category, UnitOfMeasure gbUnit) {
        return CategoryFeatureTemplate.builder()
                .code(LAPTOP_RAM)
                .name("RAM")  // Required in parent
                .description("Laptop Memory (RAM) Specifications")
                .attributeType(SPECIFICATION)
                .validationPattern(RAM_PATTERN)
                .minValue("4")
                .maxValue("128")
                .allowedValues(RAM_VALUES)
                .defaultValue(DEFAULT_RAM)
                .featureType(FeatureType.ENUM)  // Required in parent
                .dataType(STRING)  // Required in parent
                .inputType(DROPDOWN)  // Required in parent
                .visible(true)
                .filterable(true)
                .inherited(false)
                .hidden(false)
                .editable(true)
                .searchable(true)
                .comparable(true)
                .mandatory(true)
                .multiValued(false)
                .createdBy(FeatureConstants.SYSTEM_USER)
                .lastModifiedBy(FeatureConstants.SYSTEM_USER)
                .category(category)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }

    /**
     * Creates a laptop storage feature template
     * @param category Parent category
     * @return CategoryFeatureTemplate for laptop storage
     */
    public static CategoryFeatureTemplate createLaptopStorageTemplate(Category category, UnitOfMeasure tbUnit) {
        return CategoryFeatureTemplate.builder()
                .code(LAPTOP_STORAGE)
                .name("Storage")  // Required in parent
                .description("Laptop Storage Specifications")
                .attributeType(SPECIFICATION)
                .validationPattern(STORAGE_PATTERN)
                .minValue("128")
                .maxValue("4096")
                .allowedValues(STORAGE_VALUES)
                .defaultValue(DEFAULT_STORAGE)
                .featureType(FeatureType.ENUM)  // Required in parent
                .dataType(STRING)  // Required in parent
                .inputType(DROPDOWN)  // Required in parent
                .visible(true)
                .filterable(true)
                .inherited(false)
                .hidden(false)
                .editable(true)
                .searchable(true)
                .comparable(true)
                .mandatory(true)
                .multiValued(false)
                .createdBy(FeatureConstants.SYSTEM_USER)
                .lastModifiedBy(FeatureConstants.SYSTEM_USER)
                .category(category)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }


    public static CategoryFeatureTemplate createGoldPurityTemplate(Category category) {
        return CategoryFeatureTemplate.builder()
                .code("GOLD-PURITY")
                .name("Gold Purity")
                .description("Gold Purity in Karats")
                .attributeType("SPECIFICATION")
                .validationPattern("")
                .minValue("14")
                .maxValue("24")
                .allowedValues("14K,18K,22K,24K")
                .defaultValue("22K")
                .featureType(FeatureType.ENUM)
                .dataType("STRING")
                .inputType("DROPDOWN")
                .visible(true)
                .filterable(true)
                .inherited(false)
                .hidden(false)
                .editable(true)
                .searchable(true)
                .comparable(true)
                .mandatory(true)
                .multiValued(false)
                .category(category)
                .createdBy(SYSTEM_USER)
                .lastModifiedBy(SYSTEM_USER)
                .build();
    }

    public static CategoryFeatureTemplate createGoldWeightTemplate(Category category, UnitOfMeasure gramUnit) {
        return CategoryFeatureTemplate.builder()
                .code("GOLD-WEIGHT")
                .name("Gold Weight")
                .description("Weight of Gold")
                .attributeType("SPECIFICATION")
                .validationPattern("")
                .minValue("1")
                .maxValue("1000")
                .defaultValue("")
                .featureType(FeatureType.NUMBER)
                .dataType("DECIMAL")
                .inputType("NUMBER")
                .visible(true)
                .filterable(true)
                .inherited(false)
                .hidden(false)
                .editable(true)
                .searchable(true)
                .comparable(true)
                .mandatory(true)
                .multiValued(false)
                .category(category)
                .unit(gramUnit)
                .createdBy(SYSTEM_USER)
                .lastModifiedBy(SYSTEM_USER)
                .build();
    }
}
