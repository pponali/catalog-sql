package com.example.service;

import com.example.entity.Category;
import com.example.entity.CategoryFeatureTemplate;
import com.example.entity.ProductFeature;
import com.example.entity.ProductFeatureValue;
import com.example.enums.FeatureValueType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ValidationServiceTest {

    @Autowired
    private ValidationService validationService;

    @Test
    void testBigBasketProductValidation() {
        // Test organic certification validation
        List<String> errors = validationService.validateCategoryFeature(
            "BB_FRUITS_001",
            "organic_cert",
            "INVALID-CERT",
            null
        );
        assertFalse(errors.isEmpty());
        assertTrue(errors.get(0).contains("Invalid organic certification"));

        // Test valid certification
        errors = validationService.validateCategoryFeature(
            "BB_FRUITS_001",
            "organic_cert",
            "FSSAI-ORG",
            null
        );
        assertTrue(errors.isEmpty());
    }

    @Test
    void test1mgProductValidation() {
        // Test invalid medicine composition
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("composition", "{\"invalidFormat\": \"100\"}");
        
        List<String> errors = validationService.validateCategoryFeature(
            "1MG_MEDICINES_001",
            "composition",
            metadata.get("composition").toString(),
            metadata
        );
        assertFalse(errors.isEmpty());

        // Test valid composition
        metadata.put("composition", "{\"Paracetamol\": \"500mg\", \"Caffeine\": \"30mg\"}");
        errors = validationService.validateCategoryFeature(
            "1MG_MEDICINES_001",
            "composition",
            metadata.get("composition").toString(),
            metadata
        );
        assertTrue(errors.isEmpty());
    }

    @Test
    void testTanishqProductValidation() {
        // Test invalid gold purity
        List<String> errors = validationService.validateCategoryFeature(
            "TANQ_GOLD_001",
            "purity",
            "16K",
            null
        );
        assertFalse(errors.isEmpty());
        assertTrue(errors.get(0).contains("Invalid gold purity"));

        // Test valid purity
        errors = validationService.validateCategoryFeature(
            "TANQ_GOLD_001",
            "purity",
            "22K",
            null
        );
        assertTrue(errors.isEmpty());
    }

    @Test
    void testBulkValidation() {
        // Set up category
        Category category = new Category();
        category.setCode("BB_FRUITS_001");

        // Set up feature templates
        CategoryFeatureTemplate organicTemplate = new CategoryFeatureTemplate();
        organicTemplate.setCode("organic_cert");
        organicTemplate.setAttributeType("STRING");

        CategoryFeatureTemplate shelfLifeTemplate = new CategoryFeatureTemplate();
        shelfLifeTemplate.setCode("shelf_life");
        shelfLifeTemplate.setAttributeType("NUMERIC");

        // Set up product features
        ProductFeature organicFeature = new ProductFeature();
        organicFeature.setTemplate(organicTemplate);

        ProductFeature shelfLifeFeature = new ProductFeature();
        shelfLifeFeature.setTemplate(shelfLifeTemplate);

        // Set up feature values
        ProductFeatureValue organicValue = new ProductFeatureValue();
        organicValue.setFeature(organicFeature);
        organicValue.setStringValue("FSSAI-ORG");

        ProductFeatureValue shelfLifeValue = new ProductFeatureValue();
        shelfLifeValue.setFeature(shelfLifeFeature);
        shelfLifeValue.setStringValue("30");

        List<ProductFeatureValue> features = Arrays.asList(organicValue, shelfLifeValue);

        // Test validation
        List<String> errors = validationService.validateBulkFeatures(features, category);
        assertTrue(errors.isEmpty(), "Expected no validation errors");
    }

    @Test
    void testFeatureTypeValidation() {
        // Test numeric validation
        List<String> errors = validationService.validateFeature(
            "product_price",
            "-100",
            FeatureValueType.NUMERIC.name()
        );
        assertTrue(errors.isEmpty());

        // Test string pattern validation
        errors = validationService.validateFeature(
            "product_sku",
            "INVALID-SKU",
            FeatureValueType.STRING.name()
        );
        assertTrue(errors.isEmpty());
    }
}
