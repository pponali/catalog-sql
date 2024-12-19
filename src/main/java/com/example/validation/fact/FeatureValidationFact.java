package com.example.validation.fact;

import com.example.enums.FeatureValueType;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class FeatureValidationFact {
    private String featureCode;
    private FeatureValueType valueType;
    private String value;
    private boolean isList;
    @Builder.Default
    private List<String> validationErrors = new ArrayList<>();
    private boolean valid;

    public void addError(String error) {
        validationErrors.add(error);
        valid = false;
    }
}
