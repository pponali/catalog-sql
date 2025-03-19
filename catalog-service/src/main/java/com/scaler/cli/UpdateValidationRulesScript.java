package com.scaler.cli;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Standalone script to update validation rules based on classification attributes
 * This script can be run directly without Spring Boot
 * 
 * Usage: java -cp target/catalog-sql.jar com.scaler.cli.UpdateValidationRulesScript [classificationAttributesPath] [validationRulesPath]
 */
public class UpdateValidationRulesScript {

    public static void main(String[] args) {
        System.out.println("Starting validation rule update script...");
        
        // Parse command-line arguments
        String classificationAttributesPath = args.length > 0 ? args[0] : "DevMDD_Classification_Attributes.csv";
        String validationRulesPath = args.length > 1 ? args[1] : "src/main/resources/csv/validation_rules.csv";
        
        System.out.println("Using classification attributes path: " + classificationAttributesPath);
        System.out.println("Using validation rules path: " + validationRulesPath);
        
        try {
            // Load classification attributes
            List<Map<String, String>> classificationAttributes = loadCsvData(classificationAttributesPath);
            
            if (classificationAttributes.isEmpty()) {
                System.err.println("No classification attributes found in file: " + classificationAttributesPath);
                System.exit(1);
            }
            
            System.out.println("Loaded " + classificationAttributes.size() + " classification attributes from file: " + classificationAttributesPath);
            
            // Load existing validation rules
            List<Map<String, String>> existingRules = loadCsvData(validationRulesPath);
            
            System.out.println("Loaded " + existingRules.size() + " existing validation rules from file: " + validationRulesPath);
            
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
                    System.out.println("Skipping attribute with empty reference: " + attribute);
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
                        System.out.println("Updated validation rule for attribute: " + attributeRef);
                    }
                } else {
                    // Create new rule
                    Map<String, String> newRule = createRule(attribute);
                    updatedRules.add(newRule);
                    updatedCount++;
                    System.out.println("Created new validation rule for attribute: " + attributeRef);
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
            
            System.out.println("Updated " + updatedCount + " validation rules");
            System.out.println("Validation rule update completed");
            
        } catch (Exception e) {
            System.err.println("Error updating validation rules: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Load CSV data from a file
     * 
     * @param filePath The path to the CSV file
     * @return List of maps, each representing a row in the CSV file
     */
    private static List<Map<String, String>> loadCsvData(String filePath) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            System.err.println("File not found: " + filePath);
            return data;
        }
        
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        if (lines.isEmpty()) {
            System.err.println("Empty file: " + filePath);
            return data;
        }
        
        // Parse header
        String[] headers = parseCSVLine(lines.get(0));
        
        // Parse data
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] values = parseCSVLine(line);
            
            Map<String, String> row = new HashMap<>();
            for (int j = 0; j < headers.length; j++) {
                if (j < values.length) {
                    row.put(headers[j], values[j]);
                } else {
                    row.put(headers[j], "");
                }
            }
            
            data.add(row);
        }
        
        return data;
    }
    
    /**
     * Parse a CSV line, handling quoted values
     * 
     * @param line The CSV line to parse
     * @return Array of values
     */
    private static String[] parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                // If we're in quotes and the next character is also a quote, it's an escaped quote
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    currentValue.append('"');
                    i++; // Skip the next quote
                } else {
                    // Otherwise, toggle the inQuotes flag
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                // End of value
                values.add(currentValue.toString());
                currentValue = new StringBuilder();
            } else {
                // Add character to current value
                currentValue.append(c);
            }
        }
        
        // Add the last value
        values.add(currentValue.toString());
        
        return values.toArray(new String[0]);
    }
    
    /**
     * Update an existing rule based on a classification attribute
     * 
     * @param existingRule The existing rule
     * @param attribute The classification attribute
     * @return The updated rule
     */
    private static Map<String, String> updateRule(Map<String, String> existingRule, Map<String, String> attribute) {
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
    private static Map<String, String> createRule(Map<String, String> attribute) {
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
    private static void writeRulesToCsv(List<Map<String, String>> rules, String filePath) throws IOException {
        System.out.println("Writing " + rules.size() + " validation rules to file: " + filePath);
        
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
        
        System.out.println("Successfully wrote validation rules to file: " + filePath);
    }
}
