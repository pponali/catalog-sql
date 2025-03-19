package com.scaler.dto;

import com.scaler.entity.ValidationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResultDTO extends BaseDTO {
    
    @NotNull(message = "Entity ID is required")
    private UUID entityId;
    
    @NotNull(message = "Entity type is required")
    private String entityType;
    
    private String fieldName;
    
    private ValidationStatus status;
    
    @lombok.Builder.Default
    private List<String> errors = new ArrayList<>();

    
    public void addError(String error) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(error);
        status = ValidationStatus.FAILED;
    }
    
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }
}
