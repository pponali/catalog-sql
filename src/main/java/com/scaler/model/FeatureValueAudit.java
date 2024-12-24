package com.scaler.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "feature_value_audit")
public class FeatureValueAudit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "feature_value_id", nullable = false)
    private Long featureValueId;
    
    @Column(name = "feature_id", nullable = false)
    private Long featureId;
    
    @Column(name = "action", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuditAction action;
    
    @Column(name = "old_value", columnDefinition = "jsonb")
    private JsonNode oldValue;
    
    @Column(name = "new_value", columnDefinition = "jsonb")
    private JsonNode newValue;
    
    @Column(name = "changed_by")
    private String changedBy;
    
    @Column(name = "change_reason")
    private String changeReason;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    public enum AuditAction {
        CREATE,
        UPDATE,
        DELETE,
        VALIDATE,
        CONVERT_UNIT,
        NORMALIZE
    }
    
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
