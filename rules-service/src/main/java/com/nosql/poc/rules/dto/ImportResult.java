package com.nosql.poc.rules.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Result of an import operation
 */
@Data
public class ImportResult {
    private String entityType;
    private boolean success;
    private int importedCount;
    private int failedCount;
    private List<String> errors;
    private String errorMessage;
    private LocalDateTime importTime = LocalDateTime.now();
}