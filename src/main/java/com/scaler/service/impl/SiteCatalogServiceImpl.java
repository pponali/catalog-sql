package com.scaler.service.impl;

import com.scaler.dto.SiteCatalogDTO;
import com.scaler.entity.Catalog;
import com.scaler.entity.Site;
import com.scaler.entity.SiteCatalog;
import com.scaler.entity.SiteCatalogId;
import com.scaler.exception.BusinessException;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.mapper.SiteCatalogMapper;
import com.scaler.repository.CatalogRepository;
import com.scaler.repository.SiteCatalogRepository;
import com.scaler.repository.SiteRepository;
import com.scaler.service.SiteCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SiteCatalogServiceImpl implements SiteCatalogService {

    private final SiteCatalogRepository siteCatalogRepository;
    private final SiteRepository siteRepository;
    private final CatalogRepository catalogRepository;
    private final SiteCatalogMapper siteCatalogMapper;

    @Override
    @Transactional
    public SiteCatalogDTO assignCatalogToSite(UUID siteId, UUID catalogId, boolean isDefault) {
        Site site = siteRepository.findById(siteId)
            .orElseThrow(() -> new ResourceNotFoundException("Site not found with id: " + siteId));
        
        Catalog catalog = catalogRepository.findById(catalogId)
            .orElseThrow(() -> new ResourceNotFoundException("Catalog not found with id: " + catalogId));

        // Validate that the catalog belongs to the same business as the site
        if (!catalog.getBusiness().equals(site.getBusiness())) {
            throw new BusinessException("Cannot assign catalog to site from different business");
        }

        // If setting as default, unset any existing default
        if (isDefault) {
            siteCatalogRepository.findBySiteIdAndIsDefaultTrue(siteId)
                .ifPresent(existingDefault -> {
                    existingDefault.setIsDefault(false);
                    siteCatalogRepository.save(existingDefault);
                });
        }

        SiteCatalog siteCatalog = new SiteCatalog();
        siteCatalog.setId(UUID.randomUUID());
        siteCatalog.setSite(site);
        siteCatalog.setCatalog(catalog);
        siteCatalog.setIsDefault(isDefault);

        siteCatalog = siteCatalogRepository.save(siteCatalog);
        return siteCatalogMapper.toDTO(siteCatalog);
    }

    @Override
    @Transactional
    public void removeCatalogFromSite(UUID siteId, UUID catalogId) {
        SiteCatalog siteCatalog = siteCatalogRepository.findBySiteIdAndCatalogId(siteId, catalogId)
            .orElseThrow(() -> new ResourceNotFoundException("Site-Catalog mapping not found"));

        if (siteCatalog.getIsDefault()) {
            throw new BusinessException("Cannot remove default catalog from site");
        }

        siteCatalogRepository.delete(siteCatalog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteCatalogDTO> getCatalogsBySite(UUID siteId) {
        if (!siteRepository.existsById(siteId)) {
            throw new ResourceNotFoundException("Site not found with id: " + siteId);
        }

        return siteCatalogRepository.findBySiteId(siteId).stream()
            .map(siteCatalogMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SiteCatalogDTO getDefaultCatalog(UUID siteId) {
        return siteCatalogRepository.findBySiteIdAndIsDefaultTrue(siteId)
            .map(siteCatalogMapper::toDTO)
            .orElseThrow(() -> new ResourceNotFoundException("No default catalog found for site: " + siteId));
    }

    @Override
    @Transactional
    public void setDefaultCatalog(UUID siteId, UUID catalogId) {
        // First, verify the mapping exists
        SiteCatalog newDefault = siteCatalogRepository.findBySiteIdAndCatalogId(siteId, catalogId)
            .orElseThrow(() -> new ResourceNotFoundException("Site-Catalog mapping not found"));

        // Unset any existing default
        siteCatalogRepository.findBySiteIdAndIsDefaultTrue(siteId)
            .ifPresent(existingDefault -> {
                existingDefault.setIsDefault(false);
                siteCatalogRepository.save(existingDefault);
            });

        // Set new default
        newDefault.setIsDefault(true);
        siteCatalogRepository.save(newDefault);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateCatalogAccess(UUID siteId, UUID catalogId) {
        return siteCatalogRepository.findBySiteIdAndCatalogId(siteId, catalogId).isPresent();
    }
}
