package com.scaler.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final String errorId;
    private final int status;
    private final String errorCode;
    private final String message;
    private final LocalDateTime timestamp;
    private Map<String, String> validationErrors;
    private Map<String, Object> metadata;

    public ErrorResponse(String errorId, int status, String errorCode, String message, LocalDateTime timestamp) {
        this.errorId = errorId;
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = timestamp;
    }

    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new LinkedHashMap<>();
        }
        this.metadata.put(key, value);
    }
}
