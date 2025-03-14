package com.scaler.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scaler.entity.Category;
import com.scaler.entity.ProductCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Builder class for creating Category entities with metadata
 * This class encapsulates the logic for creating and configuring category entities
 */
@Slf4j
@Component
public class CategoryBuilder {
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // Cache to store categories by code for quick lookup
    private final Map<String, Category> categoryCache = new HashMap<>();

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
     * Creates a category from the primary hierarchy data
     *
     * @param hierarchyData Map containing hierarchy data
     * @param level The level in the hierarchy (1-4)
     * @return Category entity
     * @throws JsonProcessingException If there is an error processing JSON
     */
    public Category createCategoryFromHierarchy(Map<String, String> hierarchyData, int level) throws JsonProcessingException {
        String categoryCode = hierarchyData.getOrDefault("Category_Level" + level + "_Code", "");
        String categoryName = hierarchyData.getOrDefault("Category_Level" + level + "_name", "");
        
        if (categoryCode.isEmpty() || categoryName.isEmpty()) {
            log.warn("Invalid hierarchy data for level {}: {}", level, hierarchyData);
            throw new IllegalArgumentException("Invalid hierarchy data for level " + level);
        }
        
        log.info("Creating category from hierarchy level {}: code={}, name={}", level, categoryCode, categoryName);
        
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
        
        // Create metadata node for the category
        ObjectNode metadataNode = objectMapper.createObjectNode();
        metadataNode.put("hierarchyLevel", level);
        metadataNode.put("productLine", hierarchyData.getOrDefault("Category_Level1_name", ""));
        
        // Add parent category reference if applicable
        if (level > 1) {
            String parentCode = hierarchyData.getOrDefault("Category_Level" + (level - 1) + "_Code", "");
            metadataNode.put("parentCategoryCode", parentCode);
        }
        
        category.setMetadata(metadataNode.toString());
        
        // Add to cache for future reference
        categoryCache.put(categoryCode, category);
        
        return category;
    }
    
    /**
     * Creates a ProductCategory association between a product and a category
     *
     * @param categoryCode The code of the category
     * @return ProductCategory entity
     */
    public ProductCategory createProductCategory(String categoryCode) {
        log.info("Creating product-category association for category code: {}", categoryCode);
        
        // Check if category exists in cache
        if (!categoryCache.containsKey(categoryCode)) {
            log.warn("Category not found in cache: {}", categoryCode);
            throw new IllegalArgumentException("Category not found: " + categoryCode);
        }
        
        Category category = categoryCache.get(categoryCode);
        
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(UUID.randomUUID());
        productCategory.setCategory(category);
        
        // Set system fields
        productCategory.setCreatedBy("system");
        productCategory.setCreatedDate(LocalDateTime.now());
        
        return productCategory;
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
    
    /**
     * Gets a category from the cache by its code
     *
     * @param categoryCode The code of the category
     * @return Category entity or null if not found
     */
    public Category getCategoryByCode(String categoryCode) {
        return categoryCache.get(categoryCode);
    }
    
    /**
     * Clears the category cache
     */
    public void clearCache() {
        categoryCache.clear();
    }
}
