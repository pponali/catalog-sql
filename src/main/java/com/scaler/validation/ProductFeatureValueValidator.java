package com.scaler.validation;

import com.scaler.entity.ProductFeatureValue;
import com.scaler.entity.CategoryFeatureTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Iterator;
import java.util.regex.Pattern;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProductFeatureValueValidator.Validator.class)
public @interface ProductFeatureValueValidator {
    String message() default "Invalid feature value";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    class Validator implements ConstraintValidator<ProductFeatureValueValidator, ProductFeatureValue> {
        private final ObjectMapper objectMapper;

        public Validator(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public boolean isValid(ProductFeatureValue value, ConstraintValidatorContext context) {
            if (value == null || value.getFeature() == null || value.getFeature().getTemplate() == null) {
                return true; // Let other validations handle this
            }

            CategoryFeatureTemplate template = value.getFeature().getTemplate();
            JsonNode validationMetadata;
            try {
                if (template.getMetadata() != null) {
                    JsonNode metadata = objectMapper.readTree(template.getMetadata().toString());
                    validationMetadata = metadata.get("validation");
                } else {
                    validationMetadata = objectMapper.createObjectNode();
                }
            } catch (Exception e) {
                context.buildConstraintViolationWithTemplate(
                    "Invalid metadata format: " + e.getMessage())
                    .addConstraintViolation();
                return false;
            }

            switch (template.getAttributeType()) {
                case "STRING":
                    return validateString(value, validationMetadata, context);
                case "NUMERIC":
                    return validateNumeric(value, validationMetadata, context);
                case "JSON":
                    return validateJson(value, validationMetadata, context);
                default:
                    return true;
            }
        }

        private boolean validateString(ProductFeatureValue value, JsonNode validationMetadata, 
                                    ConstraintValidatorContext context) {
            if (!validationMetadata.has("pattern") || value.getStringValue() == null) {
                return true;
            }

            String pattern = validationMetadata.get("pattern").asText();
            if (!Pattern.matches(pattern, value.getStringValue())) {
                context.buildConstraintViolationWithTemplate(
                    "String value does not match required pattern: " + pattern)
                    .addConstraintViolation();
                return false;
            }
            return true;
        }

        private boolean validateNumeric(ProductFeatureValue value, JsonNode validationMetadata,
                                     ConstraintValidatorContext context) {
            if (value.getNumericValue() == null) {
                return true;
            }

            if (validationMetadata.has("min")) {
                double min = validationMetadata.get("min").asDouble();
                if (value.getNumericValue().doubleValue() < min) {
                    context.buildConstraintViolationWithTemplate(
                        "Value below minimum: " + min)
                        .addConstraintViolation();
                    return false;
                }
            }

            if (validationMetadata.has("max")) {
                double max = validationMetadata.get("max").asDouble();
                if (value.getNumericValue().doubleValue() > max) {
                    context.buildConstraintViolationWithTemplate(
                        "Value above maximum: " + max)
                        .addConstraintViolation();
                    return false;
                }
            }

            return true;
        }

        private boolean validateJson(ProductFeatureValue value, JsonNode validationMetadata,
                                  ConstraintValidatorContext context) {
            if (value.getAttributeValue() == null) {
                return true;
            }

            try {
                JsonNode attributeValue = objectMapper.readTree(value.getAttributeValue().toString());

                // Validate required fields
                if (validationMetadata.has("required_fields")) {
                    for (JsonNode field : validationMetadata.get("required_fields")) {
                        if (!attributeValue.has(field.asText())) {
                            context.buildConstraintViolationWithTemplate(
                                "Missing required field in JSON: " + field.asText())
                                .addConstraintViolation();
                            return false;
                        }
                    }
                }

                // Validate enum values
                if (validationMetadata.has("enum_values")) {
                    JsonNode enumValues = validationMetadata.get("enum_values");
                    Iterator<String> fieldNames = attributeValue.fieldNames();
                    while (fieldNames.hasNext()) {
                        String fieldName = fieldNames.next();
                        if (enumValues.has(fieldName)) {
                            String fieldValue = attributeValue.get(fieldName).asText();
                            boolean validEnum = false;
                            for (JsonNode enumValue : enumValues.get(fieldName)) {
                                if (enumValue.asText().equals(fieldValue)) {
                                    validEnum = true;
                                    break;
                                }
                            }
                            if (!validEnum) {
                                context.buildConstraintViolationWithTemplate(
                                    "Invalid enum value for field " + fieldName + ": " + fieldValue)
                                    .addConstraintViolation();
                                return false;
                            }
                        }
                    }
                }

                return true;
            } catch (Exception e) {
                context.buildConstraintViolationWithTemplate(
                    "Invalid JSON format: " + e.getMessage())
                    .addConstraintViolation();
                return false;
            }
        }
    }
}
