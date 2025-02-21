package com.scaler.service.impl;

import com.scaler.dto.SiteDTO;
import com.scaler.entity.Store;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.mapper.SiteMapper;
import com.scaler.repository.MerchantRepository;
import com.scaler.repository.StoreRepository;
import com.scaler.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
    
    private final StoreRepository storeRepository;
    private final MerchantRepository merchantRepository;
    private final SiteMapper siteMapper;

    @Override
    @Transactional
    public SiteDTO createSite(SiteDTO siteDTO) {
        if (!merchantRepository.existsById(siteDTO.getMerchantId())) {
            throw new ResourceNotFoundException("Merchant not found with id: " + siteDTO.getMerchantId());
        }

        Store store = siteMapper.toEntity(siteDTO);
        store = storeRepository.save(store);
        return siteMapper.toDTO(store);
    }

    @Override
    @Transactional
    public SiteDTO updateSite(UUID id, SiteDTO siteDTO) {
        Store store = storeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Site not found with id: " + id));
        
        if (!merchantRepository.existsById(siteDTO.getMerchantId())) {
            throw new ResourceNotFoundException("Merchant not found with id: " + siteDTO.getMerchantId());
        }

        store.setName(siteDTO.getName());
        store.setDomain(siteDTO.getDomain());
        store.setLocale(siteDTO.getLocale());
        store.setCurrency(siteDTO.getCurrency());
        
        store = storeRepository.save(store);
        return siteMapper.toDTO(store);
    }

    @Override
    @Transactional(readOnly = true)
    public SiteDTO getSite(UUID id) {
        Store store = storeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Site not found with id: " + id));
        return siteMapper.toDTO(store);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteDTO> getAllSites() {
        return storeRepository.findAll().stream()
            .map(siteMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteDTO> getSitesByBusiness(UUID businessId) {
        if (!merchantRepository.existsById(businessId)) {
            throw new ResourceNotFoundException("Merchant not found with id: " + businessId);
        }
        return storeRepository.findByMerchantId(businessId).stream()
            .map(siteMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSite(UUID id) {
        if (!storeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Site not found with id: " + id);
        }
        storeRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return storeRepository.existsById(id);
    }
}
