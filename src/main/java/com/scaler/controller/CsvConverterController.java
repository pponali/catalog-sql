package com.scaler.controller;

import com.opencsv.exceptions.CsvException;
import com.scaler.util.CsvFormatConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Controller for CSV conversion operations
 */
@Slf4j
@RestController
@RequestMapping("/api/csv")
@RequiredArgsConstructor
public class CsvConverterController {

    private final CsvFormatConverter csvFormatConverter;

    /**
     * Converts the DevMDD_Classification_Category.csv file to categories.csv format
     * 
     * @return ResponseEntity with success or error message
     */
    @PostMapping("/convert/categories")
    public ResponseEntity<String> convertCategories() {
        try {
            csvFormatConverter.convertClassificationCategoriesToCategoriesFormat();
            return ResponseEntity.ok("Categories CSV conversion completed successfully");
        } catch (IOException | CsvException e) {
            log.error("Error converting categories CSV", e);
            return ResponseEntity.internalServerError().body("Error converting categories CSV: " + e.getMessage());
        }
    }

    /**
     * Converts all DevMDD CSV files to the application's format
     * 
     * @return ResponseEntity with success or error message
     */
    @PostMapping("/convert/all")
    public ResponseEntity<String> convertAllCsvFiles() {
        try {
            csvFormatConverter.convertAllDevMddCsvFiles();
            return ResponseEntity.ok("All CSV conversions completed successfully");
        } catch (IOException | CsvException e) {
            log.error("Error converting CSV files", e);
            return ResponseEntity.internalServerError().body("Error converting CSV files: " + e.getMessage());
        }
    }
}
