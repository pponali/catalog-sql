package com.scaler.util;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import org.springframework.core.io.ClassPathResource;

/**
 * Service for converting CSV files from DevMDD format to the application's format
 */
@Slf4j
@Service
public class CsvFormatConverter {

    @Autowired
    private CsvDataReaderService csvDataReaderService;
    
    public static void main(String[] args) {
        try {
            CsvFormatConverter converter = new CsvFormatConverter();
            converter.convertAllDevMddCsvFiles();
            System.out.println("All CSV conversions completed successfully");
        } catch (Exception e) {
            System.err.println("Error converting CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Converts the DevMDD_Classification_Category.csv file to categories.csv format
     * 
     * @throws IOException If there's an error reading or writing files
     * @throws CsvException If there's an error parsing CSV
     */
    public void convertClassificationCategoriesToCategoriesFormat() throws IOException, CsvException {
        log.info("Converting DevMDD_Classification_Category.csv to categories.csv format");
        
        // Read the source CSV file
        List<String[]> sourceRows = readCsvFile("DevMDD_Classification_Category.csv");
        if (sourceRows.isEmpty()) {
            log.warn("Source file is empty");
            return;
        }
        
        // Create the target CSV file
        List<String[]> targetRows = new ArrayList<>();
        
        // Add header row
        targetRows.add(new String[] {
            "category_id", "code", "name", "description", "parent_id", "level", "path", "active", "metadata"
        });
        
        // Map to store category codes and their levels
        Map<String, Integer> categoryLevels = new HashMap<>();
        Map<String, String> categoryPaths = new HashMap<>();
        
        // First pass: determine levels for all categories
        determineCategoryLevels(sourceRows, categoryLevels, categoryPaths);
        
        // Second pass: convert rows
        for (int i = 1; i < sourceRows.size(); i++) {
            String[] sourceRow = sourceRows.get(i);
            
            // Skip if row is empty or doesn't have enough columns
            if (sourceRow.length < 4) {
                continue;
            }
            
            String pimAttributeId = sourceRow[0];
            String categoryCode = sourceRow[1];
            String categoryName = sourceRow[2];
            String parentCategoryCode = sourceRow[3];
            
            // Generate category ID (using the code with a prefix)
            String categoryId = "CAT_" + categoryCode;
            
            // Create description
            String description = categoryName + " category";
            
            // Get level
            int level = categoryLevels.getOrDefault(categoryCode, 1);
            
            // Get path
            String path = categoryPaths.getOrDefault(categoryCode, "/" + categoryCode);
            
            // Set active to true
            String active = "true";
            
            // Create simple metadata
            String metadata = String.format("{\"displayOrder\": %d, \"imageUrl\": \"https://example.com/images/%s.jpg\"}", 
                    i, categoryCode.toLowerCase());
            
            // Add the converted row
            targetRows.add(new String[] {
                categoryId, categoryCode, categoryName, description, parentCategoryCode, 
                String.valueOf(level), path, active, metadata
            });
        }
        
        // Write to the target CSV file
        writeCsvFile("csv/categories.csv", targetRows);
        
        log.info("Conversion completed successfully");
    }
    
    /**
     * Determines the level and path for each category based on parent-child relationships
     * 
     * @param rows The source CSV rows
     * @param categoryLevels Map to store category levels
     * @param categoryPaths Map to store category paths
     */
    private void determineCategoryLevels(List<String[]> rows, Map<String, Integer> categoryLevels, 
                                        Map<String, String> categoryPaths) {
        // Map to store parent-child relationships
        Map<String, List<String>> parentToChildren = new HashMap<>();
        Map<String, String> childToParent = new HashMap<>();
        
        // Build parent-child relationships
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length < 4) {
                continue;
            }
            
            String categoryCode = row[1];
            String parentCategoryCode = row[3];
            
            if (parentCategoryCode != null && !parentCategoryCode.isEmpty()) {
                parentToChildren.computeIfAbsent(parentCategoryCode, k -> new ArrayList<>())
                                .add(categoryCode);
                childToParent.put(categoryCode, parentCategoryCode);
            }
        }
        
        // Find root categories (those without parents)
        List<String> rootCategories = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length < 4) {
                continue;
            }
            
            String categoryCode = row[1];
            String parentCategoryCode = row[3];
            
            if (parentCategoryCode == null || parentCategoryCode.isEmpty()) {
                rootCategories.add(categoryCode);
                categoryLevels.put(categoryCode, 1);
                categoryPaths.put(categoryCode, "/" + categoryCode);
            }
        }
        
