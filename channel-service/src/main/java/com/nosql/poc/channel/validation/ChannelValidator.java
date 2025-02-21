package com.nosql.poc.channel.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChannelValidator {
    
    private final Validator validator;
    
    public ValidationResult validate(Object object) {
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        
        ValidationResult result = new ValidationResult();
        if (!violations.isEmpty()) {
            result.setValid(false);
            result.setViolations(violations.stream()
                .map(violation -> new ValidationError(
                    violation.getPropertyPath().toString(),
                    violation.getMessage(),
                    violation.getInvalidValue() != null ? violation.getInvalidValue().toString() : "null"
                ))
                .collect(Collectors.toList()));
        }
        return result;
    }
}
