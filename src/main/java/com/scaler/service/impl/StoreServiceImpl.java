package com.scaler.service.impl;

import com.scaler.dto.SiteDTO;
import com.scaler.entity.Site;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.mapper.SiteMapper;
import com.scaler.repository.MerchantRepository;
import com.scaler.repository.SiteRepository;
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
    
    private final SiteRepository siteRepository;
    private final MerchantRepository merchantRepository;
    private final SiteMapper siteMapper;

    @Override
    @Transactional
    public SiteDTO createSite(SiteDTO siteDTO) {
        if (!merchantRepository.existsById(siteDTO.getMerchantId())) {
            throw new ResourceNotFoundException("Merchant not found with id: " + siteDTO.getMerchantId());
        }

        Site site = siteMapper.toEntity(siteDTO);
        site = siteRepository.save(site);
        return siteMapper.toDTO(site);
    }

    @Override
    @Transactional
    public SiteDTO updateSite(UUID id, SiteDTO siteDTO) {
        Site site = siteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Site not found with id: " + id));
        
        if (!merchantRepository.existsById(siteDTO.getMerchantId())) {
            throw new ResourceNotFoundException("Merchant not found with id: " + siteDTO.getMerchantId());
        }

        site.setName(siteDTO.getName());
        site.setDomain(siteDTO.getDomain());
        site.setLocale(siteDTO.getLocale());
        site.setCurrency(siteDTO.getCurrency());
        
        site = siteRepository.save(site);
        return siteMapper.toDTO(site);
    }

    @Override
    @Transactional(readOnly = true)
    public SiteDTO getSite(UUID id) {
        Site site = siteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Site not found with id: " + id));
        return siteMapper.toDTO(site);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteDTO> getAllSites() {
        return siteRepository.findAll().stream()
            .map(siteMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteDTO> getSitesByBusiness(UUID businessId) {
        if (!merchantRepository.existsById(businessId)) {
            throw new ResourceNotFoundException("Merchant not found with id: " + businessId);
        }
        return siteRepository.findByBusinessId(businessId).stream()
            .map(siteMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSite(UUID id) {
        if (!siteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Site not found with id: " + id);
        }
        siteRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return siteRepository.existsById(id);
    }
}
