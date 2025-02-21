package com.scaler.repository;

import com.scaler.entity.SiteCatalog;
import com.scaler.entity.SiteCatalogId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreCatalogRepository extends JpaRepository<SiteCatalog, SiteCatalogId> {
    List<SiteCatalog> findBySiteId(UUID siteId);
    List<SiteCatalog> findByCatalogId(UUID catalogId);
    Optional<SiteCatalog> findBySiteIdAndIsDefaultTrue(UUID siteId);

    Optional<SiteCatalog> findBySiteIdAndCatalogId(UUID siteId, UUID catalogId);
}
