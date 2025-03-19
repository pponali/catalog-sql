package com.scaler.service;

import com.scaler.dto.StoreCatalogDTO;

import java.util.List;
import java.util.UUID;

public interface StoreCatalogService {
    StoreCatalogDTO assignCatalogToSite(UUID siteId, UUID catalogId, boolean isDefault);
    void removeCatalogFromStore(UUID siteId, UUID catalogId);
    List<StoreCatalogDTO> getCatalogsBySite(UUID siteId);
    StoreCatalogDTO getDefaultCatalog(UUID siteId);
    void setDefaultCatalog(UUID siteId, UUID catalogId);
    boolean validateCatalogAccess(UUID siteId, UUID catalogId);
}
