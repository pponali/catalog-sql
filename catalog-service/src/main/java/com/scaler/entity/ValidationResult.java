package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "validation_result")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ValidationResult extends BaseEntity {
    
    @Column(name = "entity_id", nullable = false)
    private UUID entityId;
    
    @Column(name = "entity_type", nullable = false)
    private String entityType;
    
    @Column(name = "field_name")
    private String fieldName;
    
    @Column(name = "validation_status")
    @Enumerated(EnumType.STRING)
    private ValidationStatus status;
    
    @ElementCollection
    @CollectionTable(
        name = "validation_errors",
        joinColumns = @JoinColumn(name = "validation_result_id")
    )
    @Column(name = "error_message")
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
