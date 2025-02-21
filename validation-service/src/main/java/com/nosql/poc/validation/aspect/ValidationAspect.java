package com.nosql.poc.validation.aspect;

import com.nosql.poc.validation.annotation.Validate;
import com.nosql.poc.validation.core.ValidationEngine;
import com.nosql.poc.validation.model.ValidationContext;
import com.nosql.poc.validation.model.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ValidationAspect {
    
    private final ValidationEngine validationEngine;
    
    @Around("@annotation(validate)")
    public Object validateMethod(ProceedingJoinPoint joinPoint, Validate validate) throws Throwable {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        
        // Create validation context
        ValidationContext context = ValidationContext.builder()
            .validationType(validate.type())
            .channelId(validate.channelId())
            .build();
            
        // Validate method arguments
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null && validate.validateParams()[i]) {
                ValidationResult result = validationEngine.validate(args[i], context);
                if (!result.isValid()) {
                    throw new ValidationException("Validation failed for argument " + signature.getParameterNames()[i], result);
                }
            }
        }
        
        // Execute method
        Object result = joinPoint.proceed();
        
        // Validate return value if required
        if (validate.validateReturn() && result != null) {
            ValidationResult validationResult = validationEngine.validate(result, context);
            if (!validationResult.isValid()) {
                throw new ValidationException("Validation failed for return value", validationResult);
            }
        }
        
        return result;
    }
}
