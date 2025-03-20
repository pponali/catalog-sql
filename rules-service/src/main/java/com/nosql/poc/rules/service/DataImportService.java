package com.nosql.poc.rules.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.nosql.poc.rules.model.SimpleValidationRule;
import com.nosql.poc.rules.model.SimpleBusinessRule;
import com.nosql.poc.rules.repository.ValidationRuleRepository;
import com.nosql.poc.rules.repository.BusinessRuleRepository;
import com.nosql.poc.rules.dto.ImportResult;
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
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service for loading rules from CSV files in the resources/csv directory
 */
@Service
@RequiredArgsConstructor
public class DataImportService {
    
    private static final Logger log = LoggerFactory.getLogger(DataImportService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final ResourceLoader resourceLoader;
    private final ValidationRuleRepository validationRuleRepository;
    private final BusinessRuleRepository businessRuleRepository;

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
     * Load business rules from business_rules.csv
     * 
     * @return ImportResult with details of the import operation
     */
    public ImportResult loadBusinessRules() {
        log.info("Loading business rules from CSV");
        
        ImportResult result = new ImportResult();
        result.setEntityType("BusinessRule");
        result.setErrors(new ArrayList<>());
        
        try {
            Resource resource = resourceLoader.getResource("classpath:csv/business_rules.csv");
            
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {
                
                // Skip header
                String[] header = csvReader.readNext();
                if (header == null) {
                    result.setSuccess(false);
                    result.setErrorMessage("Empty CSV file");
                    return result;
                }
                
                List<SimpleBusinessRule> rules = new ArrayList<>();
                String[] line;
                int rowCount = 0;
                
                while ((line = csvReader.readNext()) != null) {
                    rowCount++;
                    try {
                        SimpleBusinessRule rule = parseBusinessRule(line, header);
                        rules.add(rule);
                    } catch (Exception e) {
                        log.error("Error parsing business rule at row {}: {}", rowCount, e.getMessage());
                        result.getErrors().add("Row " + rowCount + ": " + e.getMessage());
                    }
                }
                
                // Save all valid rules
                if (!rules.isEmpty()) {
                    businessRuleRepository.saveAll(rules);
                    result.setSuccess(true);
                    result.setImportedCount(rules.size());
                } else {
                    result.setSuccess(false);
                    result.setErrorMessage("No valid business rules found");
                }
                
            } catch (IOException | CsvValidationException e) {
                log.error("Error reading business rules CSV", e);
                result.setSuccess(false);
                result.setErrorMessage("Error reading CSV: " + e.getMessage());
            }
            
        } catch (Exception e) {
            log.error("Error loading business rules resource", e);
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
        rule.setRuleId(requireField(rowMap, "ruleId"));
        rule.setName(requireField(rowMap, "name"));
        rule.setDescription(rowMap.get("description"));
        rule.setEntityType(requireField(rowMap, "entityType"));
        rule.setAttribute(requireField(rowMap, "attribute"));
        rule.setCondition(requireField(rowMap, "condition"));
        rule.setConditionValue(rowMap.get("conditionValue"));
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
        
        // Parse date values
        try {
            if (rowMap.containsKey("createdAt") && !rowMap.get("createdAt").isEmpty()) {
                rule.setCreatedAt(LocalDateTime.parse(rowMap.get("createdAt"), DATE_FORMATTER));
            } else {
                rule.setCreatedAt(LocalDateTime.now());
            }
        } catch (Exception e) {
            log.warn("Invalid createdAt date: {}, using current date", rowMap.get("createdAt"));
            rule.setCreatedAt(LocalDateTime.now());
        }
        
        try {
            if (rowMap.containsKey("updatedAt") && !rowMap.get("updatedAt").isEmpty()) {
                rule.setUpdatedAt(LocalDateTime.parse(rowMap.get("updatedAt"), DATE_FORMATTER));
            } else {
                rule.setUpdatedAt(LocalDateTime.now());
            }
        } catch (Exception e) {
            log.warn("Invalid updatedAt date: {}, using current date", rowMap.get("updatedAt"));
            rule.setUpdatedAt(LocalDateTime.now());
        }
        
        return rule;
    }
    
    /**
     * Parse a business rule from a CSV row
     */
    private SimpleBusinessRule parseBusinessRule(String[] row, String[] header) {
        Map<String, String> rowMap = mapRowToHeader(row, header);
        
        SimpleBusinessRule rule = new SimpleBusinessRule();
        
        rule.setId(UUID.randomUUID().toString());
        rule.setRuleId(requireField(rowMap, "ruleId"));
        rule.setName(requireField(rowMap, "name"));
        rule.setDescription(rowMap.get("description"));
        rule.setRuleType(requireField(rowMap, "ruleType"));
        rule.setEntityType(requireField(rowMap, "entityType"));
        rule.setCondition(requireField(rowMap, "condition"));
        rule.setAction(requireField(rowMap, "action"));
        rule.setActionParameters(rowMap.get("actionParameters"));
        rule.setPriority(rowMap.get("priority"));
        
        // Parse boolean values
        Boolean active = Boolean.TRUE;
        if (rowMap.containsKey("active") && !rowMap.get("active").isEmpty()) {
            active = Boolean.parseBoolean(rowMap.get("active"));
        }
        rule.setActive(active);
        
        rule.setCategory(rowMap.get("category"));
        
        // Parse date values
        LocalDateTime startDate = null;
        try {
            if (rowMap.containsKey("startDate") && !rowMap.get("startDate").isEmpty()) {
                startDate = LocalDateTime.parse(rowMap.get("startDate"), DATE_FORMATTER);
            } else {
                startDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            }
        } catch (Exception e) {
            log.warn("Invalid start date: {}, using current date", rowMap.get("startDate"));
            startDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        }
        rule.setStartDate(startDate);
        
        LocalDateTime endDate = null;
        try {
            if (rowMap.containsKey("endDate") && !rowMap.get("endDate").isEmpty()) {
                endDate = LocalDateTime.parse(rowMap.get("endDate"), DATE_FORMATTER);
            } else {
                endDate = startDate.plusYears(1);
            }
        } catch (Exception e) {
            log.warn("Invalid end date: {}, using start date + 1 year", rowMap.get("endDate"));
            endDate = startDate.plusYears(1);
        }
        rule.setEndDate(endDate);
        
        // Parse created/updated dates
        try {
            if (rowMap.containsKey("createdAt") && !rowMap.get("createdAt").isEmpty()) {
                rule.setCreatedAt(LocalDateTime.parse(rowMap.get("createdAt"), DATE_FORMATTER));
            } else {
                rule.setCreatedAt(LocalDateTime.now());
            }
        } catch (Exception e) {
            log.warn("Invalid createdAt date: {}, using current date", rowMap.get("createdAt"));
            rule.setCreatedAt(LocalDateTime.now());
        }
        
        try {
            if (rowMap.containsKey("updatedAt") && !rowMap.get("updatedAt").isEmpty()) {
                rule.setUpdatedAt(LocalDateTime.parse(rowMap.get("updatedAt"), DATE_FORMATTER));
            } else {
                rule.setUpdatedAt(LocalDateTime.now());
            }
        } catch (Exception e) {
            log.warn("Invalid updatedAt date: {}, using current date", rowMap.get("updatedAt"));
            rule.setUpdatedAt(LocalDateTime.now());
        }
        
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
    
    /**
     * Load all rules from CSV files
     * 
     * @return Map of entity types to import results
     */
    public Map<String, ImportResult> loadAllRules() {
        log.info("Loading all rules from CSV files");
        
        Map<String, ImportResult> results = new HashMap<>();
        
        // Load in a specific order to handle dependencies
        ImportResult validationRuleResult = loadValidationRules();
        ImportResult businessRuleResult = loadBusinessRules();
        
        results.put("ValidationRules", validationRuleResult);
        results.put("BusinessRules", businessRuleResult);
        
        return results;
    }
}