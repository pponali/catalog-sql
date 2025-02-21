package com.nosql.poc.copy.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "copy_tasks")
public class CopyTask {
    @Id
    private String id;
    
    private String sourceChannelId;
    private String targetChannelId;
    private String productId;
    
    private CopyStatus status;
    private String errorMessage;
    
    private Map<String, ValidationResult> validationResults;
    private Map<String, TransformationResult> transformationResults;
    
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    
    private Map<String, Object> metadata;
}

@Data
public class ValidationResult {
    private boolean valid;
    private List<String> errors;
    private List<String> warnings;
}

@Data
public class TransformationResult {
    private boolean successful;
    private String errorMessage;
    private Map<String, Object> transformedData;
}
