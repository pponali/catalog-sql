package com.scaler.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseServiceException.class)
    public ResponseEntity<ErrorResponse> handleBaseServiceException(BaseServiceException ex, WebRequest request) {
        String errorId = generateErrorId();
        log.error("Error ID: {} - Service exception: {}", errorId, ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
            errorId,
            determineHttpStatus(ex).value(),
            ex.getErrorCode(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        
        return ResponseEntity.status(determineHttpStatus(ex)).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        String errorId = generateErrorId();
        log.error("Error ID: {} - Validation error: {}", errorId, ex.getMessage(), ex);
        
        Map<String, String> validationErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            validationErrors.put(error.getField(), error.getDefaultMessage())
        );
        
        ErrorResponse errorResponse = new ErrorResponse(
            errorId,
            HttpStatus.BAD_REQUEST.value(),
            ErrorCodes.VALIDATION_ERROR,
            "Validation failed",
            LocalDateTime.now()
        );
        errorResponse.setValidationErrors(validationErrors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {
        String errorId = generateErrorId();
        log.error("Error ID: {} - Constraint violation: {}", errorId, ex.getMessage(), ex);
        
        Map<String, String> validationErrors = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(violation -> 
            validationErrors.put(violation.getPropertyPath().toString(), violation.getMessage())
        );
        
        ErrorResponse errorResponse = new ErrorResponse(
            errorId,
            HttpStatus.BAD_REQUEST.value(),
            ErrorCodes.VALIDATION_ERROR,
            "Constraint violation",
            LocalDateTime.now()
        );
        errorResponse.setValidationErrors(validationErrors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, WebRequest request) {
        String errorId = generateErrorId();
        log.error("Error ID: {} - Data integrity violation: {}", errorId, ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
            errorId,
            HttpStatus.CONFLICT.value(),
            ErrorCodes.DATA_INTEGRITY,
            "Data integrity violation",
            LocalDateTime.now()
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {
        String errorId = generateErrorId();
        log.error("Error ID: {} - Authentication error: {}", errorId, ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
            errorId,
            HttpStatus.UNAUTHORIZED.value(),
            ErrorCodes.SECURITY_UNAUTHORIZED,
            "Authentication failed",
            LocalDateTime.now()
        );
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        String errorId = generateErrorId();
        log.error("Error ID: {} - Access denied: {}", errorId, ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
            errorId,
            HttpStatus.FORBIDDEN.value(),
            ErrorCodes.SECURITY_FORBIDDEN,
            "Access denied",
            LocalDateTime.now()
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralExceptions(Exception ex, WebRequest request) {
        String errorId = generateErrorId();
        log.error("Error ID: {} - Unexpected error: {}", errorId, ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
            errorId,
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            ErrorCodes.SERVICE_ERROR,
            "An unexpected error occurred. Please contact support with the error ID.",
            LocalDateTime.now()
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private HttpStatus determineHttpStatus(BaseServiceException ex) {
        if (ex instanceof ResourceNotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else if (ex instanceof ValidationException) {
            return HttpStatus.BAD_REQUEST;
        } else if (ex instanceof ServiceException) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String generateErrorId() {
        return UUID.randomUUID().toString();
    }
}
