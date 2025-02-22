package com.scaler.service.impl;

import com.scaler.dto.StoreCatalogDTO;
import com.scaler.entity.Catalog;
import com.scaler.entity.Store;
import com.scaler.entity.StoreCatalog;
import com.scaler.exception.BusinessException;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.mapper.StoreCatalogMapper;
import com.scaler.repository.CatalogRepository;
import com.scaler.repository.StoreCatalogRepository;
import com.scaler.repository.StoreRepository;
import com.scaler.service.StoreCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreCatalogServiceImpl implements StoreCatalogService {

    private final StoreCatalogRepository storeCatalogRepository;
    private final StoreRepository storeRepository;
    private final CatalogRepository catalogRepository;
    private final StoreCatalogMapper storeCatalogMapper;

    @Override
    @Transactional
    public StoreCatalogDTO assignCatalogToSite(UUID storeId, UUID catalogId, boolean isDefault) {
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new ResourceNotFoundException("Site not found with id: " + storeId));
        
        Catalog catalog = catalogRepository.findById(catalogId)
            .orElseThrow(() -> new ResourceNotFoundException("Catalog not found with id: " + catalogId));

        // Validate that the catalog belongs to the same Merchant as the site
        if (!catalog.getBusiness().equals(store.getMerchant())) {
            throw new BusinessException("Cannot assign catalog to site from different business");
        }

        // If setting as default, unset any existing default
        if (isDefault) {
            storeCatalogRepository.findByStoreIdAndIsDefaultTrue(storeId)
                .ifPresent(existingDefault -> {
                    existingDefault.setIsDefault(false);
                    storeCatalogRepository.save(existingDefault);
                });
        }

        StoreCatalog storeCatalog = new StoreCatalog();
        storeCatalog.setId(UUID.randomUUID());
        storeCatalog.setStore(store);
        storeCatalog.setCatalog(catalog);
        storeCatalog.setIsDefault(isDefault);

        storeCatalog = storeCatalogRepository.save(storeCatalog);
        return storeCatalogMapper.toDTO(storeCatalog);
    }

    @Override
    @Transactional
    public void removeCatalogFromStore(UUID storeId, UUID catalogId) {
        StoreCatalog storeCatalog = storeCatalogRepository.findByStoreIdAndCatalogId(storeId, catalogId)
            .orElseThrow(() -> new ResourceNotFoundException("Store-Catalog mapping not found"));

        if (storeCatalog.getIsDefault()) {
            throw new BusinessException("Cannot remove default catalog from Store");
        }

        storeCatalogRepository.delete(storeCatalog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreCatalogDTO> getCatalogsBySite(UUID siteId) {
        if (!storeRepository.existsById(siteId)) {
            throw new ResourceNotFoundException("Site not found with id: " + siteId);
        }

        return storeCatalogRepository.findByStoreId(siteId).stream()
            .map(storeCatalogMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StoreCatalogDTO getDefaultCatalog(UUID siteId) {
        return storeCatalogRepository.findByStoreIdAndIsDefaultTrue(siteId)
            .map(storeCatalogMapper::toDTO)
            .orElseThrow(() -> new ResourceNotFoundException("No default catalog found for site: " + siteId));
    }

    @Override
    @Transactional
    public void setDefaultCatalog(UUID siteId, UUID catalogId) {
        // First, verify the mapping exists
        StoreCatalog newDefault = storeCatalogRepository.findByStoreIdAndCatalogId(siteId, catalogId)
            .orElseThrow(() -> new ResourceNotFoundException("Site-Catalog mapping not found"));

        // Unset any existing default
        storeCatalogRepository.findByStoreIdAndIsDefaultTrue(siteId)
            .ifPresent(existingDefault -> {
                existingDefault.setIsDefault(false);
                storeCatalogRepository.save(existingDefault);
            });

        // Set new default
        newDefault.setIsDefault(true);
        storeCatalogRepository.save(newDefault);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateCatalogAccess(UUID siteId, UUID catalogId) {
        return storeCatalogRepository.findByStoreIdAndCatalogId(siteId, catalogId).isPresent();
    }
}
