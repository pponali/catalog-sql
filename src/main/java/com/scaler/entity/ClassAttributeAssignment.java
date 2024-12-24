package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Entity
@Table(name = "class_attribute_assignments")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ClassAttributeAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification_class_id")
    private ClassificationClass classificationClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification_attribute_id")
    private ClassificationAttribute classificationAttribute;

    @Column(name = "unit")
    private String unit;

    @Column(name = "attribute_type", nullable = false)
    private String attributeType;

    @Column(name = "mandatory")
    private boolean mandatory;

    @Column(name = "multi_valued")
    private boolean multiValued;

    @ElementCollection
    @CollectionTable(
        name = "class_attribute_assignment_values",
        joinColumns = @JoinColumn(name = "assignment_id")
    )
    @Column(name = "attribute_value")
    @Builder.Default
    private Set<String> attributeValues = new HashSet<>();

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "visible")
    @Builder.Default
    private boolean visible = true;

    @Column(name = "editable")
    @Builder.Default
    private boolean editable = true;

    @Column(name = "searchable")
    @Builder.Default
    private boolean searchable = true;

    @Column(name = "comparable")
    @Builder.Default
    private boolean comparable = true;

    @Column(name = "range_min")
    private Double rangeMin;

    @Column(name = "range_max")
    private Double rangeMax;

    @Column(name = "regex_validation")
    private String regexValidation;

    @Column(name = "default_value")
    @Builder.Default
    private String defaultValue = "";

    @ElementCollection
    @CollectionTable(
        name = "class_attribute_assignment_dependencies",
        joinColumns = @JoinColumn(name = "assignment_id")
    )
    @MapKeyJoinColumn(name = "dependent_assignment_id")
    @Column(name = "required_value")
    private Map<ClassAttributeAssignment, String> dependencies = new HashMap<>();

    @ElementCollection
    @CollectionTable(
        name = "class_attribute_assignment_metadata",
        joinColumns = @JoinColumn(name = "assignment_id")
    )
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    private Map<String, String> metadata = new HashMap<>();

    @PrePersist
    @PreUpdate
    private void validateAssignment() {
        validateAttributeType();
        validateRangeValues();
        validateRegexPattern();
        validateDefaultValue();
        validateDependencies();
    }

    private void validateAttributeType() {
        if (attributeType == null) {
            throw new IllegalStateException("Attribute type must not be null");
        }
        if (!attributeType.matches("(?i)(string|numeric|boolean|enum|date|currency|dimension|weight|reference)")) {
            throw new IllegalStateException("Invalid attribute type: " + attributeType);
        }
        if ("enum".equalsIgnoreCase(attributeType) && (attributeValues == null || attributeValues.isEmpty())) {
            throw new IllegalStateException("Enum attribute type must have attribute values");
        }
    }

    private void validateRangeValues() {
        if (rangeMin != null && rangeMax != null && rangeMin > rangeMax) {
            throw new IllegalStateException("Range minimum cannot be greater than range maximum");
        }
    }

    private void validateRegexPattern() {
        if (regexValidation != null) {
            try {
                java.util.regex.Pattern.compile(regexValidation);
            } catch (java.util.regex.PatternSyntaxException e) {
                throw new IllegalStateException("Invalid regex pattern: " + regexValidation);
            }
        }
    }

    private void validateDefaultValue() {
        if (defaultValue != null) {
            if (!isValidValue(defaultValue)) {
                throw new IllegalStateException("Default value does not match attribute type or constraints");
            }
        }
    }

    private void validateDependencies() {
        for (Map.Entry<ClassAttributeAssignment, String> entry : dependencies.entrySet()) {
            ClassAttributeAssignment dependent = entry.getKey();
            String requiredValue = entry.getValue();
            
            if (!dependent.getClassificationClass().equals(this.classificationClass)) {
                throw new IllegalStateException("Dependent attribute must belong to the same classification class");
            }
            
            if (!dependent.isValidValue(requiredValue)) {
                throw new IllegalStateException("Required value is not valid for dependent attribute");
            }
        }
    }

    public boolean isValidValue(String value) {
        if (value == null) {
            return !mandatory;
        }

        try {
            // Basic type validation
            boolean typeValid = switch (attributeType.toLowerCase()) {
                case "string" -> true;
                case "numeric" -> value.matches("^-?\\d*\\.?\\d+$");
                case "boolean" -> value.matches("^(true|false)$");
                case "enum" -> attributeValues.contains(value);
                case "date" -> value.matches("^\\d{4}-\\d{2}-\\d{2}$");
                case "currency" -> value.matches("^\\d+\\.?\\d{0,2}$");
                case "dimension", "weight" -> value.matches("^\\d+\\.?\\d*$");
                case "reference" -> value.matches("^\\d+$");
                default -> false;
            };

            if (!typeValid) return false;

            // Range validation for numeric types
            if (("numeric".equalsIgnoreCase(attributeType) || 
                 "currency".equalsIgnoreCase(attributeType) ||
                 "dimension".equalsIgnoreCase(attributeType) ||
                 "weight".equalsIgnoreCase(attributeType)) && 
                (rangeMin != null || rangeMax != null)) {
                
                double numericValue = Double.parseDouble(value);
                if (rangeMin != null && numericValue < rangeMin) return false;
                if (rangeMax != null && numericValue > rangeMax) return false;
            }

            // Regex validation
            if (regexValidation != null && !value.matches(regexValidation)) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isValidValues(Set<String> values) {
        if (values == null || values.isEmpty()) {
            return !mandatory;
        }

        if (!multiValued && values.size() > 1) {
            return false;
        }

        return values.stream().allMatch(this::isValidValue);
    }

    public boolean areDependenciesSatisfied(Map<ClassAttributeAssignment, String> currentValues) {
        return dependencies.entrySet().stream().allMatch(entry -> {
            String currentValue = currentValues.get(entry.getKey());
            return currentValue != null && currentValue.equals(entry.getValue());
        });
    }
}
