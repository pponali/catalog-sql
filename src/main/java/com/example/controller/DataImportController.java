package com.example.controller;

import com.example.service.DataImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class DataImportController {

    private final DataImportService dataImportService;

    @PostMapping("/data")
    public ResponseEntity<String> importData(@RequestBody String data) {
        try {
            dataImportService.importData(data);
            return ResponseEntity.ok("Data imported successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to import data: " + e.getMessage());
        }
    }
}
