package com.scaler.controller;

import com.scaler.dto.ImportResult;
import com.scaler.service.DataImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class DataImportController {

    private final DataImportService importService;

    @PostMapping
    public ResponseEntity<ImportResult> importData(@RequestParam("file") MultipartFile file) {
        ImportResult result = importService.importData(file);
        return ResponseEntity.ok(result);
    }
}
