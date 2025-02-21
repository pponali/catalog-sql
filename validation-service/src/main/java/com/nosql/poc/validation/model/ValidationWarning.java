package com.nosql.poc.validation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationWarning {
    private String rule;
    private String message;
    private String field;
    private Object value;
    private String code;
    private ValidationSeverity severity = ValidationSeverity.WARNING;
    
    public ValidationWarning(String rule, String message) {
        this.rule = rule;
        this.message = message;
    }
    
    public ValidationWarning(String rule, String message, String field) {
        this.rule = rule;
        this.message = message;
        this.field = field;
    }
}
