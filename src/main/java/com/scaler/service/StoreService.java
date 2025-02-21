package com.scaler.service;

import com.scaler.dto.SiteDTO;
import java.util.List;
import java.util.UUID;

public interface StoreService {
    SiteDTO createSite(SiteDTO siteDTO);
    SiteDTO updateSite(UUID id, SiteDTO siteDTO);
    SiteDTO getSite(UUID id);
    List<SiteDTO> getAllSites();
    List<SiteDTO> getSitesByBusiness(UUID businessId);
    void deleteSite(UUID id);
    boolean existsById(UUID id);
}
