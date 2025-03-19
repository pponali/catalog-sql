package com.scaler.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A resilient CSV data loader that can handle various error scenarios
 * and provides robust data loading capabilities.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResilientCsvDataLoader {

    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;
    
    // Cache for CSV data to avoid repeated file reads
    private final Map<String, List<Map<String, String>>> csvDataCache = new ConcurrentHashMap<>();
    
    // Error statistics
    private final Map<String, AtomicInteger> errorCountByFile = new ConcurrentHashMap<>();
    private final Map<String, List<String>> errorMessagesByFile = new ConcurrentHashMap<>();
    
    /**
     * Load CSV data from a file with retry capabilities
     * 
     * @param filePath The path to the CSV file
     * @return List of maps, each representing a row in the CSV file
     */
    @Retryable(
        value = {IOException.class, CsvException.class},
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public List<Map<String, String>> loadCsvData(String filePath) {
        // Check cache first
        if (csvDataCache.containsKey(filePath)) {
            log.debug("Using cached data for file: {}", filePath);
            return csvDataCache.get(filePath);
        }
        
        log.info("Loading CSV data from file: {}", filePath);
        
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                resourceLoader.getResource("classpath:" + filePath).getInputStream(), 
                StandardCharsets.UTF_8))) {
            
            List<String[]> rows = reader.readAll();
            if (rows.isEmpty()) {
                log.warn("CSV file is empty: {}", filePath);
                return Collections.emptyList();
            }
            
            String[] headers = rows.get(0);
            List<Map<String, String>> result = new ArrayList<>();
            
            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                Map<String, String> rowMap = new HashMap<>();
                
                // Handle rows with fewer columns than headers
                for (int j = 0; j < headers.length; j++) {
                    if (j < row.length) {
                        rowMap.put(headers[j], row[j]);
                    } else {
                        rowMap.put(headers[j], "");
                    }
                }
                
                result.add(rowMap);
            }
            
            // Cache the result
            csvDataCache.put(filePath, result);
            
            log.info("Successfully loaded {} rows from CSV file: {}", result.size(), filePath);
            return result;
            
        } catch (IOException | CsvException e) {
            // Track error statistics
            errorCountByFile.computeIfAbsent(filePath, k -> new AtomicInteger(0)).incrementAndGet();
            errorMessagesByFile.computeIfAbsent(filePath, k -> new ArrayList<>()).add(e.getMessage());
            
            log.error("Error loading CSV data from file: {}", filePath, e);
            throw new RuntimeException("Failed to load CSV data from file: " + filePath, e);
        }
    }
    
    /**
     * Recovery method for loadCsvData
     * 
     * @param e The exception that triggered recovery
     * @param filePath The path to the CSV file
     * @return Empty list as fallback
     */
    @Recover
    public List<Map<String, String>> recoverFromLoadFailure(Exception e, String filePath) {
        log.warn("All retry attempts failed for file: {}. Using empty list as fallback.", filePath);
        return Collections.emptyList();
    }
    
    /**
     * Load all CSV files from a directory
     * 
     * @param directoryPath The path to the directory containing CSV files
     * @return Map of file names to their data
     */
    public Map<String, List<Map<String, String>>> loadAllCsvFilesFromDirectory(String directoryPath) {
        log.info("Loading all CSV files from directory: {}", directoryPath);
        
        Map<String, List<Map<String, String>>> result = new HashMap<>();
        
        try {
            // Find all CSV files in the directory
            Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                    .getResources("classpath:" + directoryPath + "/*.csv");
            
            log.info("Found {} CSV files in directory: {}", resources.length, directoryPath);
            
            // Load each file
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                if (filename != null && !filename.startsWith(".")) {
                    String filePath = directoryPath + "/" + filename;
                    result.put(filename, loadCsvData(filePath));
                }
            }
            
        } catch (IOException e) {
            log.error("Error loading CSV files from directory: {}", directoryPath, e);
        }
        
        return result;
    }
    
    /**
     * Validate CSV data against a schema
     * 
     * @param data The CSV data to validate
     * @param requiredFields List of required field names
     * @param fieldValidators Map of field names to validation functions
     * @return List of validation errors
     */
    public List<String> validateCsvData(List<Map<String, String>> data, 
                                       List<String> requiredFields,
                                       Map<String, Function<String, Boolean>> fieldValidators) {
        List<String> errors = new ArrayList<>();
        
        if (data.isEmpty()) {
            errors.add("CSV data is empty");
            return errors;
        }
        
        // Check for required fields
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> row = data.get(i);
            
            for (String field : requiredFields) {
                if (!row.containsKey(field) || StringUtils.isEmpty(row.get(field))) {
                    errors.add(String.format("Row %d: Missing required field '%s'", i + 1, field));
                }
            }
            
            // Apply field validators
            for (Map.Entry<String, Function<String, Boolean>> entry : fieldValidators.entrySet()) {
                String field = entry.getKey();
                Function<String, Boolean> validator = entry.getValue();
                
                if (row.containsKey(field) && !StringUtils.isEmpty(row.get(field))) {
                    String value = row.get(field);
                    if (!validator.apply(value)) {
                        errors.add(String.format("Row %d: Invalid value '%s' for field '%s'", i + 1, value, field));
                    }
                }
            }
        }
        
        return errors;
    }
    
    /**
     * Convert CSV data to JSON
     * 
     * @param data The CSV data to convert
     * @return List of JSON objects
     */
    public List<ObjectNode> convertCsvDataToJson(List<Map<String, String>> data) {
        return data.stream()
                .map(row -> {
                    ObjectNode json = objectMapper.createObjectNode();
                    row.forEach(json::put);
                    return json;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Merge multiple CSV files based on a common key
     * 
     * @param primaryData The primary CSV data
     * @param secondaryData The secondary CSV data
     * @param primaryKey The key field in the primary data
     * @param secondaryKey The key field in the secondary data
     * @param mergeFields The fields to merge from the secondary data
     * @return Merged CSV data
     */
    public List<Map<String, String>> mergeCsvData(
            List<Map<String, String>> primaryData,
            List<Map<String, String>> secondaryData,
            String primaryKey,
            String secondaryKey,
            List<String> mergeFields) {
        
        // Create a map of secondary data for quick lookup
        Map<String, Map<String, String>> secondaryMap = secondaryData.stream()
                .filter(row -> row.containsKey(secondaryKey) && !StringUtils.isEmpty(row.get(secondaryKey)))
                .collect(Collectors.toMap(
                        row -> row.get(secondaryKey),
                        Function.identity(),
                        (existing, replacement) -> {
                            log.warn("Duplicate key found in secondary data: {}", replacement.get(secondaryKey));
                            return existing;
                        }
                ));
        
        // Merge the data
        return primaryData.stream()
                .map(row -> {
                    String key = row.get(primaryKey);
                    if (key != null && secondaryMap.containsKey(key)) {
                        Map<String, String> secondaryRow = secondaryMap.get(key);
                        
                        // Create a new map with all primary data
                        Map<String, String> mergedRow = new HashMap<>(row);
                        
                        // Add selected fields from secondary data
                        for (String field : mergeFields) {
                            if (secondaryRow.containsKey(field)) {
                                mergedRow.put(field, secondaryRow.get(field));
                            }
                        }
                        
                        return mergedRow;
                    }
                    
                    return row;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Transform CSV data using a transformation function
     * 
     * @param data The CSV data to transform
     * @param transformer The transformation function
     * @return Transformed CSV data
     */
    public List<Map<String, String>> transformCsvData(
            List<Map<String, String>> data,
            Function<Map<String, String>, Map<String, String>> transformer) {
        
        return data.stream()
                .map(transformer)
                .collect(Collectors.toList());
    }
    
    /**
     * Get error statistics for CSV loading
     * 
     * @return Map of file names to error counts and messages
     */
    public Map<String, Map<String, Object>> getErrorStatistics() {
        Map<String, Map<String, Object>> result = new HashMap<>();
        
        for (String file : errorCountByFile.keySet()) {
            Map<String, Object> stats = new HashMap<>();
            stats.put("count", errorCountByFile.get(file).get());
            stats.put("messages", errorMessagesByFile.getOrDefault(file, Collections.emptyList()));
            
            result.put(file, stats);
        }
        
        return result;
    }
    
    /**
     * Clear the CSV data cache
     */
    public void clearCache() {
        csvDataCache.clear();
        log.info("CSV data cache cleared");
    }
    
    /**
     * Convert CSV data to a JSON string
     * 
     * @param data The CSV data to convert
     * @return JSON string
     */
    public String convertCsvDataToJsonString(List<Map<String, String>> data) {
        try {
            return objectMapper.writeValueAsString(convertCsvDataToJson(data));
        } catch (JsonProcessingException e) {
            log.error("Error converting CSV data to JSON string", e);
            return "[]";
        }
    }
}
