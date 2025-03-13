package com.scaler.validation.factory;

import com.scaler.entity.Category;
import com.scaler.entity.CategoryFeatureTemplate;
import com.scaler.entity.ProductFeature;
import com.scaler.model.ValidationRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ValidationRuleFactoryTest {

    @InjectMocks
    private ValidationRuleFactory validationRuleFactory;

    private Category laptopCategory;
    private Category jewelryCategory;
    private CategoryFeatureTemplate processorTemplate;
    private CategoryFeatureTemplate ramTemplate;
    private CategoryFeatureTemplate goldPurityTemplate;
    private ProductFeature processorFeature;
    private ProductFeature ramFeature;
    private ProductFeature goldPurityFeature;

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

        // Create feature templates
        processorTemplate = CategoryFeatureTemplate.builder()
                .id(UUID.randomUUID())
                .code("PROCESSOR")
                .name("Processor")
                .category(laptopCategory)
                .mandatory(true)
                .validationPattern("^(Intel|AMD|Apple).*$")
                .build();

        ramTemplate = CategoryFeatureTemplate.builder()
                .id(UUID.randomUUID())
                .code("RAM")
                .name("RAM")
                .category(laptopCategory)
                .mandatory(true)
                .minValue("4")
                .maxValue("64")
                .build();

        goldPurityTemplate = CategoryFeatureTemplate.builder()
                .id(UUID.randomUUID())
                .code("GOLD_PURITY")
                .name("Gold Purity")
                .category(jewelryCategory)
                .mandatory(true)
                .allowedValues("18K,22K,24K")
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

        // Set up category templates
        Set<CategoryFeatureTemplate> laptopTemplates = new HashSet<>();
        laptopTemplates.add(processorTemplate);
        laptopTemplates.add(ramTemplate);
        laptopCategory.setTemplates(laptopTemplates);

        Set<CategoryFeatureTemplate> jewelryTemplates = new HashSet<>();
        jewelryTemplates.add(goldPurityTemplate);
        jewelryCategory.setTemplates(jewelryTemplates);
    }

    @Test
    void testCreateRulesForCategory_LaptopCategory() {
        // Act
        List<ValidationRule> rules = validationRuleFactory.createRulesForCategory(laptopCategory);

        // Assert
        assertNotNull(rules, "Rules should not be null");
        assertFalse(rules.isEmpty(), "Rules should not be empty");
        
        // We expect 4 rules: 2 required rules (one for each template) and 2 additional rules (pattern for processor, range for RAM)
        assertEquals(4, rules.size(), "There should be 4 validation rules for laptop category");
        
        // Verify required rules
        long requiredRulesCount = rules.stream()
                .filter(rule -> rule.getRuleType().equals(ValidationRule.RuleType.REQUIRED.toString()))
                .count();
        assertEquals(2, requiredRulesCount, "There should be 2 required rules");
        
        // Verify pattern rule for processor
        Optional<ValidationRule> processorPatternRule = rules.stream()
                .filter(rule -> rule.getRuleType().equals(ValidationRule.RuleType.PATTERN.toString()) && 
                        rule.getName().contains("Processor"))
                .findFirst();
        assertTrue(processorPatternRule.isPresent(), "Processor pattern rule should be present");
        assertEquals("^(Intel|AMD|Apple).*$", processorPatternRule.get().getPattern(), "Processor pattern should match");
        
        // Verify range rule for RAM
        Optional<ValidationRule> ramRangeRule = rules.stream()
                .filter(rule -> rule.getRuleType().equals(ValidationRule.RuleType.RANGE.toString()) && 
                        rule.getName().contains("RAM"))
                .findFirst();
        assertTrue(ramRangeRule.isPresent(), "RAM range rule should be present");
        assertEquals(4.0, ramRangeRule.get().getMinValue(), "RAM min value should be 4");
        assertEquals(64.0, ramRangeRule.get().getMaxValue(), "RAM max value should be 64");
    }

    @Test
    void testCreateRulesForCategory_JewelryCategory() {
        // Act
        List<ValidationRule> rules = validationRuleFactory.createRulesForCategory(jewelryCategory);

        // Assert
        assertNotNull(rules, "Rules should not be null");
        assertFalse(rules.isEmpty(), "Rules should not be empty");
        
        // We expect 2 rules: 1 required rule and 1 allowed values rule
        assertEquals(2, rules.size(), "There should be 2 validation rules for jewelry category");
        
        // Verify required rule
        Optional<ValidationRule> requiredRule = rules.stream()
                .filter(rule -> rule.getRuleType().equals(ValidationRule.RuleType.REQUIRED.toString()))
                .findFirst();
        assertTrue(requiredRule.isPresent(), "Required rule should be present");
        
        // Verify allowed values rule
        Optional<ValidationRule> allowedValuesRule = rules.stream()
                .filter(rule -> rule.getRuleType().equals(ValidationRule.RuleType.ALLOWED_VALUES.toString()))
                .findFirst();
        assertTrue(allowedValuesRule.isPresent(), "Allowed values rule should be present");
        List<String> allowedValues = allowedValuesRule.get().getAllowedValues();
        assertEquals(3, allowedValues.size(), "There should be 3 allowed values");
        assertTrue(allowedValues.contains("18K"), "18K should be an allowed value");
        assertTrue(allowedValues.contains("22K"), "22K should be an allowed value");
        assertTrue(allowedValues.contains("24K"), "24K should be an allowed value");
    }

    @Test
    void testCreateRulesForTemplate_ProcessorTemplate() {
        // Act
        List<ValidationRule> rules = validationRuleFactory.createRulesForTemplate(processorTemplate);

        // Assert
        assertNotNull(rules, "Rules should not be null");
        assertFalse(rules.isEmpty(), "Rules should not be empty");
        
        // We expect 2 rules: 1 required rule and 1 pattern rule
        assertEquals(2, rules.size(), "There should be 2 validation rules for processor template");
        
        // Verify required rule
        Optional<ValidationRule> requiredRule = rules.stream()
                .filter(rule -> rule.getRuleType().equals(ValidationRule.RuleType.REQUIRED.toString()))
                .findFirst();
        assertTrue(requiredRule.isPresent(), "Required rule should be present");
        
        // Verify pattern rule
        Optional<ValidationRule> patternRule = rules.stream()
                .filter(rule -> rule.getRuleType().equals(ValidationRule.RuleType.PATTERN.toString()))
                .findFirst();
        assertTrue(patternRule.isPresent(), "Pattern rule should be present");
        assertEquals("^(Intel|AMD|Apple).*$", patternRule.get().getPattern(), "Pattern should match");
    }

    @Test
    void testCreateRulesForTemplate_RamTemplate() {
        // Act
        List<ValidationRule> rules = validationRuleFactory.createRulesForTemplate(ramTemplate);

        // Assert
        assertNotNull(rules, "Rules should not be null");
        assertFalse(rules.isEmpty(), "Rules should not be empty");
        
        // We expect 2 rules: 1 required rule and 1 range rule
        assertEquals(2, rules.size(), "There should be 2 validation rules for RAM template");
        
        // Verify required rule
        Optional<ValidationRule> requiredRule = rules.stream()
                .filter(rule -> rule.getRuleType().equals(ValidationRule.RuleType.REQUIRED.toString()))
                .findFirst();
        assertTrue(requiredRule.isPresent(), "Required rule should be present");
        
        // Verify range rule
        Optional<ValidationRule> rangeRule = rules.stream()
                .filter(rule -> rule.getRuleType().equals(ValidationRule.RuleType.RANGE.toString()))
                .findFirst();
        assertTrue(rangeRule.isPresent(), "Range rule should be present");
        assertEquals(4.0, rangeRule.get().getMinValue(), "Min value should be 4");
        assertEquals(64.0, rangeRule.get().getMaxValue(), "Max value should be 64");
    }

    @Test
    void testCreateRulesForTemplate_GoldPurityTemplate() {
        // Act
        List<ValidationRule> rules = validationRuleFactory.createRulesForTemplate(goldPurityTemplate);

        // Assert
        assertNotNull(rules, "Rules should not be null");
        assertFalse(rules.isEmpty(), "Rules should not be empty");
        
        // We expect 2 rules: 1 required rule and 1 allowed values rule
        assertEquals(2, rules.size(), "There should be 2 validation rules for gold purity template");
        
        // Verify required rule
        Optional<ValidationRule> requiredRule = rules.stream()
                .filter(rule -> rule.getRuleType().equals(ValidationRule.RuleType.REQUIRED.toString()))
                .findFirst();
        assertTrue(requiredRule.isPresent(), "Required rule should be present");
        
        // Verify allowed values rule
        Optional<ValidationRule> allowedValuesRule = rules.stream()
                .filter(rule -> rule.getRuleType().equals(ValidationRule.RuleType.ALLOWED_VALUES.toString()))
                .findFirst();
        assertTrue(allowedValuesRule.isPresent(), "Allowed values rule should be present");
        List<String> allowedValues = allowedValuesRule.get().getAllowedValues();
        assertEquals(3, allowedValues.size(), "There should be 3 allowed values");
        assertTrue(allowedValues.contains("18K"), "18K should be an allowed value");
        assertTrue(allowedValues.contains("22K"), "22K should be an allowed value");
        assertTrue(allowedValues.contains("24K"), "24K should be an allowed value");
    }

    @Test
    void testCreateRulesForFeature_ProcessorFeature() {
        // Act
        List<ValidationRule> rules = validationRuleFactory.createRulesForFeature(processorFeature, laptopCategory);

        // Assert
        assertNotNull(rules, "Rules should not be null");
        assertFalse(rules.isEmpty(), "Rules should not be empty");
        
        // We expect 2 rules: 1 required rule and 1 pattern rule
        assertEquals(2, rules.size(), "There should be 2 validation rules for processor feature");
        
        // Verify required rule
        Optional<ValidationRule> requiredRule = rules.stream()
                .filter(rule -> rule.getRuleType().equals(ValidationRule.RuleType.REQUIRED.toString()))
                .findFirst();
        assertTrue(requiredRule.isPresent(), "Required rule should be present");
        
        // Verify pattern rule
        Optional<ValidationRule> patternRule = rules.stream()
                .filter(rule -> rule.getRuleType().equals(ValidationRule.RuleType.PATTERN.toString()))
                .findFirst();
        assertTrue(patternRule.isPresent(), "Pattern rule should be present");
        assertEquals("^(Intel|AMD|Apple).*$", patternRule.get().getPattern(), "Pattern should match");
    }
}
