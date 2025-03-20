package com.scaler.service;

import com.scaler.dto.SiteDTO;
import com.scaler.dto.StoreDTO;

import java.util.List;
import java.util.UUID;

public interface StoreService {
    StoreDTO createStore(StoreDTO storeDTO);
    StoreDTO updateStore(UUID id, StoreDTO siteDTO);
    StoreDTO getSite(UUID id);
    List<StoreDTO> getAllSites();
    List<StoreDTO> getSitesByBusiness(UUID businessId);
    void deleteSite(UUID id);
    boolean existsById(UUID id);
}
