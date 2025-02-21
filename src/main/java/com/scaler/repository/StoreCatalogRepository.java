package com.scaler.repository;

import com.scaler.entity.SiteCatalogId;
import com.scaler.entity.StoreCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreCatalogRepository extends JpaRepository<StoreCatalog, SiteCatalogId> {
    List<StoreCatalog> findByStoreId(UUID siteId);
    List<StoreCatalog> findByCatalogId(UUID catalogId);
    Optional<StoreCatalog> findByStoreIdAndIsDefaultTrue(UUID storeId);

    Optional<StoreCatalog> findByStoreIdAndCatalogId(UUID siteId, UUID catalogId);
}
