package com.nosql.poc.catalog;

import com.scaler.dto.MigrationRequestDTO;
import com.scaler.dto.MigrationResultDTO;

import java.util.List;
import java.util.UUID;

public interface CatalogMigrationService {
    MigrationResultDTO migrateCategories(UUID sourceCatalogId, UUID targetCatalogId, List<UUID> categoryIds);
    MigrationResultDTO migrateProducts(UUID sourceCatalogId, UUID targetCatalogId, List<UUID> productIds);
    MigrationResultDTO bulkMigrate(MigrationRequestDTO request);
    boolean validateMigration(UUID sourceCatalogId, UUID targetCatalogId);
    List<MigrationResultDTO> getMigrationHistory(UUID catalogId);
}
