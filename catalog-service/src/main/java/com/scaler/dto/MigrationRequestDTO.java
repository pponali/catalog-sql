package com.scaler.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MigrationRequestDTO {
    @NotNull(message = "Source catalog ID is required")
    private UUID sourceCatalogId;
    
    @NotNull(message = "Target catalog ID is required")
    private UUID targetCatalogId;
    
    private List<UUID> categoryIds;
    private List<UUID> productIds;
    
    private boolean includeChildren = true;
    private boolean keepSource = false;
    private String migrationNotes;
}
