package com.scaler.entity;

import com.scaler.enums.MigrationStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "catalog_migration")
@Data
public class CatalogMigration {
    @Id
    private UUID id;

    @Column(name = "source_catalog_id", nullable = false)
    private UUID sourceCatalogId;

    @Column(name = "target_catalog_id", nullable = false)
    private UUID targetCatalogId;

    @Column(name = "migration_time", nullable = false)
    private LocalDateTime migrationTime;

    @Column(name = "status", nullable = false)
    private MigrationStatus status;

    @Column(name = "message")
    private String message;

    @Column(name = "categories_migrated")
    private int categoriesMigrated;

    @Column(name = "products_migrated")
    private int productsMigrated;

    @Column(name = "warnings")
    @ElementCollection
    private List<String> warnings;

    @Column(name = "errors")
    @ElementCollection
    private List<String> errors;

    @Column(name = "migrated_category_ids")
    @ElementCollection
    private List<UUID> migratedCategoryIds;

    @Column(name = "migrated_product_ids")
    @ElementCollection
    private List<UUID> migratedProductIds;

    @Column(name = "failed_category_ids")
    @ElementCollection
    private List<UUID> failedCategoryIds;

    @Column(name = "failed_product_ids")
    @ElementCollection
    private List<UUID> failedProductIds;

    @Column(name = "migration_notes")
    private String migrationNotes;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "performed_by")
    private String performedBy;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (migrationTime == null) {
            migrationTime = LocalDateTime.now();
        }
    }
}
