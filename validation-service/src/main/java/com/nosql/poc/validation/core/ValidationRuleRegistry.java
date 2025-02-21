package com.nosql.poc.validation.core;

import com.nosql.poc.validation.rules.ValidationRule;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ValidationRuleRegistry {
    
    @Autowired
    private ApplicationContext applicationContext;
    
    private final Map<Class<?>, List<ValidationRule>> rulesByTarget = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void init() {
        // Get all ValidationRule beans from Spring context
        Map<String, ValidationRule> ruleBeans = applicationContext.getBeansOfType(ValidationRule.class);
        
        // Group rules by target class
        ruleBeans.values().forEach(rule -> {
            Class<?> targetClass = rule.getTargetClass();
            rulesByTarget.computeIfAbsent(targetClass, k -> new ArrayList<>()).add(rule);
        });
        
        // Sort rules by priority
        rulesByTarget.values().forEach(rules -> 
            rules.sort(Comparator.comparingInt(ValidationRule::getPriority))
        );
    }
    
    public List<ValidationRule> getRulesForTarget(Class<?> targetClass) {
        List<ValidationRule> rules = new ArrayList<>();
        
        // Get rules for the exact class
        rules.addAll(rulesByTarget.getOrDefault(targetClass, Collections.emptyList()));
        
        // Get rules for interfaces
        Arrays.stream(targetClass.getInterfaces())
            .forEach(iface -> rules.addAll(rulesByTarget.getOrDefault(iface, Collections.emptyList())));
        
        // Get rules for superclasses
        Class<?> superClass = targetClass.getSuperclass();
        while (superClass != null) {
            rules.addAll(rulesByTarget.getOrDefault(superClass, Collections.emptyList()));
            superClass = superClass.getSuperclass();
        }
        
        return rules;
    }
    
    public void registerRule(ValidationRule rule) {
        rulesByTarget.computeIfAbsent(rule.getTargetClass(), k -> new ArrayList<>()).add(rule);
        rulesByTarget.get(rule.getTargetClass()).sort(Comparator.comparingInt(ValidationRule::getPriority));
    }
}
