package com.nosql.poc.rules.exception;

import com.nosql.poc.rules.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<String>> handleResponseStatusException(ResponseStatusException ex) {
        log.error("Response status exception: {}", ex.getMessage());
        
        ApiResponse<String> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(ex.getReason());
        response.setErrorCode(String.valueOf(ex.getStatusCode().value()));
        
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        log.error("Validation error: {}", ex.getMessage());
        
        BindingResult result = ex.getBindingResult();
        Map<String, String> errors = result.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage() == null ? "Invalid value" : fieldError.getDefaultMessage()
                ));
        
        ApiResponse<Map<String, String>> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage("Validation failed");
        response.setErrorCode("VALIDATION_ERROR");
        response.setData(errors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    @ExceptionHandler(RuleValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuleValidationException(RuleValidationException ex) {
        log.error("Rule validation error: {}", ex.getMessage());
        
        ApiResponse<Object> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        response.setErrorCode("RULE_VALIDATION_ERROR");
        response.setData(ex.getErrors());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("type", ex.getClass().getSimpleName());
        errorDetails.put("detail", ex.getMessage());
        
        ApiResponse<Map<String, Object>> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage("An unexpected error occurred");
        response.setErrorCode("INTERNAL_SERVER_ERROR");
        response.setData(errorDetails);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}