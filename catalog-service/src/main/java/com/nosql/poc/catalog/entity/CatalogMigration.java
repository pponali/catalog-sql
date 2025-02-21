package com.nosql.poc.catalog.entity;

import com.scaler.enums.MigrationStatus;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document(collection = "catalog_migration")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CatalogMigration extends BaseEntity {

    private UUID sourceCatalogId;
    private UUID targetCatalogId;
    private LocalDateTime migrationTime;
    private MigrationStatus status;
    private String message;
    private int categoriesMigrated;
    private int productsMigrated;
    private List<String> warnings;
    private List<String> errors;
}
