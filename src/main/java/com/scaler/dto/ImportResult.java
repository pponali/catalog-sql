package com.scaler.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ImportResult {
    private int totalRecords;
    private int successCount;
    private int failureCount;
    private List<String> errors;
}
