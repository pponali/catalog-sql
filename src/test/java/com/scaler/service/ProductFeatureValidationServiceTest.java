package com.scaler.service;

import com.scaler.entity.*;
import com.scaler.model.ValidationRule;
import com.scaler.repository.ValidationRuleRepository;
import com.scaler.validation.factory.ValidationRuleFactory;
import com.scaler.validation.rule.ValidationRules;
import com.scaler.validation.service.CategoryValidationService;
import com.scaler.validation.service.ProductFeatureValidationService;
import com.scaler.validation.service.impl.ProductFeatureValidationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductFeatureValidationServiceTest {

    @Mock
    private ValidationRuleRepository validationRuleRepository;

    @Mock
    private ValidationRuleFactory validationRuleFactory;

    @Mock
    private CategoryValidationService categoryValidationService;
    
    @Mock
    private org.kie.api.runtime.KieContainer kieContainer;

    @InjectMocks
    private ProductFeatureValidationServiceImpl productFeatureValidationService;

    private Category laptopCategory;
    private Category jewelryCategory;
    private Product macBookPro;
    private Product goldNecklace;
    private ProductFeature processorFeature;
    private ProductFeature ramFeature;
    private ProductFeature goldPurityFeature;
    private ProductFeatureValue processorValue;
    private ProductFeatureValue ramValue;
    private ProductFeatureValue goldPurityValue;

    @BeforeEach
    void setUp() {
        // Create categories
        laptopCategory = Category.builder()
                .id(UUID.randomUUID())
                .code("LAPTOP")
                .name("Laptops")
                .build();

        jewelryCategory = Category.builder()
                .id(UUID.randomUUID())
                .code("JEWELRY")
                .name("Jewelry")
                .build();

        // Create products
        macBookPro = Product.builder()
                .id(UUID.randomUUID())
                .code("MACBOOK_PRO")
                .name("MacBook Pro")
                .build();

        goldNecklace = Product.builder()
                .id(UUID.randomUUID())
                .code("GOLD_NECKLACE")
                .name("Gold Necklace")
                .build();

        // Create feature templates
        CategoryFeatureTemplate processorTemplate = CategoryFeatureTemplate.builder()
                .id(UUID.randomUUID())
                .code("PROCESSOR")
                .name("Processor")
                .category(laptopCategory)
                .mandatory(true)
                .build();

        CategoryFeatureTemplate ramTemplate = CategoryFeatureTemplate.builder()
                .id(UUID.randomUUID())
                .code("RAM")
                .name("RAM")
                .category(laptopCategory)
                .mandatory(true)
                .build();

        CategoryFeatureTemplate goldPurityTemplate = CategoryFeatureTemplate.builder()
                .id(UUID.randomUUID())
                .code("GOLD_PURITY")
                .name("Gold Purity")
                .category(jewelryCategory)
                .mandatory(true)
                .build();

        // Create features
        processorFeature = ProductFeature.builder()
                .id(UUID.randomUUID())
                .code("PROCESSOR")
                .name("Processor")
                .template(processorTemplate)
                .build();

        ramFeature = ProductFeature.builder()
                .id(UUID.randomUUID())
                .code("RAM")
                .name("RAM")
                .template(ramTemplate)
                .build();

        goldPurityFeature = ProductFeature.builder()
                .id(UUID.randomUUID())
                .code("GOLD_PURITY")
                .name("Gold Purity")
                .template(goldPurityTemplate)
                .build();

        // Create feature values
        processorValue = ProductFeatureValue.builder()
                .id(UUID.randomUUID())
                .feature(processorFeature)
                .build();

        ramValue = ProductFeatureValue.builder()
                .id(UUID.randomUUID())
                .feature(ramFeature)
                .build();

        goldPurityValue = ProductFeatureValue.builder()
                .id(UUID.randomUUID())
                .feature(goldPurityFeature)
                .build();
    }

    @Test
    void testValidateFeature_ValidProcessor() {
        // Arrange
        List<String> noErrors = Collections.emptyList();
        when(categoryValidationService.validateProductFeature(processorFeature, laptopCategory))
                .thenReturn(noErrors);

        // Act
        List<String> errors = productFeatureValidationService.validateFeature(processorFeature);

        // Assert
        assertTrue(errors.isEmpty(), "No validation errors should be present for valid processor");
        verify(categoryValidationService).validateProductFeature(processorFeature, laptopCategory);
    }

    @Test
    void testValidateFeature_InvalidProcessor() {
        // Arrange
        List<String> validationErrors = Collections.singletonList("Invalid processor");
        when(categoryValidationService.validateProductFeature(processorFeature, laptopCategory))
                .thenReturn(validationErrors);

        // Act
        List<String> errors = productFeatureValidationService.validateFeature(processorFeature);

        // Assert
        assertFalse(errors.isEmpty(), "Validation errors should be present for invalid processor");
        assertEquals(1, errors.size(), "There should be one validation error");
        assertEquals("Invalid processor", errors.get(0), "Error message should match");
        verify(categoryValidationService).validateProductFeature(processorFeature, laptopCategory);
    }

    @Test
    void testValidateFeatureValue_ValidGoldPurity() {
        // Arrange
        List<String> noErrors = Collections.emptyList();
        when(categoryValidationService.validateProductFeatureValue(goldPurityValue, goldPurityFeature, jewelryCategory))
                .thenReturn(noErrors);

        // Act
        List<String> errors = productFeatureValidationService.validateFeatureValue(goldPurityValue);

        // Assert
        assertTrue(errors.isEmpty(), "No validation errors should be present for valid gold purity");
        verify(categoryValidationService).validateProductFeatureValue(goldPurityValue, goldPurityFeature, jewelryCategory);
    }

    @Test
    void testValidateFeatureValue_InvalidGoldPurity() {
        // Arrange
        List<String> validationErrors = Collections.singletonList("Invalid gold purity");
        when(categoryValidationService.validateProductFeatureValue(goldPurityValue, goldPurityFeature, jewelryCategory))
                .thenReturn(validationErrors);

        // Act
        List<String> errors = productFeatureValidationService.validateFeatureValue(goldPurityValue);

        // Assert
        assertFalse(errors.isEmpty(), "Validation errors should be present for invalid gold purity");
        assertEquals(1, errors.size(), "There should be one validation error");
        assertEquals("Invalid gold purity", errors.get(0), "Error message should match");
        verify(categoryValidationService).validateProductFeatureValue(goldPurityValue, goldPurityFeature, jewelryCategory);
    }

    @Test
    void testValidateProductFeatures_ValidMacBookPro() {
        // Arrange
        ProductCategory productCategory = ProductCategory.builder()
                .product(macBookPro)
                .category(laptopCategory)
                .build();
        Set<ProductCategory> productCategories = new HashSet<>();
        productCategories.add(productCategory);
        macBookPro.setProductCategories(productCategories);

        Map<String, List<String>> noErrors = Collections.emptyMap();
        when(categoryValidationService.validateProduct(macBookPro, laptopCategory))
                .thenReturn(noErrors);

        // Act
        Map<String, List<String>> validationResults = productFeatureValidationService.validateProductFeatures(macBookPro);

        // Assert
        assertTrue(validationResults.isEmpty(), "No validation errors should be present for valid MacBook Pro");
        verify(categoryValidationService).validateProduct(macBookPro, laptopCategory);
    }

    @Test
    void testValidateProductFeatures_InvalidMacBookPro() {
        // Arrange
        ProductCategory productCategory = ProductCategory.builder()
                .product(macBookPro)
                .category(laptopCategory)
                .build();
        Set<ProductCategory> productCategories = new HashSet<>();
        productCategories.add(productCategory);
        macBookPro.setProductCategories(productCategories);

        Map<String, List<String>> validationErrors = new HashMap<>();
        validationErrors.put("PROCESSOR", Collections.singletonList("Invalid processor"));
        validationErrors.put("RAM", Collections.singletonList("Invalid RAM"));
        when(categoryValidationService.validateProduct(macBookPro, laptopCategory))
                .thenReturn(validationErrors);

        // Act
        Map<String, List<String>> validationResults = productFeatureValidationService.validateProductFeatures(macBookPro);

        // Assert
        assertFalse(validationResults.isEmpty(), "Validation errors should be present for invalid MacBook Pro");
        assertEquals(2, validationResults.size(), "There should be validation errors for both processor and RAM");
        assertTrue(validationResults.containsKey("PROCESSOR"), "Validation errors should include processor");
        assertTrue(validationResults.containsKey("RAM"), "Validation errors should include RAM");
        verify(categoryValidationService).validateProduct(macBookPro, laptopCategory);
    }

    @Test
    void testValidateProductFeaturesForCategory_ValidMacBookPro() {
        // Arrange
        Map<String, List<String>> noErrors = Collections.emptyMap();
        when(categoryValidationService.validateProduct(macBookPro, laptopCategory))
                .thenReturn(noErrors);

        // Act
        Map<String, List<String>> validationResults = productFeatureValidationService.validateProductFeaturesForCategory(macBookPro, laptopCategory);

        // Assert
        assertTrue(validationResults.isEmpty(), "No validation errors should be present for valid MacBook Pro");
        verify(categoryValidationService).validateProduct(macBookPro, laptopCategory);
    }

    @Test
    void testValidateProductFeaturesForCategory_InvalidMacBookPro() {
        // Arrange
        Map<String, List<String>> validationErrors = new HashMap<>();
        validationErrors.put("PROCESSOR", Collections.singletonList("Invalid processor"));
        validationErrors.put("RAM", Collections.singletonList("Invalid RAM"));
        when(categoryValidationService.validateProduct(macBookPro, laptopCategory))
                .thenReturn(validationErrors);

        // Act
        Map<String, List<String>> validationResults = productFeatureValidationService.validateProductFeaturesForCategory(macBookPro, laptopCategory);

        // Assert
        assertFalse(validationResults.isEmpty(), "Validation errors should be present for invalid MacBook Pro");
        assertEquals(2, validationResults.size(), "There should be validation errors for both processor and RAM");
        assertTrue(validationResults.containsKey("PROCESSOR"), "Validation errors should include processor");
        assertTrue(validationResults.containsKey("RAM"), "Validation errors should include RAM");
        verify(categoryValidationService).validateProduct(macBookPro, laptopCategory);
    }
}
