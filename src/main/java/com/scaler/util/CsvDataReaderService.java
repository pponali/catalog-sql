package com.scaler.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for reading and processing CSV data files
 * This service handles the parsing and extraction of data from CSV files
 */
@Slf4j
@Service
public class CsvDataReaderService {

    /**
     * Reads a CSV file and returns its content as a list of string arrays
     * 
     * @param fileName The name of the CSV file to read
     * @return List of string arrays, each representing a row in the CSV file
     * @throws IOException If the file cannot be read
     * @throws CsvException If the CSV file is malformed
     */
    public List<String[]> readCsvFile(String fileName) throws IOException, CsvException {
        log.info("Reading CSV file: {}", fileName);
        
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                new ClassPathResource(fileName).getInputStream()))) {
            return reader.readAll();
        } catch (IOException | CsvException e) {
            log.error("Error reading CSV file: {}", fileName, e);
            throw e;
        }
    }
    
    /**
     * Reads a CSV file and returns its content as a list of maps
     * Each map represents a row in the CSV file, with column headers as keys
     * 
     * @param fileName The name of the CSV file to read
     * @return List of maps, each representing a row in the CSV file
     * @throws IOException If the file cannot be read
     * @throws CsvException If the CSV file is malformed
     */
    public List<Map<String, String>> readCsvFileAsMaps(String fileName) throws IOException, CsvException {
        List<String[]> rows = readCsvFile(fileName);
        if (rows.isEmpty()) {
            return new ArrayList<>();
        }
        
        String[] headers = rows.get(0);
        List<Map<String, String>> result = new ArrayList<>();
        
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            Map<String, String> rowMap = new HashMap<>();
            
            for (int j = 0; j < headers.length && j < row.length; j++) {
                rowMap.put(headers[j], row[j]);
            }
            
            result.add(rowMap);
        }
        
        return result;
    }
    
    /**
     * Reads the Classification Attributes CSV file
     * 
     * @return List of maps, each representing an attribute
     */
    public List<Map<String, String>> readClassificationAttributes() {
        try {
            return readCsvFileAsMaps("DevMDD_Classification_Attributes.csv");
        } catch (IOException | CsvException e) {
            log.error("Error reading Classification Attributes CSV file", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Reads the Classification Category CSV file
     * 
     * @return List of maps, each representing a category
     */
    public List<Map<String, String>> readClassificationCategories() {
        try {
            return readCsvFileAsMaps("DevMDD_Classification_Category.csv");
        } catch (IOException | CsvException e) {
            log.error("Error reading Classification Category CSV file", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Reads the Classification Attribute Mapping CSV file
     * 
     * @return List of maps, each representing an attribute mapping
     */
    public List<Map<String, String>> readAttributeMappings() {
        try {
            return readCsvFileAsMaps("DevMDD_Classification_Attr_Mapping.csv");
        } catch (IOException | CsvException e) {
            log.error("Error reading Classification Attribute Mapping CSV file", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Reads the List of Values CSV file
     * 
     * @return List of maps, each representing a value
     */
    public List<Map<String, String>> readListOfValues() {
        try {
            return readCsvFileAsMaps("DevMDD_List of Values.csv");
        } catch (IOException | CsvException e) {
            log.error("Error reading List of Values CSV file", e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Reads the Primary Hierarchy CSV file
     * 
     * @return List of maps, each representing a hierarchy level
     */
    public List<Map<String, String>> readPrimaryHierarchy() {
        try {
            return readCsvFileAsMaps("DevMDD_Primary_Hierarchy.csv");
        } catch (IOException | CsvException e) {
            log.error("Error reading Primary Hierarchy CSV file", e);
            return new ArrayList<>();
        }
    }
}
