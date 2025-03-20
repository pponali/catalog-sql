package com.scaler.service.impl;


import com.scaler.dto.StoreDTO;
import com.scaler.entity.Store;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.mapper.StoreMapper;
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
    private final StoreMapper storeMapper;

    @Override
    @Transactional
    public StoreDTO createStore(StoreDTO siteDTO) {
        if (!merchantRepository.existsById(siteDTO.getMerchantId())) {
            throw new ResourceNotFoundException("Merchant not found with id: " + siteDTO.getMerchantId());
        }

        Store store = storeMapper.toEntity(siteDTO);
        store = storeRepository.save(store);
        return storeMapper.toDTO(store);
    }

    @Override
    @Transactional
    public StoreDTO updateStore(UUID id, StoreDTO storeDTO) {
        Store store = storeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Site not found with id: " + id));
        
        if (!merchantRepository.existsById(storeDTO.getMerchantId())) {
            throw new ResourceNotFoundException("Merchant not found with id: " + storeDTO.getMerchantId());
        }

        store.setName(storeDTO.getName());
        store.setDomain(storeDTO.getDomain());
        store.setLocale(storeDTO.getLocale());
        store.setCurrency(storeDTO.getCurrency());
        
        store = storeRepository.save(store);
        return storeMapper.toDTO(store);
    }

    @Override
    @Transactional(readOnly = true)
    public StoreDTO getSite(UUID id) {
        Store store = storeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Site not found with id: " + id));
        return storeMapper.toDTO(store);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreDTO> getAllSites() {
        return storeRepository.findAll().stream()
            .map(storeMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreDTO> getSitesByBusiness(UUID businessId) {
        if (!merchantRepository.existsById(businessId)) {
            throw new ResourceNotFoundException("Merchant not found with id: " + businessId);
        }
        return storeRepository.findByMerchantId(businessId).stream()
            .map(storeMapper::toDTO)
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
