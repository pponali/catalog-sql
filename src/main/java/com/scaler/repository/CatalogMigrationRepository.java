package com.scaler.repository;

import com.scaler.entity.CatalogMigration;
import com.scaler.enums.MigrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CatalogMigrationRepository extends JpaRepository<CatalogMigration, UUID> {
    List<CatalogMigration> findBySourceCatalogIdOrTargetCatalogId(UUID sourceCatalogId, UUID targetCatalogId);
    List<CatalogMigration> findBySourceCatalogId(UUID sourceCatalogId);
    List<CatalogMigration> findByTargetCatalogId(UUID targetCatalogId);
    List<CatalogMigration> findByStatus(MigrationStatus status);
}
