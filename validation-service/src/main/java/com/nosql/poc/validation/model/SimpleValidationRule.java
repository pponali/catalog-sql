package com.nosql.poc.validation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simplified validation rule model for basic validation scenarios.
 * This entity represents a single validation rule that can be applied to an entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "validation_rules")
@CompoundIndexes({
    @CompoundIndex(name = "entityType_ruleId_idx", def = "{'entityType': 1, 'ruleId': 1}", unique = true),
    @CompoundIndex(name = "entityType_field_idx", def = "{'entityType': 1, 'field': 1}")
})
public class SimpleValidationRule {
    @Id
    private String id;
    
    @Indexed
    @Field("rule_id")
    private String ruleId;
    
    private String code;
    private String name;
    private String description;
    
    @Indexed
    @Field("entity_type")
    private String entityType; // PRODUCT, CATEGORY, FEATURE, etc.
    
    @Field("field")
    private String field; // The attribute this rule validates
    
    @Field("validation_type")
    private String validationType; // The validation type (e.g., NOT_NULL, REGEX, MIN_LENGTH, etc.)
    
    @Field("condition_value")
    private String conditionValue; // The value used in the condition (e.g., regex pattern, min length, etc.)
    
    @Field("rule_expression")
    private String ruleExpression;
    
    private String message; // The message to display if validation fails
    
    private String severity; // ERROR, WARNING, INFO
    
    @Field("priority")
    @Builder.Default
    private Integer priority = 1; // Higher numbers = higher priority
    
    @Field("is_active") 
    @Builder.Default
    private Boolean active = true;
    
    @Field("category_id")
    private String categoryId;
    
    @Field("feature_id")
    private String featureId;
    
    @Field("validation_pattern")
    private String validationPattern;
    
    @Field("min_value")
    private String minValue;
    
    @Field("max_value")
    private String maxValue;
    
    @Field("min_length")
    private Integer minLength;
    
    @Field("max_length")
    private Integer maxLength;
    
    @Field("categories")
    @Builder.Default
    private List<String> categories = new ArrayList<>(); // Business categories this rule applies to
    
    @Field("allowed_values")
    @Builder.Default
    private List<String> allowedValues = new ArrayList<>();
    
    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;
    
    @Field("created_by")
    private String createdBy;
    
    @Field("updated_by")
    private String updatedBy;
    
    @Field("metadata")
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();
    
    /**
     * Add a category to this rule.
     * 
     * @param category the category to add
     * @return this rule for chaining
     */
    public SimpleValidationRule addCategory(String category) {
        if (categories == null) {
            categories = new ArrayList<>();
        }
        categories.add(category);
        return this;
    }
    
    /**
     * Enum defining the different types of validation rules
     */
    public enum RuleType {
        REQUIRED,
        PATTERN,
        LENGTH,
        RANGE,
        TYPE,
        DEPENDENCY,
        CUSTOM,
        ALLOWED_VALUES
    }
    
    /**
     * Creates a required field validation rule
     * 
     * @param code The rule code
     * @param name The rule name
     * @return A new validation rule
     */
    public static SimpleValidationRule createRequiredRule(String code, String name) {
        return SimpleValidationRule.builder()
                .code(code)
                .name(name)
                .description("Field is required")
                .validationType(RuleType.REQUIRED.toString())
                .ruleExpression("")
                .priority(1)
                .active(true)
                .build();
    }
    
    /**
     * Creates a pattern validation rule
     * 
     * @param code The rule code
     * @param name The rule name
     * @param pattern The validation pattern (regex)
     * @return A new validation rule
     */
    public static SimpleValidationRule createPatternRule(String code, String name, String pattern) {
        return SimpleValidationRule.builder()
                .code(code)
                .name(name)
                .description("Value must match pattern")
                .validationType(RuleType.PATTERN.toString())
                .ruleExpression(pattern)
                .validationPattern(pattern)
                .conditionValue(pattern)
                .priority(1)
                .active(true)
                .build();
    }
    
    /**
     * Creates a range validation rule
     * 
     * @param code The rule code
     * @param name The rule name
     * @param min The minimum value
     * @param max The maximum value
     * @return A new validation rule
     */
    public static SimpleValidationRule createRangeRule(String code, String name, String min, String max) {
        return SimpleValidationRule.builder()
                .code(code)
                .name(name)
                .description("Value must be within range")
                .validationType(RuleType.RANGE.toString())
                .ruleExpression(String.format("%s,%s", min, max))
                .conditionValue(String.format("%s,%s", min, max))
                .minValue(min)
                .maxValue(max)
                .priority(1)
                .active(true)
                .build();
    }
    
    /**
     * Creates an allowed values validation rule
     * 
     * @param code The rule code
     * @param name The rule name
     * @param allowedValuesString Comma-separated allowed values
     * @return A new validation rule
     */
    public static SimpleValidationRule createAllowedValuesRule(String code, String name, String allowedValuesString) {
        SimpleValidationRule rule = SimpleValidationRule.builder()
                .code(code)
                .name(name)
                .description("Value must be one of allowed values")
                .validationType(RuleType.ALLOWED_VALUES.toString())
                .ruleExpression(allowedValuesString)
                .conditionValue(allowedValuesString)
                .priority(1)
                .active(true)
                .build();
        
        // Parse allowed values
        if (allowedValuesString != null && !allowedValuesString.isEmpty()) {
            String[] values = allowedValuesString.split(",");
            for (String value : values) {
                rule.getAllowedValues().add(value.trim());
            }
        }
        
        return rule;
    }
    
    /**
     * Creates a type validation rule
     * 
     * @param code The rule code
     * @param name The rule name
     * @param valueType The expected value type
     * @return A new validation rule
     */
    public static SimpleValidationRule createTypeRule(String code, String name, String valueType) {
        return SimpleValidationRule.builder()
                .code(code)
                .name(name)
                .description("Value must be of the correct type")
                .validationType(RuleType.TYPE.toString())
                .ruleExpression(valueType)
                .conditionValue(valueType)
                .priority(1)
                .active(true)
                .build();
    }
    
    /**
     * Creates a length validation rule
     * 
     * @param code The rule code
     * @param name The rule name
     * @param minLength The minimum length
     * @param maxLength The maximum length
     * @return A new validation rule
     */
    public static SimpleValidationRule createLengthRule(String code, String name, Integer minLength, Integer maxLength) {
        return SimpleValidationRule.builder()
                .code(code)
                .name(name)
                .description("Value must be within valid length")
                .validationType(RuleType.LENGTH.toString())
                .ruleExpression(String.format("%d,%d", minLength, maxLength))
                .conditionValue(String.format("%d,%d", minLength, maxLength))
                .minLength(minLength)
                .maxLength(maxLength)
                .priority(1)
                .active(true)
                .build();
    }
}