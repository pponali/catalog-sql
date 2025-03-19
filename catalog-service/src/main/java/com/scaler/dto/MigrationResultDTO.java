package com.scaler.dto;

import com.scaler.enums.MigrationStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class MigrationResultDTO {
    private UUID migrationId;
    private UUID sourceCatalogId;
    private UUID targetCatalogId;
    private LocalDateTime migrationTime;
    private MigrationStatus status;
    private String message;
    
    private int categoriesMigrated;
    private int productsMigrated;
    private List<String> warnings;
    private List<String> errors;
    
    private List<UUID> migratedCategoryIds;
    private List<UUID> migratedProductIds;
    private List<UUID> failedCategoryIds;
    private List<UUID> failedProductIds;
    
    private String migrationNotes;
    private LocalDateTime completedAt;
    private String performedBy;
}
