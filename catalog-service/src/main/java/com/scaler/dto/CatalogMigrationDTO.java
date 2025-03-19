package com.scaler.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogMigrationDTO {
    private UUID sourceCatalogId;
    private UUID targetCatalogId;
    private String migrationNotes;
}
