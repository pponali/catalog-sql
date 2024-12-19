package com.example.validation.fact;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class CategoryValidationFact {
    private String categoryCode;
    private String featureCode;
    private String value;
    private Map<String, Object> metadata;
    
    @Builder.Default
    private List<String> errors = new ArrayList<>();
    
    public void addError(String error) {
        errors.add(error);
    }
}
