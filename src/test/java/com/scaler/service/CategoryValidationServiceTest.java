package com.scaler.service;

import com.scaler.entity.*;
import com.scaler.model.ValidationRule;
import com.scaler.repository.ValidationRuleRepository;
import com.scaler.repository.ValidationRulesRepository;
import com.scaler.validation.factory.ValidationRuleFactory;
import com.scaler.validation.rule.ValidationRules;
import com.scaler.validation.service.CategoryValidationService;
import com.scaler.validation.service.ProductFeatureValidationService;
import com.scaler.validation.service.impl.CategoryValidationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryValidationServiceTest {

    @Mock
    private ValidationRuleRepository validationRuleRepository;

    @Mock
    private ValidationRulesRepository validationRulesRepository;

    @Mock
    private ValidationRuleFactory validationRuleFactory;

    @Mock
    private ProductFeatureValidationService productFeatureValidationService;

    @Mock
    private org.kie.api.runtime.KieContainer kieContainer;

    @InjectMocks
    private CategoryValidationServiceImpl categoryValidationService;

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
    private List<ValidationRule> laptopRules;
    private List<ValidationRule> jewelryRules;

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

        // Create validation rules
        laptopRules = new ArrayList<>();
        laptopRules.add(ValidationRule.createRequiredRule("PROCESSOR_REQUIRED", "Processor Required"));
        laptopRules.add(ValidationRule.createRequiredRule("RAM_REQUIRED", "RAM Required"));
        laptopRules.add(ValidationRule.createPatternRule("PROCESSOR_PATTERN", "Processor Pattern", "^(Intel|AMD|Apple).*$"));
        laptopRules.add(ValidationRule.createRangeRule("RAM_RANGE", "RAM Range", "4", "64"));

        jewelryRules = new ArrayList<>();
        jewelryRules.add(ValidationRule.createRequiredRule("GOLD_PURITY_REQUIRED", "Gold Purity Required"));
        jewelryRules.add(ValidationRule.createAllowedValuesRule("GOLD_PURITY_ALLOWED", "Gold Purity Allowed", "18K,22K,24K"));
    }

    @Test
    void testValidateProductFeature_ValidProcessor() {
        // Arrange
        processorFeature.setDefaultValue("Apple M2 Pro");
        when(validationRuleRepository.findByCategoryIdAndFeatureCode(laptopCategory.getId(), processorFeature.getCode()))
                .thenReturn(Arrays.asList(laptopRules.get(0), laptopRules.get(2)));

        // Act
        List<String> errors = categoryValidationService.validateProductFeature(processorFeature, laptopCategory);

        // Assert
        assertTrue(errors.isEmpty(), "No validation errors should be present for valid processor");
        verify(validationRuleRepository).findByCategoryIdAndFeatureCode(laptopCategory.getId(), processorFeature.getCode());
    }

    @Test
    void testValidateProductFeature_InvalidProcessor() {
        // Arrange
        processorFeature.setDefaultValue("Unknown Processor");
        when(validationRuleRepository.findByCategoryIdAndFeatureCode(laptopCategory.getId(), processorFeature.getCode()))
                .thenReturn(Arrays.asList(laptopRules.get(0), laptopRules.get(2)));

        // Act
        List<String> errors = categoryValidationService.validateProductFeature(processorFeature, laptopCategory);

        // Assert
        assertFalse(errors.isEmpty(), "Validation errors should be present for invalid processor");
        assertEquals(1, errors.size(), "There should be one validation error");
        assertTrue(errors.get(0).contains("Processor Pattern"), "Error should mention pattern validation");
        verify(validationRuleRepository).findByCategoryIdAndFeatureCode(laptopCategory.getId(), processorFeature.getCode());
    }

    @Test
    void testValidateProductFeature_MissingRequiredValue() {
        // Arrange
        processorFeature.setDefaultValue(null);
        when(validationRuleRepository.findByCategoryIdAndFeatureCode(laptopCategory.getId(), processorFeature.getCode()))
                .thenReturn(Arrays.asList(laptopRules.get(0), laptopRules.get(2)));

        // Act
        List<String> errors = categoryValidationService.validateProductFeature(processorFeature, laptopCategory);

        // Assert
        assertFalse(errors.isEmpty(), "Validation errors should be present for missing required value");
        assertEquals(1, errors.size(), "There should be one validation error");
        assertTrue(errors.get(0).contains("Required"), "Error should mention required validation");
        verify(validationRuleRepository).findByCategoryIdAndFeatureCode(laptopCategory.getId(), processorFeature.getCode());
    }

    @Test
    void testValidateProductFeatureValue_ValidGoldPurity() {
        // Arrange
        goldPurityValue = Mockito.mock(ProductFeatureValue.class);
        when(goldPurityValue.getValueAsString()).thenReturn("22K");
        
        // Mock validation rule repository
        List<ValidationRule> rules = Arrays.asList(jewelryRules.get(0), jewelryRules.get(1));
        when(validationRuleRepository.findByCategoryIdAndFeatureCode(any(UUID.class), anyString()))
                .thenReturn(rules);

        // Act
        categoryValidationService.validateProductFeatureValue(goldPurityValue, goldPurityFeature, jewelryCategory);

        // Assert
        verify(validationRuleRepository).findByCategoryIdAndFeatureCode(any(UUID.class), anyString());
    }

    @Test
    void testValidateProductFeatureValue_InvalidGoldPurity() {
        // Arrange
        goldPurityValue = Mockito.mock(ProductFeatureValue.class);
        when(goldPurityValue.getValueAsString()).thenReturn("20K");
        
        // Mock validation rule repository
        List<ValidationRule> rules = Arrays.asList(jewelryRules.get(0), jewelryRules.get(1));
        when(validationRuleRepository.findByCategoryIdAndFeatureCode(any(UUID.class), anyString()))
                .thenReturn(rules);

        // Act
        categoryValidationService.validateProductFeatureValue(goldPurityValue, goldPurityFeature, jewelryCategory);

        // Assert
        verify(validationRuleRepository).findByCategoryIdAndFeatureCode(any(UUID.class), anyString());
    }

    @Test
    void testValidateProduct_ValidMacBookPro() {
        // Arrange
        ProductCategory productCategory = ProductCategory.builder()
                .product(macBookPro)
                .category(laptopCategory)
                .build();
        Set<ProductCategory> productCategories = new HashSet<>();
        productCategories.add(productCategory);
        macBookPro.setProductCategories(productCategories);

        ProductFeatureMapping processorMapping = ProductFeatureMapping.builder()
                .product(macBookPro)
                .feature(processorFeature)
                .build();
        ProductFeatureMapping ramMapping = ProductFeatureMapping.builder()
                .product(macBookPro)
                .feature(ramFeature)
                .build();
        Set<ProductFeatureMapping> featureMappings = new HashSet<>();
        featureMappings.add(processorMapping);
        featureMappings.add(ramMapping);
        macBookPro.setFeatureMappings(featureMappings);

        processorFeature.setDefaultValue("Apple M2 Pro");
        ramFeature.setDefaultValue("16GB");

        // Mock validation rule repository
        when(validationRuleRepository.findByCategoryIdAndFeatureCode(any(UUID.class), anyString()))
                .thenReturn(Arrays.asList(laptopRules.get(0), laptopRules.get(2)));

        // Act
        categoryValidationService.validateProduct(macBookPro, laptopCategory);

        // Assert
        verify(validationRuleRepository, atLeastOnce()).findByCategoryIdAndFeatureCode(any(UUID.class), anyString());
    }

    @Test
    void testValidateProduct_InvalidMacBookPro() {
        // Arrange
        ProductCategory productCategory = ProductCategory.builder()
                .product(macBookPro)
                .category(laptopCategory)
                .build();
        Set<ProductCategory> productCategories = new HashSet<>();
        productCategories.add(productCategory);
        macBookPro.setProductCategories(productCategories);

        ProductFeatureMapping processorMapping = ProductFeatureMapping.builder()
                .product(macBookPro)
                .feature(processorFeature)
                .build();
        ProductFeatureMapping ramMapping = ProductFeatureMapping.builder()
                .product(macBookPro)
                .feature(ramFeature)
                .build();
        Set<ProductFeatureMapping> featureMappings = new HashSet<>();
        featureMappings.add(processorMapping);
        featureMappings.add(ramMapping);
        macBookPro.setFeatureMappings(featureMappings);

        processorFeature.setDefaultValue("Unknown Processor");
        ramFeature.setDefaultValue("128GB");

        // Mock validation rule repository
        when(validationRuleRepository.findByCategoryIdAndFeatureCode(any(UUID.class), anyString()))
                .thenReturn(Arrays.asList(laptopRules.get(0), laptopRules.get(2)));

        // Act
        categoryValidationService.validateProduct(macBookPro, laptopCategory);

        // Assert
        verify(validationRuleRepository, atLeastOnce()).findByCategoryIdAndFeatureCode(any(UUID.class), anyString());
    }

    @Test
    void testCreateValidationRulesForCategory() {
        // Arrange
        CategoryFeatureTemplate processorTemplate = CategoryFeatureTemplate.builder()
                .id(UUID.randomUUID())
                .code("PROCESSOR")
                .name("Processor")
                .category(laptopCategory)
                .mandatory(true)
                .validationPattern("^(Intel|AMD|Apple).*$")
                .build();

        CategoryFeatureTemplate ramTemplate = CategoryFeatureTemplate.builder()
                .id(UUID.randomUUID())
                .code("RAM")
                .name("RAM")
                .category(laptopCategory)
                .mandatory(true)
                .minValue("4")
                .maxValue("64")
                .build();

        Set<CategoryFeatureTemplate> templates = new HashSet<>();
        templates.add(processorTemplate);
        templates.add(ramTemplate);
        laptopCategory.setTemplates(templates);

        // Mock the validationRulesRepository.save method
        List<ValidationRules> mockRules = new ArrayList<>();
        mockRules.add(ValidationRules.builder().build());
        when(validationRulesRepository.save(any(ValidationRules.class))).thenReturn(ValidationRules.builder().build());

        // Act
        List<ValidationRules> rules = categoryValidationService.createValidationRulesForCategory(laptopCategory);

        // Assert
        assertNotNull(rules, "Rules should not be null");
        verify(validationRulesRepository, atLeastOnce()).save(any(ValidationRules.class));
    }
}
