package com.scaler.builder;

import com.scaler.entity.*;
import com.scaler.model.ValidationRule;
import com.scaler.repository.*;
import com.scaler.validation.rule.ValidationRules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ValidationBuilder {


    @Autowired
    private ValidationRuleRepository validationRuleRepository;

    @Autowired
    private ValidationRulesRepository validationRulesRepository;


    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private ProductInventoryRepository productInventoryRepository;


    @Autowired
    private CategoryFeatureTemplateRepository categoryFeatureTemplateRepository;
    public  void validationRules() {
        // Create product-level validation rules
        ProductFeature sizeFeature = ProductFeature.builder()
                .code("SIZE")
                .name("Size")
                .createdBy("SYSTEM")
                .description("Product size")
                .build();

        // Required validation rule for size
        ValidationRule sizeRequired = ValidationRule.builder()
                .code("SIZE_REQUIRED")
                .name("Size Required")
                .description("Size is required for fashion products")
                .ruleType(ValidationRule.RuleType.REQUIRED.toString())
                .feature(sizeFeature)
                .priority(1)
                .createdBy("SYSTEM")
                .active(true)
                .build();

        // Size range validation rule
        ValidationRule sizeRange = ValidationRule.builder()
                .code("SIZE_RANGE")
                .name("Valid Size Range")
                .description("Size must be within valid range")
                .ruleType(ValidationRule.RuleType.RANGE.toString())
                .ruleExpression("0,100")
                .feature(sizeFeature)
                .priority(2)
                .createdBy("SYSTEM")
                .active(true)
                .build();

        validationRuleRepository.saveAll(List.of(sizeRequired, sizeRange));

        // Create category-level validation rules
        CategoryFeatureTemplate priceTemplate = CategoryFeatureTemplate.builder()
                .code("PRICE")
                .name("Price")
                .description("Product price")
                .attributeType("DECIMAL")
                .minValue("0")
                .createdBy("SYSTEM")
                .maxValue("1000000")
                .build();
        categoryFeatureTemplateRepository.save(priceTemplate);

        // Required rule for price
        ValidationRules priceRequired = ValidationRules.createRequiredRule(priceTemplate);

        // Range rule for price
        ValidationRules priceRange = ValidationRules.createRangeRule(priceTemplate);

        // Type rule for price
        ValidationRules priceType = ValidationRules.createTypeRule(priceTemplate);

        validationRulesRepository.saveAll(List.of(priceRequired, priceRange, priceType));

        // Create category-level validation rules for color
        CategoryFeatureTemplate colorTemplate = CategoryFeatureTemplate.builder()
                .code("COLOR")
                .name("Color")
                .description("Product color")
                .attributeType("STRING")
                .allowedValues("RED,BLUE,GREEN,BLACK,WHITE")
                .createdBy("SYSTEM")
                .build();
        categoryFeatureTemplateRepository.save(colorTemplate);

        // Required rule for color
        ValidationRules colorRequireds = ValidationRules.createRequiredRule(colorTemplate);

        // Allowed values rule for color
        ValidationRules colorAllowedValues = ValidationRules.createAllowedValuesRule(colorTemplate);

        validationRulesRepository.saveAll(List.of(colorRequireds, colorAllowedValues));

        // Create SQL-based validation rules for product features
        ProductFeature colorFeature = ProductFeature.builder()
                .code("COLOR")
                .name("Color")
                .description("Product color")
                .createdBy("SYSTEM")
                .build();

        // Required validation rule
        ValidationRule colorRequired = ValidationRule.builder()
                .code("COLOR_REQUIRED")
                .name("Color Required")
                .description("Color is required for fashion products")
                .ruleType(ValidationRule.RuleType.REQUIRED.toString())
                .feature(colorFeature)
                .priority(1)
                .active(true)
                .createdBy("SYSTEM")
                .build();

        // Allowed values validation rule
        ValidationRule colorAllowedValue = ValidationRule.builder()
                .code("COLOR_VALUES")
                .name("Valid Colors")
                .description("List of valid colors")
                .ruleType(ValidationRule.RuleType.ALLOWED_VALUES.toString())
                .ruleExpression("RED,BLUE,GREEN,BLACK,WHITE")
                .feature(colorFeature)
                .priority(2)
                .active(true)
                .createdBy("SYSTEM")
                .build();

        validationRuleRepository.saveAll(List.of(colorRequired, colorAllowedValue));

        // Create NoSQL-based validation rules for TataCliq channel
        ValidationRules channelRules = new ValidationRules();


        // Note: In a real application, these NoSQL rules would be saved to a NoSQL database
        // For demo purposes, we're just creating the objects
    }
}
