package com.example.validation;

import com.example.entity.CategoryFeatureTemplate;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoryFeatureTemplateValidator.Validator.class)
public @interface CategoryFeatureTemplateValidator {
    String message() default "Invalid feature template configuration";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Component
    class Validator implements ConstraintValidator<CategoryFeatureTemplateValidator, CategoryFeatureTemplate> {
        private static final Set<String> VALID_ATTRIBUTE_TYPES = new HashSet<>(
            Arrays.asList("STRING", "NUMERIC", "BOOLEAN", "JSON")
        );

        private final ObjectMapper objectMapper;

        public Validator(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public boolean isValid(CategoryFeatureTemplate template, ConstraintValidatorContext context) {
            if (template == null) {
                return true; // Let other validations handle this
            }

            // Validate attribute type
            if (!VALID_ATTRIBUTE_TYPES.contains(template.getAttributeType())) {
                context.buildConstraintViolationWithTemplate(
                    "Invalid attribute_type: " + template.getAttributeType())
                    .addConstraintViolation();
                return false;
            }

            // Validate metadata
            if (template.getMetadata() != null) {
                try {
                    JsonNode metadata = objectMapper.readTree(template.getMetadata().toString());
                    if (!metadata.isObject()) {
                        context.buildConstraintViolationWithTemplate(
                            "Metadata must be a JSON object")
                            .addConstraintViolation();
                        return false;
                    }

                    // Validate validation rules
                    if (metadata.has("validation")) {
                        JsonNode validation = metadata.get("validation");
                        
                        switch (template.getAttributeType()) {
                            case "STRING":
                                if (validation.has("pattern")) {
                                    try {
                                        Pattern.compile(validation.get("pattern").asText());
                                    } catch (PatternSyntaxException e) {
                                        context.buildConstraintViolationWithTemplate(
                                            "Invalid pattern syntax: " + e.getMessage())
                                            .addConstraintViolation();
                                        return false;
                                    }
                                }
                                break;

                            case "NUMERIC":
                                if (validation.has("min")) {
                                    try {
                                        Double.parseDouble(validation.get("min").asText());
                                    } catch (NumberFormatException e) {
                                        context.buildConstraintViolationWithTemplate(
                                            "Invalid minimum value format")
                                            .addConstraintViolation();
                                        return false;
                                    }
                                }
                                if (validation.has("max")) {
                                    try {
                                        Double.parseDouble(validation.get("max").asText());
                                    } catch (NumberFormatException e) {
                                        context.buildConstraintViolationWithTemplate(
                                            "Invalid maximum value format")
                                            .addConstraintViolation();
                                        return false;
                                    }
                                }
                                break;
                        }
                    }
                } catch (Exception e) {
                    context.buildConstraintViolationWithTemplate(
                        "Invalid metadata format: " + e.getMessage())
                        .addConstraintViolation();
                    return false;
                }
            }

            return true;
        }
    }
}
