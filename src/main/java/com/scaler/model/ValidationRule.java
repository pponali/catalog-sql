package com.scaler.model;

import com.scaler.entity.ProductFeature;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @Column(unique = true, nullable = false)
    private String code;

    private String name;
    private String description;
    private String ruleType;
    private String ruleExpression;
    private int priority;

    @ManyToOne
    private ProductFeature feature;
    private boolean active;

    @Column(name = "validation_pattern")
    private String pattern;

    @Column(name = "allowed_values")
    private String allowedValues;

    @Column(name = "min_value")
    private String minValue;

    @Column(name = "max_value")
    private String maxValue;

    @Column(name = "feature_id")
    private Long featureId;

    public enum RuleType {
        REQUIRED,
        PATTERN,
        LENGTH,
        RANGE,
        DEPENDENCY,
        CUSTOM
    }

    public String getRuleType() {
        return ruleType;
    }

    public String getPattern() {
        return ruleExpression;
    }

    public Double getMinValue() {
        String[] values = ruleExpression.split(",");
        if (values.length > 1) {
            return Double.parseDouble(values[0]);
        }
        return null;
    }

    public Double getMaxValue() {
        String[] values = ruleExpression.split(",");
        if (values.length > 1) {
            return Double.parseDouble(values[1]);
        }
        return null;
    }

    public List<String> getAllowedValues() {
        // Assuming allowed values are comma separated in ruleExpression
        String[] values = ruleExpression.split(",");
        return List.of(values);
    }

    public String getName() {
        return name;
    }

    public static ValidationRule createRequiredRule(String code, String name) {
        return ValidationRule.builder()
                .code("REQUIRED_" + code)
                .name(name)
                .description("Field is required")
                .ruleType(RuleType.REQUIRED.toString())
                .ruleExpression("")
                .priority(0)
                .active(true)
                .build();
    }

    public static ValidationRule createPatternRule(String code, String name, String pattern) {
        return ValidationRule.builder()
                .code("PATTERN_" + code)
                .name(name)
                .description("Value must match pattern")
                .ruleType(RuleType.PATTERN.toString())
                .ruleExpression(pattern)
                .priority(0)
                .active(true)
                .build();
    }

    public static ValidationRule createRangeRule(String code, String name, String min, String max) {
        return ValidationRule.builder()
                .code("RANGE_" + code)
                .name(name)
                .description("Value must be within range")
                .ruleType(RuleType.RANGE.toString())
                .ruleExpression(String.format("%s,%s", min, max))
                .priority(0)
                .active(true)
                .build();
    }

    public static ValidationRule createAllowedValuesRule(String code, String name, String allowedValues) {
        return ValidationRule.builder()
                .code("ALLOWED_VALUES_" + code)
                .name(name)
                .description("Value must be one of allowed values")
                .ruleType(RuleType.DEPENDENCY.toString())
                .ruleExpression(allowedValues)
                .priority(0)
                .active(true)
                .build();
    }
}
