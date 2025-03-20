package com.nosql.poc.channel.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Audit information for tracking entity changes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditInfo {
    /**
     * Creation date.
     */
    private LocalDateTime createdAt;
    
    /**
     * User who created the entity.
     */
    private String createdBy;
    
    /**
     * Last modification date.
     */
    private LocalDateTime updatedAt;
    
    /**
     * User who last modified the entity.
     */
    private String updatedBy;
    
    /**
     * Audit trail of changes.
     */
    @Builder.Default
    private List<AuditEntry> auditTrail = new ArrayList<>();
}
