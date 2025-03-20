package com.nosql.poc.validation.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.nosql.poc.validation.model.SimpleValidationRule;
import com.nosql.poc.validation.repository.ValidationRuleRepository;
import com.nosql.poc.validation.dto.ImportResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service for loading validation rules from CSV files in the resources/csv directory
 */
@Service
@RequiredArgsConstructor
public class DataImportService {
    
    private static final Logger log = LoggerFactory.getLogger(DataImportService.class);

    private final ResourceLoader resourceLoader;
    private final ValidationRuleRepository validationRuleRepository;

    /**
     * Load validation rules from validation_rules.csv
     * 
     * @return ImportResult with details of the import operation
     */
    public ImportResult loadValidationRules() {
        log.info("Loading validation rules from CSV");
        
        ImportResult result = new ImportResult();
        result.setEntityType("ValidationRule");
        result.setErrors(new ArrayList<>());
        
        try {
            Resource resource = resourceLoader.getResource("classpath:csv/validation_rules.csv");
            
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {
                
                // Skip header
                String[] header = csvReader.readNext();
                if (header == null) {
                    result.setSuccess(false);
                    result.setErrorMessage("Empty CSV file");
                    return result;
                }
                
                List<SimpleValidationRule> rules = new ArrayList<>();
                String[] line;
                int rowCount = 0;
                
                while ((line = csvReader.readNext()) != null) {
                    rowCount++;
                    try {
                        SimpleValidationRule rule = parseValidationRule(line, header);
                        rules.add(rule);
                    } catch (Exception e) {
                        log.error("Error parsing validation rule at row {}: {}", rowCount, e.getMessage());
                        result.getErrors().add("Row " + rowCount + ": " + e.getMessage());
                    }
                }
                
                // Save all valid rules
                if (!rules.isEmpty()) {
                    validationRuleRepository.saveAll(rules);
                    result.setSuccess(true);
                    result.setImportedCount(rules.size());
                } else {
                    result.setSuccess(false);
                    result.setErrorMessage("No valid validation rules found");
                }
                
            } catch (IOException | CsvValidationException e) {
                log.error("Error reading validation rules CSV", e);
                result.setSuccess(false);
                result.setErrorMessage("Error reading CSV: " + e.getMessage());
            }
            
        } catch (Exception e) {
            log.error("Error loading validation rules resource", e);
            result.setSuccess(false);
            result.setErrorMessage("Error loading resource: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Parse a validation rule from a CSV row
     */
    private SimpleValidationRule parseValidationRule(String[] row, String[] header) {
        Map<String, String> rowMap = mapRowToHeader(row, header);
        
        SimpleValidationRule rule = new SimpleValidationRule();
        
        rule.setId(UUID.randomUUID().toString());
        rule.setRuleId(requireField(rowMap, "rule_id"));
        rule.setName(requireField(rowMap, "name"));
        rule.setDescription(rowMap.get("description"));
        rule.setEntityType(requireField(rowMap, "entity_type"));
        rule.setAttribute(requireField(rowMap, "attribute"));
        rule.setCondition(requireField(rowMap, "condition"));
        rule.setConditionValue(rowMap.get("condition_value"));
        rule.setMessage(rowMap.get("message"));
        rule.setSeverity(rowMap.getOrDefault("severity", "ERROR"));
        
        // Parse integer values
        Integer priority = 1;
        try {
            if (rowMap.containsKey("priority") && !rowMap.get("priority").isEmpty()) {
                priority = Integer.parseInt(rowMap.get("priority"));
            }
        } catch (Exception e) {
            log.warn("Invalid priority: {}, using 1", rowMap.get("priority"));
        }
        rule.setPriority(priority);
        
        // Parse boolean values
        Boolean active = Boolean.TRUE;
        if (rowMap.containsKey("active") && !rowMap.get("active").isEmpty()) {
            active = Boolean.parseBoolean(rowMap.get("active"));
        }
        rule.setActive(active);
        
        rule.setCategory(rowMap.get("category"));
        rule.setCreatedAt(LocalDateTime.now());
        rule.setUpdatedAt(LocalDateTime.now());
        
        return rule;
    }
    
    /**
     * Map a CSV row to column headers
     */
    private Map<String, String> mapRowToHeader(String[] row, String[] header) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < Math.min(row.length, header.length); i++) {
            map.put(header[i], row[i]);
        }
        return map;
    }
    
    /**
     * Get a required field from the row map
     */
    private String requireField(Map<String, String> map, String fieldName) {
        String value = map.get(fieldName);
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Required field missing: " + fieldName);
        }
        return value;
    }
}