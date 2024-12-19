package com.example.validation;

import com.example.enums.FeatureValueType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.regex.Pattern;

public class FeatureValueValidator implements ConstraintValidator<ValidFeatureValue, Object> {
    private FeatureValueType valueType;
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^-?\\d*\\.?\\d+$");
    private static final Pattern BOOLEAN_PATTERN = Pattern.compile("^(true|false)$", Pattern.CASE_INSENSITIVE);

    @Override
    public void initialize(ValidFeatureValue constraintAnnotation) {
        this.valueType = FeatureValueType.valueOf(constraintAnnotation.valueType());
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // null values should be handled by @NotNull if required
        }

        if (value instanceof List<?>) {
            return validateList((List<?>) value);
        }

        return validateSingleValue(value);
    }

    private boolean validateList(List<?> values) {
        switch (valueType) {
            case STRING_LIST:
                return values.stream().allMatch(v -> v instanceof String);
            case NUMERIC_LIST:
                return values.stream()
                    .map(String::valueOf)
                    .allMatch(v -> NUMERIC_PATTERN.matcher(v).matches());
            case BOOLEAN_LIST:
                return values.stream()
                    .map(String::valueOf)
                    .allMatch(v -> BOOLEAN_PATTERN.matcher(v).matches());
            default:
                return false;
        }
    }

    private boolean validateSingleValue(Object value) {
        String stringValue = String.valueOf(value);
        switch (valueType) {
            case STRING:
                return value instanceof String;
            case NUMERIC:
                return NUMERIC_PATTERN.matcher(stringValue).matches();
            case BOOLEAN:
                return BOOLEAN_PATTERN.matcher(stringValue).matches();
            default:
                return false;
        }
    }
}
