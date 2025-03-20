package com.scaler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportResult {
    private String entityType;
    private boolean success;
    private String errorMessage;
    private int importedCount;
    
    @Builder.Default
    private List<String> errors = new ArrayList<>();
    
    private int totalRecords;
    private int successCount;
    private int failureCount;
    
    public void setImportedCount(int count) {
        this.importedCount = count;
        this.successCount = count;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}