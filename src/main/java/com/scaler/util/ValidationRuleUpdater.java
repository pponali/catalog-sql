package com.scaler.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for updating validation rules based on classification attributes
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidationRuleUpdater {

    private final ResilientCsvDataLoader csvDataLoader;
    private final ObjectMapper objectMapper;

    /**
     * Update validation rules based on classification attributes
     * 
     * @param classificationAttributesPath Path to the classification attributes CSV file
     * @param validationRulesPath Path to the validation rules CSV file
     * @return Number of rules updated
     */
    @Transactional
    public int updateValidationRules(String classificationAttributesPath, String validationRulesPath) {
        log.info("Updating validation rules based on classification attributes");
        
        try {
            // Load classification attributes
            List<Map<String, String>> classificationAttributes = csvDataLoader.loadCsvData(classificationAttributesPath);
            
            if (classificationAttributes.isEmpty()) {
                log.warn("No classification attributes found in file: {}", classificationAttributesPath);
                return 0;
            }
            
            log.info("Loaded {} classification attributes from file: {}", classificationAttributes.size(), classificationAttributesPath);
            
            // Load existing validation rules
            List<Map<String, String>> existingRules = csvDataLoader.loadCsvData(validationRulesPath);
            
            log.info("Loaded {} existing validation rules from file: {}", existingRules.size(), validationRulesPath);
            
            // Create a map of existing rules by attribute reference for quick lookup
            Map<String, Map<String, String>> existingRulesByAttributeRef = existingRules.stream()
                    .filter(rule -> rule.containsKey("Attribute Reference"))
                    .collect(Collectors.toMap(
                            rule -> rule.get("Attribute Reference"),
                            rule -> rule,
                            (existing, replacement) -> existing // Keep existing in case of duplicates
                    ));
            
            // Process classification attributes and update validation rules
            List<Map<String, String>> updatedRules = new ArrayList<>();
            int updatedCount = 0;
            
            for (Map<String, String> attribute : classificationAttributes) {
                String attributeRef = attribute.getOrDefault("Attribute Reference", "");
                
                if (attributeRef.isEmpty()) {
                    log.warn("Skipping attribute with empty reference: {}", attribute);
                    continue;
                }
                
                // Check if rule already exists for this attribute
                Map<String, String> existingRule = existingRulesByAttributeRef.get(attributeRef);
                
                if (existingRule != null) {
                    // Update existing rule
                    Map<String, String> updatedRule = updateRule(existingRule, attribute);
                    updatedRules.add(updatedRule);
                    
                    if (!existingRule.equals(updatedRule)) {
                        updatedCount++;
                        log.debug("Updated validation rule for attribute: {}", attributeRef);
                    }
                } else {
                    // Create new rule
                    Map<String, String> newRule = createRule(attribute);
                    updatedRules.add(newRule);
                    updatedCount++;
                    log.debug("Created new validation rule for attribute: {}", attributeRef);
                }
            }
            
            // Add any existing rules that weren't updated
            for (Map<String, String> existingRule : existingRules) {
                String attributeRef = existingRule.getOrDefault("Attribute Reference", "");
                
                if (attributeRef.isEmpty() || !existingRulesByAttributeRef.containsKey(attributeRef)) {
                    updatedRules.add(existingRule);
                }
            }
            
            // Write updated rules to file
            writeRulesToCsv(updatedRules, validationRulesPath);
            
            log.info("Updated {} validation rules", updatedCount);
            return updatedCount;
            
        } catch (Exception e) {
            log.error("Error updating validation rules", e);
            throw new RuntimeException("Error updating validation rules", e);
        }
    }
    
    /**
     * Update an existing rule based on a classification attribute
     * 
     * @param existingRule The existing rule
     * @param attribute The classification attribute
     * @return The updated rule
     */
    private Map<String, String> updateRule(Map<String, String> existingRule, Map<String, String> attribute) {
        Map<String, String> updatedRule = new HashMap<>(existingRule);
        
        // Update rule fields based on attribute
        updatedRule.put("Attribute Reference", attribute.getOrDefault("Attribute Reference", ""));
        updatedRule.put("PIM Attribute ID", attribute.getOrDefault("PIM Attribute ID (Internal PCM)", ""));
        updatedRule.put("PIM Attribute Name", attribute.getOrDefault("PIM Attribute Name", ""));
        updatedRule.put("Product Line", attribute.getOrDefault("Product Line", ""));
        updatedRule.put("Data Type", attribute.getOrDefault("Attribute Data Type", ""));
        updatedRule.put("Length", attribute.getOrDefault("Length", ""));
        updatedRule.put("Mandatory", attribute.getOrDefault("Mandatory / Optional", "").equalsIgnoreCase("Mandatory") ? "Yes" : "No");
        updatedRule.put("List of Values", attribute.getOrDefault("List of Values (Y/N)", ""));
        updatedRule.put("List of Value Table", attribute.getOrDefault("List of Value Table", ""));
        updatedRule.put("Multi Valued", attribute.getOrDefault("Multi valued", ""));
        updatedRule.put("Units", attribute.getOrDefault("Units (Y/N)", ""));
        updatedRule.put("Unit of Measure", attribute.getOrDefault("Unit of Measure (UOM)", ""));
        updatedRule.put("Default UOM", attribute.getOrDefault("Default UOM", ""));
        updatedRule.put("Default Value", attribute.getOrDefault("Default Value", ""));
        updatedRule.put("Type", attribute.getOrDefault("Type", ""));
        updatedRule.put("Business Rules", attribute.getOrDefault("Business Rules", ""));
        updatedRule.put("Hint Text", attribute.getOrDefault("Hint Text", ""));
        
        return updatedRule;
    }
    
    /**
     * Create a new rule based on a classification attribute
     * 
     * @param attribute The classification attribute
     * @return The new rule
     */
    private Map<String, String> createRule(Map<String, String> attribute) {
        Map<String, String> rule = new HashMap<>();
        
        // Set rule fields based on attribute
        rule.put("Attribute Reference", attribute.getOrDefault("Attribute Reference", ""));
        rule.put("PIM Attribute ID", attribute.getOrDefault("PIM Attribute ID (Internal PCM)", ""));
        rule.put("PIM Attribute Name", attribute.getOrDefault("PIM Attribute Name", ""));
        rule.put("Product Line", attribute.getOrDefault("Product Line", ""));
        rule.put("Data Type", attribute.getOrDefault("Attribute Data Type", ""));
        rule.put("Length", attribute.getOrDefault("Length", ""));
        rule.put("Mandatory", attribute.getOrDefault("Mandatory / Optional", "").equalsIgnoreCase("Mandatory") ? "Yes" : "No");
        rule.put("List of Values", attribute.getOrDefault("List of Values (Y/N)", ""));
        rule.put("List of Value Table", attribute.getOrDefault("List of Value Table", ""));
        rule.put("Multi Valued", attribute.getOrDefault("Multi valued", ""));
        rule.put("Units", attribute.getOrDefault("Units (Y/N)", ""));
        rule.put("Unit of Measure", attribute.getOrDefault("Unit of Measure (UOM)", ""));
        rule.put("Default UOM", attribute.getOrDefault("Default UOM", ""));
        rule.put("Default Value", attribute.getOrDefault("Default Value", ""));
        rule.put("Type", attribute.getOrDefault("Type", ""));
        rule.put("Business Rules", attribute.getOrDefault("Business Rules", ""));
        rule.put("Hint Text", attribute.getOrDefault("Hint Text", ""));
        
        // Add additional fields with default values
        rule.put("Rule ID", UUID.randomUUID().toString());
        rule.put("Rule Name", attribute.getOrDefault("PIM Attribute Name", "") + " Validation Rule");
        rule.put("Rule Description", "Validation rule for " + attribute.getOrDefault("PIM Attribute Name", ""));
        rule.put("Rule Type", "ATTRIBUTE");
        rule.put("Rule Severity", "ERROR");
        rule.put("Rule Status", "ACTIVE");
        rule.put("Created By", "system");
        rule.put("Created Date", new Date().toString());
        
        return rule;
    }
    
    /**
     * Write validation rules to a CSV file
     * 
     * @param rules The rules to write
     * @param filePath The path to the file
     */
    private void writeRulesToCsv(List<Map<String, String>> rules, String filePath) {
        log.info("Writing {} validation rules to file: {}", rules.size(), filePath);
        
        try {
            // Get all unique headers from all rules
            Set<String> headers = new LinkedHashSet<>();
            headers.add("Rule ID"); // Ensure important fields come first
            headers.add("Rule Name");
            headers.add("Rule Description");
            headers.add("Rule Type");
            headers.add("Rule Severity");
            headers.add("Rule Status");
            headers.add("Attribute Reference");
            headers.add("PIM Attribute ID");
            headers.add("PIM Attribute Name");
            headers.add("Product Line");
            
            // Add all other headers
            for (Map<String, String> rule : rules) {
                headers.addAll(rule.keySet());
            }
            
            // Get the actual file path
            Path path = Paths.get(filePath);
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            
            // Write to CSV
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                // Write header
                writer.write(String.join(",", headers));
                writer.newLine();
                
                // Write rules
                for (Map<String, String> rule : rules) {
                    List<String> values = new ArrayList<>();
                    
                    for (String header : headers) {
                        String value = rule.getOrDefault(header, "");
                        // Escape commas and quotes
                        if (value.contains(",") || value.contains("\"")) {
                            value = "\"" + value.replace("\"", "\"\"") + "\"";
                        }
                        values.add(value);
                    }
                    
                    writer.write(String.join(",", values));
                    writer.newLine();
                }
            }
            
            log.info("Successfully wrote validation rules to file: {}", filePath);
            
        } catch (Exception e) {
            log.error("Error writing validation rules to file: {}", filePath, e);
            throw new RuntimeException("Error writing validation rules to file: " + filePath, e);
        }
    }
    
    /**
     * Read the current validation rules CSV file
     * 
     * @param filePath The path to the file
     * @return List of maps, each representing a row in the CSV file
     */
    private List<Map<String, String>> readValidationRulesCsv(String filePath) {
        log.info("Reading validation rules from file: {}", filePath);
        
        List<Map<String, String>> rules = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            if (line == null) {
                log.warn("Empty validation rules file: {}", filePath);
                return rules;
            }
            
            // Parse header
            String[] headers = line.split(",");
            
            // Parse rules
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> rule = new HashMap<>();
                
                for (int i = 0; i < headers.length; i++) {
                    if (i < values.length) {
                        rule.put(headers[i], values[i]);
                    } else {
                        rule.put(headers[i], "");
                    }
                }
                
                rules.add(rule);
            }
            
            log.info("Successfully read {} validation rules from file: {}", rules.size(), filePath);
            return rules;
            
        } catch (Exception e) {
            log.error("Error reading validation rules from file: {}", filePath, e);
            throw new RuntimeException("Error reading validation rules from file: " + filePath, e);
        }
    }
}
