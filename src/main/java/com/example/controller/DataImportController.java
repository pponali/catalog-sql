package com.example.controller;

import com.example.service.DataImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
@Tag(name = "Data Import", description = "APIs for importing catalog data")
public class DataImportController {

    private final DataImportService dataImportService;

    @PostMapping("/{dataFile}")
    @Operation(summary = "Import data from a JSON file")
    public ResponseEntity<String> importData(@PathVariable String dataFile) {
        try {
            dataImportService.importData(dataFile + ".json");
            return ResponseEntity.ok("Data imported successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to import data: " + e.getMessage());
        }
    }
}
