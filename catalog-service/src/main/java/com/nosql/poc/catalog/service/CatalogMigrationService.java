package com.nosql.poc.catalog.service;

import com.scaler.dto.MigrationRequestDTO;
import com.scaler.dto.MigrationResultDTO;

import java.util.List;
import java.util.UUID;

public interface CatalogMigrationService {
    /**
     * Migrate categories from one catalog to another
     */
    MigrationResultDTO migrateCategories(UUID sourceCatalogId, UUID targetCatalogId, List<UUID> categoryIds);
    
    /**
     * Migrate products from one catalog to another
     */
    MigrationResultDTO migrateProducts(UUID sourceCatalogId, UUID targetCatalogId, List<UUID> productIds);
    
    /**
     * Bulk migrate both categories and products
     */
    MigrationResultDTO bulkMigrate(MigrationRequestDTO request);
    
    /**
     * Validate if migration is possible
     */
    boolean validateMigration(UUID sourceCatalogId, UUID targetCatalogId);
    
    /**
     * Get migration history for a catalog
     */
    List<MigrationResultDTO> getMigrationHistory(UUID catalogId);
}
