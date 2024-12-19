package com.example.validation.rule;

import com.example.entity.CategoryFeatureTemplate;
import com.example.entity.ClassificationAttribute;
import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "validation_rules")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "rule_type", nullable = false)
    private RuleType ruleType;

    private String pattern;
    
    @Column(name = "min_value")
    private Double minValue;
    
    @Column(name = "max_value")
    private Double maxValue;

    @Column(name = "min_length")
    private Integer minLength;

    @Column(name = "max_length")
    private Integer maxLength;

    @Column(name = "allowed_values")
    private String allowedValues;

    @Column(name = "required")
    private boolean required;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification_attribute_id")
    private ClassificationAttribute classificationAttribute;

    public List<String> getAllowedValues() {
        if (allowedValues == null || allowedValues.trim().isEmpty()) {
            return List.of();
        }
        return Arrays.asList(allowedValues.split(","));
    }

    public enum RuleType {
        REQUIRED,
        TYPE,
        RANGE,
        PATTERN,
        LENGTH,
        ENUMERATION
    }

    public static ValidationRule createRequiredRule(CategoryFeatureTemplate template) {
        return ValidationRule.builder()
                .code("REQUIRED_" + template.getCode())
                .name("Required Value")
                .description("Value is required for " + template.getName())
                .ruleType(RuleType.REQUIRED)
                .required(true)
                .build();
    }

    public static ValidationRule createTypeRule(CategoryFeatureTemplate template) {
        return ValidationRule.builder()
                .code("TYPE_" + template.getCode())
                .name("Type Validation")
                .description("Value must match type " + template.getAttributeType())
                .ruleType(RuleType.TYPE)
                .pattern(getTypePattern(template.getAttributeType()))
                .build();
    }

    public static ValidationRule createRangeRule(CategoryFeatureTemplate template) {
        return ValidationRule.builder()
                .code("RANGE_" + template.getCode())
                .name("Range Validation")
                .description("Value must be between " + template.getMinValue() + " and " + template.getMaxValue())
                .ruleType(RuleType.RANGE)
                .minValue(template.getMinValue())
                .maxValue(template.getMaxValue())
                .build();
    }

    private static String getTypePattern(String type) {
        return switch (type.toLowerCase()) {
            case "numeric" -> "^-?\\d*\\.?\\d+$";
            case "boolean" -> "^(true|false)$";
            default -> ".*";
        };
    }
}
