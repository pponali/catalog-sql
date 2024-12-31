package com.scaler.validation.fact;

import com.scaler.enums.FeatureValueType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FeatureValidationFact {
    private String featureCode;
    private String value;
    private FeatureValueType valueType;
    private boolean isList;
    private boolean valid;
    
    @Builder.Default
    private List<String> validationErrors = new ArrayList<>();
    
    public void addValidationError(String error) {
        if (validationErrors == null) {
            validationErrors = new ArrayList<>();
        }
        validationErrors.add(error);
        valid = false;
    }
}
