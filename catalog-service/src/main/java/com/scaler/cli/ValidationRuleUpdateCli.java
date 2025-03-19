package com.scaler.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.util.ResilientCsvDataLoader;
import com.scaler.util.ValidationRuleUpdater;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.ResourceLoader;

/**
 * Command-line utility for updating validation rules based on classification attributes
 * 
 * Usage: java -jar catalog-sql.jar --spring.main.web-application-type=none --classificationAttributesPath=DevMDD_Classification_Attributes.csv --validationRulesPath=src/main/resources/csv/validation_rules.csv
 */
//@SpringBootApplication
@ComponentScan(
    basePackages = {"com.scaler.util", "com.scaler.cli"},
    excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.scaler\\.controller\\..*")
)
public class ValidationRuleUpdateCli {

    public static void main(String[] args) {
        SpringApplication.run(ValidationRuleUpdateCli.class, args);
    }
    
    @Bean
    public CommandLineRunner commandLineRunner(
            ResourceLoader resourceLoader,
            ObjectMapper objectMapper) {
        return args -> {
            System.out.println("Starting validation rule update CLI...");
            
            // Parse command-line arguments
            String classificationAttributesPath = "DevMDD_Classification_Attributes.csv";
            String validationRulesPath = "src/main/resources/csv/validation_rules.csv";
            
            for (String arg : args) {
                if (arg.startsWith("--classificationAttributesPath=")) {
                    classificationAttributesPath = arg.substring("--classificationAttributesPath=".length());
                } else if (arg.startsWith("--validationRulesPath=")) {
                    validationRulesPath = arg.substring("--validationRulesPath=".length());
                }
            }
            
            System.out.println("Using classification attributes path: " + classificationAttributesPath);
            System.out.println("Using validation rules path: " + validationRulesPath);
            
            // Create dependencies
            ResilientCsvDataLoader csvDataLoader = new ResilientCsvDataLoader(resourceLoader, objectMapper);
            ValidationRuleUpdater validationRuleUpdater = new ValidationRuleUpdater(csvDataLoader, objectMapper);
            
            // Update validation rules
            try {
                int updatedCount = validationRuleUpdater.updateValidationRules(
                        classificationAttributesPath, validationRulesPath);
                
                System.out.println("Validation rules updated successfully");
                System.out.println("Updated " + updatedCount + " rules");
                
            } catch (Exception e) {
                System.err.println("Error updating validation rules: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
            
            System.out.println("Validation rule update completed");
            System.exit(0);
        };
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
