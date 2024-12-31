package com.scaler.service;

import com.scaler.dto.SiteCatalogDTO;
import java.util.List;
import java.util.UUID;

public interface SiteCatalogService {
    SiteCatalogDTO assignCatalogToSite(UUID siteId, UUID catalogId, boolean isDefault);
    void removeCatalogFromSite(UUID siteId, UUID catalogId);
    List<SiteCatalogDTO> getCatalogsBySite(UUID siteId);
    SiteCatalogDTO getDefaultCatalog(UUID siteId);
    void setDefaultCatalog(UUID siteId, UUID catalogId);
    boolean validateCatalogAccess(UUID siteId, UUID catalogId);
}