        // Traverse the hierarchy to determine levels and paths
        for (String rootCategory : rootCategories) {
            traverseHierarchy(rootCategory, 1, "/" + rootCategory, parentToChildren, categoryLevels, categoryPaths);
        }
        
        // Handle any categories that weren't processed (in case of circular references or missing parents)
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length < 4) {
                continue;
            }
            
            String categoryCode = row[1];
            
            if (!categoryLevels.containsKey(categoryCode)) {
                // Try to determine level based on parent
                String parentCode = childToParent.get(categoryCode);
                if (parentCode != null && categoryLevels.containsKey(parentCode)) {
                    int parentLevel = categoryLevels.get(parentCode);
                    categoryLevels.put(categoryCode, parentLevel + 1);
                    
                    String parentPath = categoryPaths.get(parentCode);
                    if (parentPath != null) {
                        categoryPaths.put(categoryCode, parentPath + "/" + categoryCode);
                    } else {
                        categoryPaths.put(categoryCode, "/" + categoryCode);
                    }
                } else {
                    // Default to level 1 if we can't determine
                    categoryLevels.put(categoryCode, 1);
                    categoryPaths.put(categoryCode, "/" + categoryCode);
                }
            }
        }
    }
    
    /**
     * Recursively traverses the category hierarchy to determine levels and paths
     * 
     * @param categoryCode The current category code
     * @param level The current level
     * @param path The current path
     * @param parentToChildren Map of parent categories to their children
     * @param categoryLevels Map to store category levels
     * @param categoryPaths Map to store category paths
     */
    private void traverseHierarchy(String categoryCode, int level, String path,
                                  Map<String, List<String>> parentToChildren,
                                  Map<String, Integer> categoryLevels,
                                  Map<String, String> categoryPaths) {
        categoryLevels.put(categoryCode, level);
        categoryPaths.put(categoryCode, path);
        
        List<String> children = parentToChildren.get(categoryCode);
        if (children != null) {
            for (String child : children) {
                traverseHierarchy(child, level + 1, path + "/" + child, parentToChildren, categoryLevels, categoryPaths);
            }
        }
    }
    
    /**
     * Reads a CSV file and returns its content as a list of string arrays
     * 
     * @param fileName The name of the CSV file to read
     * @return List of string arrays, each representing a row in the CSV file
     * @throws IOException If the file cannot be read
     * @throws CsvException If the CSV file is malformed
     */
    private List<String[]> readCsvFile(String fileName) throws IOException, CsvException {
        CSVParser parser = new CSVParserBuilder()
                .withSeparator('\t')
                .build();
        
        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(
                new ClassPathResource(fileName).getInputStream()))
                .withCSVParser(parser)
                .build()) {
            return reader.readAll();
        }
    }
    
    /**
     * Writes a list of string arrays to a CSV file
     * 
     * @param fileName The name of the CSV file to write
     * @param rows The rows to write to the CSV file
     * @throws IOException If the file cannot be written
     */
    private void writeCsvFile(String fileName, List<String[]> rows) throws IOException {
        // Ensure the directory exists
        Path filePath = Paths.get("src/main/resources", fileName);
        Files.createDirectories(filePath.getParent());
        
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath.toFile()))) {
            writer.writeAll(rows);
        }
    }
    
    /**
     * Converts the DevMDD_Classification_Attributes.csv file to feature_templates.csv format
     * 
     * @throws IOException If there's an error reading or writing files
     * @throws CsvException If there's an error parsing CSV
     */
    public void convertClassificationAttributesToFeatureTemplatesFormat() throws IOException, CsvException {
        log.info("Converting DevMDD_Classification_Attributes.csv to feature_templates.csv format");
        
        // Read the source CSV file
        List<String[]> sourceRows = readCsvFile("DevMDD_Classification_Attributes.csv");
        if (sourceRows.isEmpty()) {
            log.warn("Source file is empty");
            return;
        }
        
        // Create the target CSV file
        List<String[]> targetRows = new ArrayList<>();
        
        // Add header row
        targetRows.add(new String[] {
            "template_id", "template_type", "name", "description", "feature_type", "data_type", 
            "input_type", "validation_pattern", "min_value", "max_value", "allowed_values", 
            "default_value", "unit_code", "required", "filterable", "hidden", "multi_valued", 
            "searchable", "comparable", "visible", "editable", "metadata"
        });
        
        // Convert rows
        for (int i = 1; i < sourceRows.size(); i++) {
            String[] sourceRow = sourceRows.get(i);
            
            // Skip if row is empty or doesn't have enough columns
            if (sourceRow.length < 10) {
                continue;
            }
            
            String attributeReference = sourceRow[0];
            String pimAttributeId = sourceRow[1];
            String pimAttributeName = sourceRow[2];
            String description = sourceRow[7];
            String dataType = mapDataType(sourceRow[9]);
            String inputType = mapInputType(sourceRow[28]);
            String validationPattern = "";
            String minValue = "";
            String maxValue = "";
            String allowedValues = "";
            String defaultValue = sourceRow[27];
            String unitCode = mapUnitCode(sourceRow[24]);
            String required = sourceRow[14].equalsIgnoreCase("Mandatory") ? "true" : "false";
            String filterable = sourceRow[11].equalsIgnoreCase("Y") ? "true" : "false";
            String hidden = "false";
            String multiValued = sourceRow[21].equalsIgnoreCase("Y") ? "true" : "false";
            String searchable = sourceRow[12].equalsIgnoreCase("Y") ? "true" : "false";
            String comparable = "true";
            String visible = "true";
            String editable = "true";
            String metadata = String.format("{\"displayOrder\": %d, \"source\": \"classification\"}", i);
            
            // Add the converted row
            targetRows.add(new String[] {
                attributeReference, "CATEGORY", pimAttributeName, description, "SPECIFICATION", 
                dataType, inputType, validationPattern, minValue, maxValue, allowedValues, 
                defaultValue, unitCode, required, filterable, hidden, multiValued, 
                searchable, comparable, visible, editable, metadata
            });
        }
        
        // Write to the target CSV file
        writeCsvFile("csv/feature_templates.csv", targetRows);
        
        log.info("Classification Attributes to Feature Templates conversion completed successfully");
    }
    
    /**
     * Maps the data type from DevMDD format to the application's format
     * 
     * @param devMddDataType The data type in DevMDD format
     * @return The data type in the application's format
     */
    private String mapDataType(String devMddDataType) {
        if (devMddDataType == null) {
            return "STRING";
        }
        
        switch (devMddDataType.trim().toLowerCase()) {
            case "text":
            case "large text":
                return "STRING";
            case "numeric":
            case "number":
            case "integer":
                return "NUMERIC";
            case "boolean":
                return "BOOLEAN";
            case "date":
                return "DATE";
            default:
                return "STRING";
        }
    }
    
    /**
     * Maps the input type from DevMDD format to the application's format
     * 
     * @param devMddInputType The input type in DevMDD format
     * @return The input type in the application's format
     */
    private String mapInputType(String devMddInputType) {
        if (devMddInputType == null) {
            return "TEXT";
        }
        
        switch (devMddInputType.trim().toUpperCase()) {
            case "LOV":
                return "SELECT";
            case "OPEN TEXT":
                return "TEXT";
            default:
                return "TEXT";
        }
    }
    
    /**
     * Maps the unit code from DevMDD format to the application's format
     * 
     * @param devMddUnitCode The unit code in DevMDD format
     * @return The unit code in the application's format
     */
    private String mapUnitCode(String devMddUnitCode) {
        if (devMddUnitCode == null || devMddUnitCode.trim().equals("-NA-")) {
            return "";
        }
        
        return devMddUnitCode.trim();
    }
    
    /**
     * Converts the DevMDD_Classification_Attr_Mapping.csv file to category_feature_mappings.csv format
     * 
     * @throws IOException If there's an error reading or writing files
     * @throws CsvException If there's an error parsing CSV
     */
    public void convertClassificationAttrMappingToCategoryFeatureMappingsFormat() throws IOException, CsvException {
        log.info("Converting DevMDD_Classification_Attr_Mapping.csv to category_feature_mappings.csv format");
        
        // Read the source CSV file
        List<String[]> sourceRows = readCsvFile("DevMDD_Classification_Attr_Mapping.csv");
        if (sourceRows.isEmpty()) {
            log.warn("Source file is empty");
            return;
        }
        
        // Create the target CSV file
        List<String[]> targetRows = new ArrayList<>();
        
        // Add header row
        targetRows.add(new String[] {
            "category_code", "template_code", "display_order", "required", "filterable", 
            "searchable", "comparable", "visible", "editable", "metadata"
        });
        
        // Map to track display order for each category-template combination
        Map<String, Map<String, Integer>> categoryTemplateDisplayOrder = new HashMap<>();
        
        // Convert rows
        for (int i = 1; i < sourceRows.size(); i++) {
            String[] sourceRow = sourceRows.get(i);
            
            // Skip if row is empty or doesn't have enough columns
            if (sourceRow.length < 7) {
                continue;
            }
            
            String productLine = sourceRow[0];
            String pimAttributeId = sourceRow[1];
            String pimAttributeName = sourceRow[2];
            String primaryHierarchyCode = sourceRow[3];
            String primaryHierarchyName = sourceRow[4];
            String classificationCategoryCode = sourceRow[5];
            String classificationCategoryName = sourceRow[6];
            
            // Use the primary hierarchy code as the category code
            String categoryCode = primaryHierarchyCode;
            
            // Use the PIM attribute ID as the template code
            String templateCode = pimAttributeId;
            
            // Get or initialize the display order map for this category
            Map<String, Integer> templateDisplayOrders = categoryTemplateDisplayOrder
                    .computeIfAbsent(categoryCode, k -> new HashMap<>());
            
            // Get or initialize the display order for this template in this category
            int displayOrder = templateDisplayOrders.getOrDefault(templateCode, 
                    templateDisplayOrders.size() + 1);
            templateDisplayOrders.put(templateCode, displayOrder);
            
            // Default values
            String required = "false";
            String filterable = "true";
            String searchable = "true";
            String comparable = "true";
            String visible = "true";
            String editable = "true";
            String metadata = String.format("{\"displayGroup\": \"%s\"}", pimAttributeName);
            
            // Add the converted row
            targetRows.add(new String[] {
                categoryCode, templateCode, String.valueOf(displayOrder), required, filterable, 
                searchable, comparable, visible, editable, metadata
            });
        }
        
        // Write to the target CSV file
        writeCsvFile("csv/category_feature_mappings.csv", targetRows);
        
        log.info("Classification Attribute Mapping to Category Feature Mappings conversion completed successfully");
    }
    
    /**
     * Converts the DevMDD_FeatureTemplates.csv file to feature_templates.csv format
     * 
     * @throws IOException If there's an error reading or writing files
     * @throws CsvException If there's an error parsing CSV
     */
    public void convertFeatureTemplatesToApplicationFormat() throws IOException, CsvException {
        log.info("Converting DevMDD_FeatureTemplates.csv to feature_templates.csv format");
        
        // Read the source CSV file
        List<String[]> sourceRows = readCsvFile("DevMDD_FeatureTemplates.csv");
        if (sourceRows.isEmpty()) {
            log.warn("Source file is empty");
            return;
        }
        
        // Read the existing feature_templates.csv file if it exists
        List<String[]> existingRows = new ArrayList<>();
        try {
            existingRows = readCsvFile("csv/feature_templates.csv");
        } catch (IOException e) {
            log.info("No existing feature_templates.csv file found, creating a new one");
            existingRows.add(new String[] {
                "template_id", "template_type", "name", "description", "feature_type", "data_type", 
                "input_type", "validation_pattern", "min_value", "max_value", "allowed_values", 
                "default_value", "unit_code", "required", "filterable", "hidden", "multi_valued", 
                "searchable", "comparable", "visible", "editable", "metadata"
            });
        }
        
        // Create a map of existing template IDs to avoid duplicates
        Map<String, Boolean> existingTemplateIds = new HashMap<>();
        for (int i = 1; i < existingRows.size(); i++) {
            existingTemplateIds.put(existingRows.get(i)[0], true);
        }
        
        // Convert rows
        for (int i = 1; i < sourceRows.size(); i++) {
            String[] sourceRow = sourceRows.get(i);
            
            // Skip if row is empty or doesn't have enough columns
            if (sourceRow.length < 10) {
                continue;
            }
            
            String templateCode = sourceRow[0];
            
            // Skip if this template ID already exists
            if (existingTemplateIds.containsKey(templateCode)) {
                continue;
            }
            
            String templateName = sourceRow[1];
            String description = sourceRow[2];
            String featureType = sourceRow[3];
            String dataType = sourceRow[4];
            String inputType = sourceRow[5];
            String validationPattern = sourceRow[6];
            String minValue = sourceRow[7];
            String maxValue = sourceRow[8];
            String allowedValues = sourceRow[9];
            String defaultValue = sourceRow[10];
            String unitCode = sourceRow[11];
            String required = sourceRow[12];
            String filterable = sourceRow[13];
            String hidden = sourceRow[14];
            String multiValued = sourceRow[15];
            String searchable = sourceRow[16];
            String comparable = sourceRow[17];
            String visible = sourceRow[18];
            String editable = sourceRow[19];
            String metadata = sourceRow.length > 20 ? sourceRow[20] : "{}";
            
            // Add the converted row to the existing rows
            existingRows.add(new String[] {
                templateCode, "CATEGORY", templateName, description, featureType, dataType, 
                inputType, validationPattern, minValue, maxValue, allowedValues, 
                defaultValue, unitCode, required, filterable, hidden, multiValued, 
                searchable, comparable, visible, editable, metadata
            });
        }
        
        // Write to the target CSV file
        writeCsvFile("csv/feature_templates.csv", existingRows);
        
        log.info("Feature Templates conversion completed successfully");
    }
    
    /**
     * Converts the DevMDD_ProductFeatureValues.csv file to product_feature_values.csv format
     * 
     * @throws IOException If there's an error reading or writing files
     * @throws CsvException If there's an error parsing CSV
     */
    public void convertProductFeatureValuesToApplicationFormat() throws IOException, CsvException {
        log.info("Converting DevMDD_ProductFeatureValues.csv to product_feature_values.csv format");
        
        // Read the source CSV file
        List<String[]> sourceRows = readCsvFile("DevMDD_ProductFeatureValues.csv");
        if (sourceRows.isEmpty()) {
            log.warn("Source file is empty");
            return;
        }
        
        // Create the target CSV file
        List<String[]> targetRows = new ArrayList<>();
        
        // Add header row
        targetRows.add(new String[] {
            "product_code", "feature_code", "value_type", "string_value", "numeric_value", 
            "boolean_value", "unit_of_measure_code", "metadata"
        });
        
        // Convert rows
        for (int i = 1; i < sourceRows.size(); i++) {
            String[] sourceRow = sourceRows.get(i);
            
            // Skip if row is empty or doesn't have enough columns
            if (sourceRow.length < 7) {
                continue;
            }
            
            String productCode = sourceRow[0];
            String featureCode = sourceRow[1];
            String valueType = sourceRow[2];
            String stringValue = sourceRow[3];
            String numericValue = sourceRow[4];
            String booleanValue = sourceRow[5];
            String unitOfMeasureCode = sourceRow[6];
            String metadata = sourceRow.length > 7 ? sourceRow[7] : "{}";
            
            // Add the converted row
            targetRows.add(new String[] {
                productCode, featureCode, valueType, stringValue, numericValue, 
                booleanValue, unitOfMeasureCode, metadata
            });
        }
        
        // Write to the target CSV file
        writeCsvFile("csv/product_feature_values.csv", targetRows);
        
        log.info("Product Feature Values conversion completed successfully");
    }
    
    /**
     * Converts the DevMDD_UnitOfMeasure.csv file to unit_of_measure.csv format
     * 
     * @throws IOException If there's an error reading or writing files
     * @throws CsvException If there's an error parsing CSV
     */
    public void convertUnitOfMeasureToApplicationFormat() throws IOException, CsvException {
        log.info("Converting DevMDD_UnitOfMeasure.csv to unit_of_measure.csv format");
        
        // Read the source CSV file
        List<String[]> sourceRows = readCsvFile("DevMDD_UnitOfMeasure.csv");
        if (sourceRows.isEmpty()) {
            log.warn("Source file is empty");
            return;
        }
        
        // Create the target CSV file
        List<String[]> targetRows = new ArrayList<>();
        
        // Add header row
        targetRows.add(new String[] {
            "code", "name", "description", "type", "display_symbol", "active", 
            "conversion_factor", "metadata"
        });
        
        // Convert rows
        for (int i = 1; i < sourceRows.size(); i++) {
            String[] sourceRow = sourceRows.get(i);
            
            // Skip if row is empty or doesn't have enough columns
            if (sourceRow.length < 7) {
                continue;
            }
            
            String code = sourceRow[0];
            String name = sourceRow[1];
            String description = sourceRow[2];
            String type = sourceRow[3];
            String displaySymbol = sourceRow[4];
            String active = sourceRow[5];
            String conversionFactor = sourceRow[6];
            String metadata = sourceRow.length > 7 ? sourceRow[7] : "{}";
            
            // Add the converted row
            targetRows.add(new String[] {
                code, name, description, type, displaySymbol, active, 
                conversionFactor, metadata
            });
        }
        
        // Write to the target CSV file
        writeCsvFile("csv/unit_of_measure.csv", targetRows);
        
        log.info("Unit Of Measure conversion completed successfully");
    }
    
    /**
     * Converts the DevMDD_Classification_Attributes.csv and DevMDD_List of Values.csv files to validation_rules.csv format
     * 
     * @throws IOException If there's an error reading or writing files
     * @throws CsvException If there's an error parsing CSV
     */
    public void convertClassificationAttributesToValidationRulesFormat() throws IOException, CsvException {
        log.info("Converting DevMDD_Classification_Attributes.csv to validation_rules.csv format");
        
        // Read the classification attributes CSV file
        List<String[]> attributeRows = readCsvFile("DevMDD_Classification_Attributes.csv");
        if (attributeRows.isEmpty()) {
            log.warn("Classification Attributes file is empty");
            return;
        }
        
        // Read the list of values CSV file
        List<String[]> lovRows = new ArrayList<>();
        try {
            lovRows = readCsvFile("DevMDD_List of Values.csv");
        } catch (IOException e) {
            log.warn("List of Values file not found, proceeding without LOV data");
        }
        
        // Create a map of attribute ID to list of values
        Map<String, List<String[]>> attributeToValues = new HashMap<>();
        for (int i = 1; i < lovRows.size(); i++) {
            String[] row = lovRows.get(i);
            if (row.length < 4) continue;
            
            String attributeId = row[1]; // PIM_Attribute_ID column
            attributeToValues.computeIfAbsent(attributeId, k -> new ArrayList<>()).add(row);
        }
        
        // Create the target CSV file
        List<String[]> targetRows = new ArrayList<>();
        
        // Add header row
        targetRows.add(new String[] {
            "rule_id", "rule_name", "rule_type", "attribute_code", "pattern", "min_value", 
            "max_value", "allowed_values", "error_message", "severity", "active"
        });
        
        // Process each attribute row
        int ruleCounter = 1;
        for (int i = 1; i < attributeRows.size(); i++) {
            String[] row = attributeRows.get(i);
            if (row.length < 18) continue;
            
            String attributeReference = row[0];
            String attributeId = row[1];
            String attributeName = row[2];
            String isLov = row[17]; // List of Values (Y/N) column
            String lovTable = row.length > 18 ? row[18] : ""; // List of Value Table column
            String mandatory = row[14]; // Mandatory / Optional column
            
            // Create required rule if attribute is mandatory
            if ("Mandatory".equalsIgnoreCase(mandatory)) {
                String ruleId = "RULE_" + ruleCounter++;
                String ruleName = attributeName + " Required";
                String ruleType = "REQUIRED";
                String errorMessage = attributeName + " is required";
                
                targetRows.add(new String[] {
                    ruleId, ruleName, ruleType, attributeId, "", "", "", "", errorMessage, "ERROR", "true"
                });
            }
            
            // Create allowed values rule if attribute has list of values
            if ("Y".equalsIgnoreCase(isLov) && !lovTable.isEmpty()) {
                String ruleId = "RULE_" + ruleCounter++;
                String ruleName = attributeName + " Allowed Values";
                String ruleType = "ALLOWED_VALUES";
                
                // Get the allowed values for this attribute
                List<String[]> values = attributeToValues.getOrDefault(attributeId, new ArrayList<>());
                
                // Format allowed values as comma-separated list of value codes
                String allowedValues = values.stream()
                    .map(valueRow -> valueRow[3]) // LOV_Value_Code column
                    .collect(Collectors.joining(","));
                
                String errorMessage = attributeName + " must be one of the allowed values";
                
                targetRows.add(new String[] {
                    ruleId, ruleName, ruleType, attributeId, "", "", "", allowedValues, errorMessage, "ERROR", "true"
                });
            }
            
            // Add length validation for text fields
            String dataType = row[9]; // Attribute Data Type column
            String length = row[10]; // Length column
            
            if (("Text".equalsIgnoreCase(dataType) || "String".equalsIgnoreCase(dataType) || 
                 "Large Text".equalsIgnoreCase(dataType)) && !length.isEmpty()) {
                try {
                    int maxLength = Integer.parseInt(length);
                    String ruleId = "RULE_" + ruleCounter++;
                    String ruleName = attributeName + " Max Length";
                    String ruleType = "PATTERN";
                    String pattern = "^.{0," + maxLength + "}$";
                    String errorMessage = attributeName + " cannot exceed " + maxLength + " characters";
                    
                    targetRows.add(new String[] {
                        ruleId, ruleName, ruleType, attributeId, pattern, "", "", "", errorMessage, "ERROR", "true"
                    });
                } catch (NumberFormatException e) {
                    log.warn("Invalid length value for attribute: " + attributeId);
                }
            }
        }
        
        // Write to the target CSV file
        writeCsvFile("csv/validation_rules.csv", targetRows);
        
        log.info("Validation Rules conversion completed successfully");
    }

    /**
     * Converts all DevMDD CSV files to the application's format
     * 
     * @throws IOException If there's an error reading or writing files
     * @throws CsvException If there's an error parsing CSV
     */
    public void convertAllDevMddCsvFiles() throws IOException, CsvException {
        // Convert Classification Categories
        convertClassificationCategoriesToCategoriesFormat();
        
        // Convert Classification Attributes to Feature Templates
        convertClassificationAttributesToFeatureTemplatesFormat();
        
        // Convert Classification Attribute Mappings to Category Feature Mappings
        convertClassificationAttrMappingToCategoryFeatureMappingsFormat();
        
        // Convert Feature Templates
        convertFeatureTemplatesToApplicationFormat();
        
        // Convert Product Feature Values
        convertProductFeatureValuesToApplicationFormat();
        
        // Convert Unit Of Measure
        convertUnitOfMeasureToApplicationFormat();
        
        // Convert Classification Attributes to Validation Rules
        convertClassificationAttributesToValidationRulesFormat();
        
        log.info("All CSV conversions completed successfully");
    }
}
