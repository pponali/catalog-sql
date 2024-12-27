package com.scaler.validation.rule;

import com.scaler.entity.CategoryFeatureTemplate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "validation_rules")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationRules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "template_id")
    private CategoryFeatureTemplate template;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    private String description;
    private String ruleType;
    private String pattern;
    private String minValue;
    private String maxValue;
    private String allowedValues;
    private String errorMessage;

    public static ValidationRules createRequiredRule(CategoryFeatureTemplate template) {
        return ValidationRules.builder()
                .code(template.getCode() + "_REQUIRED")
                .name(template.getName() + " Required")
                .description("Validates that " + template.getName() + " is not empty")
                .ruleType("REQUIRED")
                .errorMessage("Value is required")
                .build();
    }

    public static ValidationRules createTypeRule(CategoryFeatureTemplate template) {
        return ValidationRules.builder()
                .code(template.getCode() + "_TYPE")
                .name(template.getName() + " Type")
                .description("Validates that " + template.getName() + " is of type " + template.getAttributeType())
                .ruleType("TYPE")
                .pattern(template.getAttributeType())
                .errorMessage("Invalid type. Expected: " + template.getAttributeType())
                .build();
    }

    public static ValidationRules createRangeRule(CategoryFeatureTemplate template) {
        return ValidationRules.builder()
                .code(template.getCode() + "_RANGE")
                .name(template.getName() + " Range")
                .description("Validates that " + template.getName() + " is between " + template.getMinValue() + " and " + template.getMaxValue())
                .ruleType("RANGE")
                .minValue(template.getMinValue())
                .maxValue(template.getMaxValue())
                .errorMessage("Value must be between " + template.getMinValue() + " and " + template.getMaxValue())
                .build();
    }

    public static ValidationRules createPatternRule(CategoryFeatureTemplate template) {
        return ValidationRules.builder()
                .code(template.getCode())
                .name(template.getName())
                .description("Pattern validation for " + template.getName())
                .ruleType("PATTERN")
                .pattern(template.getValidationPattern())
                .errorMessage("Value does not match required pattern")
                .build();
    }

    public static ValidationRules createAllowedValuesRule(CategoryFeatureTemplate template) {
        return ValidationRules.builder()
                .code(template.getCode())
                .name(template.getName())
                .description("Allowed values validation for " + template.getName())
                .ruleType("ALLOWED_VALUES")
                .allowedValues(template.getAllowedValues())
                .errorMessage("Value is not in the list of allowed values")
                .build();
    }

    public static ValidationRules createCustomRule(CategoryFeatureTemplate template) {
        return ValidationRules.builder()
                .code(template.getCode())
                .name(template.getName())
                .description("Custom validation for " + template.getName())
                .ruleType("CUSTOM")
                .pattern(template.getValidationPattern())
                .errorMessage("Value does not meet custom validation requirements")
                .build();
    }
}
