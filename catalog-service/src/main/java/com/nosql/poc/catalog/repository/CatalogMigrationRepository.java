package com.nosql.poc.catalog.repository;

import com.scaler.entity.CatalogMigration;
import com.scaler.enums.MigrationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CatalogMigrationRepository extends MongoRepository<CatalogMigration, UUID> {
    List<CatalogMigration> findBySourceCatalogIdOrTargetCatalogId(UUID sourceCatalogId, UUID targetCatalogId);
    List<CatalogMigration> findBySourceCatalogId(UUID sourceCatalogId);
    List<CatalogMigration> findByTargetCatalogId(UUID targetCatalogId);
    List<CatalogMigration> findByStatus(MigrationStatus status);
}
