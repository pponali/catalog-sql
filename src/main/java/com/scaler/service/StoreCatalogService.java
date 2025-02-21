package com.scaler.service;

import com.scaler.dto.SiteCatalogDTO;
import java.util.List;
import java.util.UUID;

public interface StoreCatalogService {
    SiteCatalogDTO assignCatalogToSite(UUID siteId, UUID catalogId, boolean isDefault);
    void removeCatalogFromStore(UUID siteId, UUID catalogId);
    List<SiteCatalogDTO> getCatalogsBySite(UUID siteId);
    SiteCatalogDTO getDefaultCatalog(UUID siteId);
    void setDefaultCatalog(UUID siteId, UUID catalogId);
    boolean validateCatalogAccess(UUID siteId, UUID catalogId);
}
