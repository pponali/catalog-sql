package com.nosql.poc.catalog.controller;

import com.scaler.dto.MigrationRequestDTO;
import com.scaler.dto.MigrationResultDTO;
import com.scaler.service.CatalogMigrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/catalog-migrations")
@RequiredArgsConstructor
@Tag(name = "Catalog Migration", description = "APIs for managing catalog migrations")
public class CatalogMigrationController {
    private final CatalogMigrationService migrationService;

    @PostMapping("/categories")
    @Operation(summary = "Migrate categories between catalogs",
            description = "Migrate specified categories from source catalog to target catalog")
    public ResponseEntity<MigrationResultDTO> migrateCategories(
            @Parameter(description = "Source catalog ID") @RequestParam UUID sourceCatalogId,
            @Parameter(description = "Target catalog ID") @RequestParam UUID targetCatalogId,
            @Parameter(description = "List of category IDs to migrate") @RequestBody List<UUID> categoryIds) {
        return ResponseEntity.ok(migrationService.migrateCategories(sourceCatalogId, targetCatalogId, categoryIds));
    }

    @PostMapping("/products")
    @Operation(summary = "Migrate products between catalogs",
            description = "Migrate specified products from source catalog to target catalog")
    public ResponseEntity<MigrationResultDTO> migrateProducts(
            @Parameter(description = "Source catalog ID") @RequestParam UUID sourceCatalogId,
            @Parameter(description = "Target catalog ID") @RequestParam UUID targetCatalogId,
            @Parameter(description = "List of product IDs to migrate") @RequestBody List<UUID> productIds) {
        return ResponseEntity.ok(migrationService.migrateProducts(sourceCatalogId, targetCatalogId, productIds));
    }

    @PostMapping("/bulk")
    @Operation(summary = "Bulk migrate categories and products",
            description = "Migrate multiple categories and products in a single operation")
    public ResponseEntity<MigrationResultDTO> bulkMigrate(
            @Parameter(description = "Migration request details")
            @Valid @RequestBody MigrationRequestDTO migrationRequestDTO) {
        return ResponseEntity.ok(migrationService.bulkMigrate(migrationRequestDTO));
    }
}
