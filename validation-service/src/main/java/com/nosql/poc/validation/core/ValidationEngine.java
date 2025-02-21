package com.nosql.poc.validation.core;

import com.nosql.poc.validation.model.ValidationContext;
import com.nosql.poc.validation.model.ValidationResult;
import com.nosql.poc.validation.rules.ValidationRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ValidationEngine {
    
    private final ValidationRuleRegistry ruleRegistry;
    
    public ValidationResult validate(Object target, ValidationContext context) {
        List<ValidationRule> rules = ruleRegistry.getRulesForTarget(target.getClass());
        
        ValidationResult result = new ValidationResult();
        
        // Execute synchronous validations
        List<ValidationRule> syncRules = rules.stream()
            .filter(rule -> !rule.isAsync())
            .collect(Collectors.toList());
            
        syncRules.forEach(rule -> {
            try {
                rule.validate(target, context, result);
            } catch (Exception e) {
                result.addError(rule.getName(), "Validation failed: " + e.getMessage());
            }
        });
        
        // Execute asynchronous validations
        List<ValidationRule> asyncRules = rules.stream()
            .filter(ValidationRule::isAsync)
            .collect(Collectors.toList());
            
        List<CompletableFuture<Void>> futures = asyncRules.stream()
            .map(rule -> CompletableFuture.runAsync(() -> {
                try {
                    rule.validate(target, context, result);
                } catch (Exception e) {
                    result.addError(rule.getName(), "Async validation failed: " + e.getMessage());
                }
            }))
            .collect(Collectors.toList());
            
        // Wait for all async validations to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        return result;
    }
}
