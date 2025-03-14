package com.scaler.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the CsvFormatConverter class
 */
class CsvFormatConverterTest {

    private CsvFormatConverter csvFormatConverter;

    @BeforeEach
    void setUp() {
        csvFormatConverter = new CsvFormatConverter();
    }

    /**
     * Tests the conversion of DevMDD_Classification_Category.csv to categories.csv
     */
    @Test
    void testConvertClassificationCategoriesToCategoriesFormat() throws IOException, CsvException {
        // Call the method to convert the CSV
        csvFormatConverter.convertClassificationCategoriesToCategoriesFormat();
        
        // Verify that the output file exists
        Path outputPath = Paths.get("src/main/resources/csv/categories.csv");
        assertTrue(Files.exists(outputPath), "Output file should exist");
        
        // Read the output file
        List<String[]> rows;
        try (CSVReader reader = new CSVReader(Files.newBufferedReader(outputPath))) {
            rows = reader.readAll();
        }
        
        // Verify the header row
        assertNotNull(rows);
        assertTrue(rows.size() > 0, "Output file should have at least one row");
        
        String[] header = rows.get(0);
        assertEquals("category_id", header[0]);
        assertEquals("code", header[1]);
        assertEquals("name", header[2]);
        assertEquals("description", header[3]);
        assertEquals("parent_id", header[4]);
        assertEquals("level", header[5]);
        assertEquals("path", header[6]);
        assertEquals("active", header[7]);
        assertEquals("metadata", header[8]);
        
        // Verify that we have data rows
        assertTrue(rows.size() > 1, "Output file should have data rows");
        
        // Verify a sample data row
        String[] dataRow = rows.get(1);
        assertNotNull(dataRow[0]); // category_id
        assertNotNull(dataRow[1]); // code
        assertNotNull(dataRow[2]); // name
        assertNotNull(dataRow[3]); // description
        // parent_id can be null
        assertNotNull(dataRow[5]); // level
        assertNotNull(dataRow[6]); // path
        assertEquals("true", dataRow[7]); // active
        assertTrue(dataRow[8].contains("displayOrder")); // metadata
    }
}
