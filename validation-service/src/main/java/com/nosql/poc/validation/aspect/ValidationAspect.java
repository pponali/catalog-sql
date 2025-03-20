package com.nosql.poc.validation.aspect;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nosql.poc.validation.annotation.Validate;
import com.nosql.poc.validation.core.ValidationEngine;
import com.nosql.poc.validation.model.ValidationContext;
import com.nosql.poc.validation.model.ValidationResult;
import com.nosql.poc.validation.repository.ValidationRuleRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * Aspect that provides validation functionality through annotations.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class ValidationAspect {
    
    private final ValidationEngine validationEngine;
    private final ValidationRuleRepository validationRuleRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Validates method arguments and return values.
     *
     * @param joinPoint the join point
     * @param validate the validate annotation
     * @return the result of the method invocation
     * @throws Throwable if validation fails or method invocation throws an exception
     */
    @Around("@annotation(validate)")
    public Object validateMethod(ProceedingJoinPoint joinPoint, Validate validate) throws Throwable {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        
        // Validate method arguments
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null && validate.validateParams().length > i && validate.validateParams()[i]) {
                ValidationResult result = validateObject(args[i], validate.type());
                if (!result.isValid()) {
                    throw new ValidationException("Validation failed for argument " + signature.getParameterNames()[i], result);
                }
            }
        }
        
        // Execute method
        Object result = joinPoint.proceed();
        
        // Validate return value if required
        if (validate.validateReturn() && result != null) {
            ValidationResult validationResult = validateObject(result, validate.type());
            if (!validationResult.isValid()) {
                throw new ValidationException("Validation failed for return value", validationResult);
            }
        }
        
        return result;
    }
    
    /**
     * Validates an object against rules for its type.
     *
     * @param object the object to validate
     * @param entityType the entity type
     * @return the validation result
     */
    private ValidationResult validateObject(Object object, String entityType) {
        try {
            // Convert object to JsonNode
            JsonNode jsonNode = objectMapper.valueToTree(object);
            
            // Create validation context
            ValidationContext context = new ValidationContext();
            context.setEntityType(entityType);
            context.setEntity(jsonNode);
            context.setRules(validationRuleRepository.findByEntityTypeAndActive(entityType, true));
            
            // Validate
            return validationEngine.validate(context);
        } catch (Exception e) {
            ValidationResult result = new ValidationResult();
            result.setValid(false);
            result.setEntityType(entityType);
            result.getErrorMessages().add("Error during validation: " + e.getMessage());
            return result;
        }
    }
}