package com.scaler.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scaler.entity.Catalog;
import com.scaler.entity.Category;
import com.scaler.entity.Merchant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class CategoryBuilder {


    private final ObjectMapper objectMapper = new ObjectMapper();

    // Cache to store categories by code for quick lookup
    private final Map<String, Category> categoryCache = new HashMap<>();
    
    public static Category createLaptopCategory(Catalog catalog, Merchant merchant) {
        return Category.builder()
                .code("LAPTOP")
                .name("Laptops")
                .description("All types of laptops and notebooks")
                .catalog(catalog)
                .merchant(merchant)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static Category createSmartphoneCategory(Catalog catalog, Merchant merchant) {
        return Category.builder()
                .code("SMARTPHONE")
                .name("Smartphones")
                .description("Mobile phones and smartphones")
                .catalog(catalog)
                .merchant(merchant)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static Category createFreshProduceCategory(Catalog catalog, Merchant merchant) {
        return Category.builder()
                .code("FRESH-PROD")
                .name("Fresh Produce")
                .description("Fresh fruits and vegetables")
                .catalog(catalog)
                .merchant(merchant)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static Category createMedicinesCategory(Catalog catalog, Merchant merchant) {
        return Category.builder()
                .code("MEDICINES")
                .name("Medicines")
                .description("Prescription and over-the-counter medicines")
                .catalog(catalog)
                .merchant(merchant)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static Category createGoldNecklaceCategory(Catalog catalog, Merchant merchant) {
        return Category.builder()
                .code("GOLD-NECKLACE")
                .name("Gold Necklaces")
                .description("Premium gold necklaces")
                .catalog(catalog)
                .merchant(merchant)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static Category createGoldBangleCategory(Catalog catalog, Merchant merchant) {
        return Category.builder()
                .code("GOLD-BANGLE")
                .name("Gold Bangles")
                .description("Premium gold bangles")
                .catalog(catalog)
                .merchant(merchant)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    public static Category createMensCategory(Catalog fashionCatalog, Merchant tataCliq) {
        return Category.builder()
                .code("MENS")
                .name("Men's Fashion")
                .description("Men's clothing and accessories")
                .catalog(fashionCatalog)
                .merchant(tataCliq)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }

    /**
     * Creates a category with metadata from a map of attributes
     *
     * @param attributes Map of category attributes
     * @return Category entity with metadata
     * @throws JsonProcessingException If there is an error processing JSON
     */
    public Category createCategoryWithMetadata(Map<String, String> attributes) throws JsonProcessingException {
        String categoryCode = attributes.getOrDefault("ClassificationCategory_Code", "");
        String categoryName = attributes.getOrDefault("ClassificationCategory_Name", "");

        log.info("Creating category with code: {}, name: {}", categoryCode, categoryName);

        // Check if category already exists in cache
        if (categoryCache.containsKey(categoryCode)) {
            log.debug("Category already exists in cache: {}", categoryCode);
            return categoryCache.get(categoryCode);
        }

        Category category = new Category();

        // Set basic category properties
        category.setId(UUID.randomUUID());
        category.setCode(categoryCode);
        category.setName(categoryName);

        // Set system fields
        category.setCreatedBy("system");
        category.setCreatedDate(LocalDateTime.now());
        category.setLastModifiedBy("system");
        category.setLastModifiedDate(LocalDateTime.now());

        // Create and set metadata as JSON
        ObjectNode metadataNode = createCategoryMetadataNode(attributes);
        category.setMetadata(metadataNode.toString());

        // Add to cache for future reference
        categoryCache.put(categoryCode, category);

        return category;
    }

    /**
     * Creates metadata for a category as a JSON node
     *
     * @param attributes Map of category attributes
     * @return ObjectNode containing category metadata
     */
    private ObjectNode createCategoryMetadataNode(Map<String, String> attributes) {
        ObjectNode metadataNode = objectMapper.createObjectNode();

        // Add all attributes to the metadata node
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            metadataNode.put(entry.getKey(), entry.getValue());
        }

        // Add super category reference if applicable
        String superCategoryCode = attributes.getOrDefault("Classification_SuperCategory_Code", "");
        if (!superCategoryCode.isEmpty()) {
            metadataNode.put("superCategoryCode", superCategoryCode);
        }

        return metadataNode;
    }

    public static Category createWomensCategory(Catalog fashionCatalog, Merchant tataCliq) {
        return Category.builder()
                .code("WOMENS")
                .name("Women's Fashion")
                .description("Women's clothing and accessories")
                .catalog(fashionCatalog)
                .merchant(tataCliq)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();
    }
}
