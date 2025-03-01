package com.scaler.validation.fact;

import com.scaler.entity.ProductFeature;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductFeatureValidationFact {
    private ProductFeature feature;
    
    @Builder.Default
    private List<String> errors = new ArrayList<>();
    
    public void addError(String error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(error);
    }
}
