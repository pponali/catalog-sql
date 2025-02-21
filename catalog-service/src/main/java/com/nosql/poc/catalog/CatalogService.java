package com.nosql.poc.catalog;

import com.scaler.dto.CatalogDTO;
import java.util.List;
import java.util.UUID;

public interface CatalogService {
    CatalogDTO createCatalog(CatalogDTO catalogDTO);
    CatalogDTO updateCatalog(UUID id, CatalogDTO catalogDTO);
    CatalogDTO getCatalog(UUID id);
    List<CatalogDTO> getAllCatalogs();
    List<CatalogDTO> getCatalogsByBusiness(UUID businessId);
    void deleteCatalog(UUID id);
    boolean existsById(UUID id);
}
