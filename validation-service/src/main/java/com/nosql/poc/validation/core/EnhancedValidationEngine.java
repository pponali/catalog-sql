package com.nosql.poc.validation.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nosql.poc.validation.model.EnhancedValidationRule;
import com.nosql.poc.validation.model.RuleConditionType;
import com.nosql.poc.validation.model.ValidationContext;
import com.nosql.poc.validation.model.ValidationResult;
import com.nosql.poc.validation.model.ValidationSeverity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * Enhanced validation engine with support for complex business rules.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EnhancedValidationEngine {

    private final ExpressionParser expressionParser = new SpelExpressionParser();
    
    /**
     * Validates an entity against a set of rules.
     *
     * @param context the validation context containing the entity and rules
     * @return a validation result with details about validation status
     */
    public ValidationResult validate(ValidationContext context) {
        log.debug("Validating {} entity with enhanced validation engine", context.getEntityType());
        
        ValidationResult result = new ValidationResult();
        result.setEntityType(context.getEntityType());
        result.setValid(true);
        
        // Get rules that this entity should be validated against
        List<EnhancedValidationRule> rules = context.getEnhancedRules();
        
        // Sort rules by priority (higher priority first) and handle dependencies
        rules = sortRulesByPriorityAndDependencies(rules);
        
        // Apply each rule to the entity
        for (EnhancedValidationRule rule : rules) {
            if (Boolean.TRUE.equals(rule.getActive())) {
                validateRule(rule, context.getEntity(), result);
                
                // If validation failed with an ERROR and rule execution should stop on error, break
                if (!result.isValid() && rule.getSeverity() == ValidationSeverity.ERROR) {
                    break;
                }
            }
        }
        
        return result;
    }
    
    /**
     * Sort rules by priority and handle dependencies.
     *
     * @param rules the rules to sort
     * @return sorted rules
     */
    private List<EnhancedValidationRule> sortRulesByPriorityAndDependencies(List<EnhancedValidationRule> rules) {
        // Create a map of rule IDs to rules
        Map<String, EnhancedValidationRule> ruleMap = rules.stream()
                .collect(Collectors.toMap(r -> r.getRuleId(), r -> r));
        
        // Create a dependency graph
        Map<String, Set<String>> dependencyGraph = new HashMap<>();
        for (EnhancedValidationRule rule : rules) {
            dependencyGraph.put(rule.getRuleId(), new HashSet<>());
            if (rule.getDependsOn() != null) {
                for (String dependsOn : rule.getDependsOn()) {
                    dependencyGraph.get(rule.getRuleId()).add(dependsOn);
                }
            }
        }
        
        // Perform topological sort
        List<String> sortedRuleIds = topologicalSort(dependencyGraph);
        
        // Convert back to rules and sort by priority
        List<EnhancedValidationRule> sortedRules = sortedRuleIds.stream()
                .map(id -> ruleMap.get(id))
                .filter(Objects::nonNull)
                .sorted((r1, r2) -> r2.getPriority().compareTo(r1.getPriority()))
                .collect(Collectors.toList());
        
        return sortedRules;
    }
    
    /**
     * Perform a topological sort on a dependency graph.
     *
     * @param graph the dependency graph
     * @return sorted nodes
     */
    private List<String> topologicalSort(Map<String, Set<String>> graph) {
        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> temp = new HashSet<>();
        
        // Process each node
        for (String node : graph.keySet()) {
            if (!visited.contains(node)) {
                topologicalSortUtil(node, visited, temp, graph, result);
            }
        }
        
        Collections.reverse(result);
        return result;
    }
    
    /**
     * Utility method for topological sort.
     *
     * @param node the current node
     * @param visited set of visited nodes
     * @param temp set of nodes in the current recursion stack
     * @param graph the dependency graph
     * @param result the result list
     */
    private void topologicalSortUtil(String node, Set<String> visited, Set<String> temp, 
            Map<String, Set<String>> graph, List<String> result) {
        
        // Mark the current node as temporarily visited
        temp.add(node);
        
        // Visit all the neighbors
        if (graph.containsKey(node)) {
            for (String neighbor : graph.get(node)) {
                if (temp.contains(neighbor)) {
                    // Cyclic dependency detected
                    log.warn("Cyclic dependency detected in validation rules: {} -> {}", node, neighbor);
                    continue;
                }
                
                if (!visited.contains(neighbor)) {
                    topologicalSortUtil(neighbor, visited, temp, graph, result);
                }
            }
        }
        
        // Mark the current node as permanently visited
        temp.remove(node);
        visited.add(node);
        result.add(node);
    }
    
    /**
     * Applies a single validation rule to an entity.
     *
     * @param rule the validation rule to apply
     * @param entity the entity to validate
     * @param result the validation result to update
     */
    private void validateRule(EnhancedValidationRule rule, JsonNode entity, ValidationResult result) {
        log.debug("Applying enhanced rule: {}", rule.getName());
        
        boolean isValid = true;
        
        try {
            switch (rule.getConditionType()) {
                case NOT_NULL:
                    isValid = validateNotNull(rule, entity);
                    break;
                    
                case NOT_EMPTY:
                    isValid = validateNotEmpty(rule, entity);
                    break;
                    
                case REGEX:
                    isValid = validateRegex(rule, entity);
                    break;
                    
                case MIN_LENGTH:
                    isValid = validateMinLength(rule, entity);
                    break;
                    
                case MAX_LENGTH:
                    isValid = validateMaxLength(rule, entity);
                    break;
                    
                case MIN_VALUE:
                    isValid = validateMinValue(rule, entity);
                    break;
                    
                case MAX_VALUE:
                    isValid = validateMaxValue(rule, entity);
                    break;
                    
                case ENUM:
                    isValid = validateEnum(rule, entity);
                    break;
                    
                case CONDITIONAL:
                    isValid = validateConditional(rule, entity);
                    break;
                    
                case CROSS_FIELD:
                    isValid = validateCrossField(rule, entity);
                    break;
                    
                case CALCULATED:
                    isValid = validateCalculated(rule, entity);
                    break;
                    
                case DEPENDENT:
                    isValid = validateDependent(rule, entity);
                    break;
                    
                case COMPOSITE:
                    isValid = validateComposite(rule, entity);
                    break;
                    
                default:
                    log.warn("Unknown condition type: {}", rule.getConditionType());
                    isValid = false;
                    break;
            }
        } catch (Exception e) {
            log.error("Error applying rule: {}", rule.getName(), e);
            isValid = false;
        }
        
        // Update the validation result
        if (!isValid) {
            if (rule.getSeverity() == ValidationSeverity.ERROR) {
                result.setValid(false);
                result.getErrorMessages().add(rule.getMessage());
                result.addError(rule.getName(), rule.getMessage());
            } else if (rule.getSeverity() == ValidationSeverity.WARNING) {
                result.getWarningMessages().add(rule.getMessage());
                result.addWarning(rule.getName(), rule.getMessage());
            } else if (rule.getSeverity() == ValidationSeverity.INFO) {
                result.getInfoMessages().add(rule.getMessage());
                result.addInfo(rule.getName(), rule.getMessage());
            }
        }
    }
    
    /**
     * Validates that an attribute is not null.
     *
     * @param rule the validation rule
     * @param entity the entity to validate
     * @return true if valid, false otherwise
     */
    private boolean validateNotNull(EnhancedValidationRule rule, JsonNode entity) {
        JsonNode attributeNode = entity.get(rule.getPrimaryAttribute());
        return attributeNode != null && !attributeNode.isNull();
    }
    
    /**
     * Validates that an attribute is not empty.
     *
     * @param rule the validation rule
     * @param entity the entity to validate
     * @return true if valid, false otherwise
     */
    private boolean validateNotEmpty(EnhancedValidationRule rule, JsonNode entity) {
        JsonNode attributeNode = entity.get(rule.getPrimaryAttribute());
        
        if (attributeNode == null || attributeNode.isNull()) {
            return false;
        }
        
        if (attributeNode.isTextual()) {
            return !StringUtils.isEmpty(attributeNode.asText());
        } else if (attributeNode.isArray()) {
            return attributeNode.size() > 0;
        } else if (attributeNode.isObject()) {
            return attributeNode.size() > 0;
        }
        
        return true;
    }
    
    /**
     * Validates an attribute against a regular expression.
     *
     * @param rule the validation rule
     * @param entity the entity to validate
     * @return true if valid, false otherwise
     */
    private boolean validateRegex(EnhancedValidationRule rule, JsonNode entity) {
        JsonNode attributeNode = entity.get(rule.getPrimaryAttribute());
        
        if (attributeNode == null || attributeNode.isNull() || !attributeNode.isTextual()) {
            return false;
        }
        
        String value = attributeNode.asText();
        String regex = rule.getConditionValue();
        
        try {
            Pattern pattern = Pattern.compile(regex);
            return pattern.matcher(value).matches();
        } catch (PatternSyntaxException e) {
            log.error("Invalid regex pattern: {}", regex, e);
            return false;
        }
    }
    
    /**
     * Validates that an attribute has at least a minimum length.
     *
     * @param rule the validation rule
     * @param entity the entity to validate
     * @return true if valid, false otherwise
     */
    private boolean validateMinLength(EnhancedValidationRule rule, JsonNode entity) {
        JsonNode attributeNode = entity.get(rule.getPrimaryAttribute());
        
        if (attributeNode == null || attributeNode.isNull() || !attributeNode.isTextual()) {
            return false;
        }
        
        String value = attributeNode.asText();
        
        try {
            int minLength = Integer.parseInt(rule.getConditionValue());
            return value.length() >= minLength;
        } catch (NumberFormatException e) {
            log.error("Invalid min length: {}", rule.getConditionValue(), e);
            return false;
        }
    }
    
    /**
     * Validates that an attribute does not exceed a maximum length.
     *
     * @param rule the validation rule
     * @param entity the entity to validate
     * @return true if valid, false otherwise
     */
    private boolean validateMaxLength(EnhancedValidationRule rule, JsonNode entity) {
        JsonNode attributeNode = entity.get(rule.getPrimaryAttribute());
        
        if (attributeNode == null || attributeNode.isNull()) {
            return true; // Null is valid for max length validation
        }
        
        if (!attributeNode.isTextual()) {
            return false;
        }
        
        String value = attributeNode.asText();
        
        try {
            int maxLength = Integer.parseInt(rule.getConditionValue());
            return value.length() <= maxLength;
        } catch (NumberFormatException e) {
            log.error("Invalid max length: {}", rule.getConditionValue(), e);
            return false;
        }
    }
    
    /**
     * Validates that a numeric attribute is at least a minimum value.
     *
     * @param rule the validation rule
     * @param entity the entity to validate
     * @return true if valid, false otherwise
     */
    private boolean validateMinValue(EnhancedValidationRule rule, JsonNode entity) {
        JsonNode attributeNode = entity.get(rule.getPrimaryAttribute());
        
        if (attributeNode == null || attributeNode.isNull()) {
            return false;
        }
        
        if (!attributeNode.isNumber() && !attributeNode.isTextual()) {
            return false;
        }
        
        try {
            double actualValue = attributeNode.isNumber() ? 
                    attributeNode.asDouble() : Double.parseDouble(attributeNode.asText());
            double minValue = Double.parseDouble(rule.getConditionValue());
            return actualValue >= minValue;
        } catch (NumberFormatException e) {
            log.error("Invalid numeric value for min value validation: {}", attributeNode, e);
            return false;
        }
    }
    
    /**
     * Validates that a numeric attribute does not exceed a maximum value.
     *
     * @param rule the validation rule
     * @param entity the entity to validate
     * @return true if valid, false otherwise
     */
    private boolean validateMaxValue(EnhancedValidationRule rule, JsonNode entity) {
        JsonNode attributeNode = entity.get(rule.getPrimaryAttribute());
        
        if (attributeNode == null || attributeNode.isNull()) {
            return true; // Null is valid for max value validation
        }
        
        if (!attributeNode.isNumber() && !attributeNode.isTextual()) {
            return false;
        }
        
        try {
            double actualValue = attributeNode.isNumber() ? 
                    attributeNode.asDouble() : Double.parseDouble(attributeNode.asText());
            double maxValue = Double.parseDouble(rule.getConditionValue());
            return actualValue <= maxValue;
        } catch (NumberFormatException e) {
            log.error("Invalid numeric value for max value validation: {}", attributeNode, e);
            return false;
        }
    }
    
    /**
     * Validates that an attribute is one of a set of allowed values.
     *
     * @param rule the validation rule
     * @param entity the entity to validate
     * @return true if valid, false otherwise
     */
    private boolean validateEnum(EnhancedValidationRule rule, JsonNode entity) {
        JsonNode attributeNode = entity.get(rule.getPrimaryAttribute());
        
        if (attributeNode == null || attributeNode.isNull()) {
            return false;
        }
        
        if (!attributeNode.isTextual() && !attributeNode.isNumber()) {
            return false;
        }
        
        String value = attributeNode.asText();
        String allowedValuesStr = rule.getConditionValue();
        
        String[] allowedValues = allowedValuesStr.split(",");
        for (String allowedValue : allowedValues) {
            if (value.equals(allowedValue.trim())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Validates an entity using conditional expressions.
     *
     * @param rule the validation rule
     * @param entity the entity to validate
     * @return true if valid, false otherwise
     */
    private boolean validateConditional(EnhancedValidationRule rule, JsonNode entity) {
        // Parse the condition expression
        StandardEvaluationContext context = createEvaluationContext(entity);
        
        // Evaluate the condition
        Expression conditionExpression = expressionParser.parseExpression(rule.getConditionExpression());
        Boolean conditionResult = conditionExpression.getValue(context, Boolean.class);
        
        if (Boolean.TRUE.equals(conditionResult)) {
            // Get the then-expression
            String thenExpression = rule.getConditionalExpressions().get("then");
            if (thenExpression != null) {
                Expression thenExpr = expressionParser.parseExpression(thenExpression);
                return Boolean.TRUE.equals(thenExpr.getValue(context, Boolean.class));
            }
        } else {
            // Get the else-expression
            String elseExpression = rule.getConditionalExpressions().get("else");
            if (elseExpression != null) {
                Expression elseExpr = expressionParser.parseExpression(elseExpression);
                return Boolean.TRUE.equals(elseExpr.getValue(context, Boolean.class));
            }
        }
        
        return true;
    }
    
    /**
     * Validates an entity using cross-field validation.
     *
     * @param rule the validation rule
     * @param entity the entity to validate
     * @return true if valid, false otherwise
     */
    private boolean validateCrossField(EnhancedValidationRule rule, JsonNode entity) {
        StandardEvaluationContext context = createEvaluationContext(entity);
        Expression expression = expressionParser.parseExpression(rule.getConditionExpression());
        return Boolean.TRUE.equals(expression.getValue(context, Boolean.class));
    }
    
    /**
     * Validates an entity using a calculated field.
     *
     * @param rule the validation rule
     * @param entity the entity to validate
     * @return true if valid, false otherwise
     */
    private boolean validateCalculated(EnhancedValidationRule rule, JsonNode entity) {
        // Calculate the field value
        StandardEvaluationContext context = createEvaluationContext(entity);
        Expression calculationExpression = expressionParser.parseExpression(rule.getCalculationExpression());
        Object calculatedValue = calculationExpression.getValue(context);
        
        // Add the calculated value to the context
        context.setVariable("calculatedValue", calculatedValue);
        
        // Validate the calculated value
        Expression validationExpression = expressionParser.parseExpression(rule.getConditionExpression());
        return Boolean.TRUE.equals(validationExpression.getValue(context, Boolean.class));
    }
    
    /**
     * Validates an entity using dependent field validation.
     *
     * @param rule the validation rule
     * @param entity the entity to validate
     * @return true if valid, false otherwise
     */
    private boolean validateDependent(EnhancedValidationRule rule, JsonNode entity) {
        StandardEvaluationContext context = createEvaluationContext(entity);
        Expression expression = expressionParser.parseExpression(rule.getConditionExpression());
        return Boolean.TRUE.equals(expression.getValue(context, Boolean.class));
    }
    
    /**
     * Validates an entity using composite validation (AND/OR conditions).
     *
     * @param rule the validation rule
     * @param entity the entity to validate
     * @return true if valid, false otherwise
     */
    private boolean validateComposite(EnhancedValidationRule rule, JsonNode entity) {
        StandardEvaluationContext context = createEvaluationContext(entity);
        Expression expression = expressionParser.parseExpression(rule.getConditionExpression());
        return Boolean.TRUE.equals(expression.getValue(context, Boolean.class));
    }
    
    /**
     * Creates an evaluation context for Spring Expression Language (SpEL).
     *
     * @param entity the entity to validate
     * @return the evaluation context
     */
    private StandardEvaluationContext createEvaluationContext(JsonNode entity) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        
        // Add the entity as a root object
        context.setRootObject(entity);
        
        // Add each field as a variable
        Iterator<Map.Entry<String, JsonNode>> fields = entity.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            context.setVariable(field.getKey(), convertJsonNodeToJavaObject(field.getValue()));
        }
        
        return context;
    }
    
    /**
     * Converts a JsonNode to a Java object for use in SpEL expressions.
     *
     * @param node the JsonNode to convert
     * @return the Java object
     */
    private Object convertJsonNodeToJavaObject(JsonNode node) {
        if (node.isNull()) {
            return null;
        } else if (node.isTextual()) {
            return node.asText();
        } else if (node.isNumber()) {
            return node.asDouble();
        } else if (node.isBoolean()) {
            return node.asBoolean();
        } else if (node.isArray()) {
            List<Object> list = new ArrayList<>();
            for (JsonNode item : node) {
                list.add(convertJsonNodeToJavaObject(item));
            }
            return list;
        } else if (node.isObject()) {
            Map<String, Object> map = new HashMap<>();
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                map.put(field.getKey(), convertJsonNodeToJavaObject(field.getValue()));
            }
            return map;
        } else {
            return null;
        }
    }
    
    /**
     * Executes a custom JavaScript validation script.
     *
     * @param rule the validation rule
     * @param entity the entity to validate
     * @return true if valid, false otherwise
     */
    private boolean executeJavaScript(EnhancedValidationRule rule, JsonNode entity) {
        Context context = Context.enter();
        try {
            context.setOptimizationLevel(-1);
            Scriptable scope = context.initStandardObjects();
            
            // Convert JsonNode to JavaScript object
            String entityJson = entity.toString();
            Object entityObj = NativeJSON.parse(context, scope, entityJson, new org.mozilla.javascript.Callable() {
                @Override
                public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
                    return args[1];
                }
            });
            
            // Add the entity to the scope
            ScriptableObject.putProperty(scope, "entity", entityObj);
            
            // Execute the script
            Object result = context.evaluateString(scope, rule.getValidationScript(), "validation", 1, null);
            
            // Convert the result to a boolean
            return Context.toBoolean(result);
        } finally {
            Context.exit();
        }
    }
}