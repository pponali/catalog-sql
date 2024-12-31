package com.scaler.service.impl;

import com.scaler.dto.CatalogDTO;
import com.scaler.entity.Catalog;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.mapper.CatalogMapper;
import com.scaler.repository.BusinessRepository;
import com.scaler.repository.CatalogRepository;
import com.scaler.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {
    
    private final CatalogRepository catalogRepository;
    private final BusinessRepository businessRepository;
    private final CatalogMapper catalogMapper;

    @Override
    @Transactional
    public CatalogDTO createCatalog(CatalogDTO catalogDTO) {
        if (!businessRepository.existsById(catalogDTO.getBusinessId())) {
            throw new ResourceNotFoundException("Business not found with id: " + catalogDTO.getBusinessId());
        }

        Catalog catalog = catalogMapper.toEntity(catalogDTO);
        catalog = catalogRepository.save(catalog);
        return catalogMapper.toDTO(catalog);
    }

    @Override
    @Transactional
    public CatalogDTO updateCatalog(UUID id, CatalogDTO catalogDTO) {
        Catalog catalog = catalogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Catalog not found with id: " + id));
        
        if (!businessRepository.existsById(catalogDTO.getBusinessId())) {
            throw new ResourceNotFoundException("Business not found with id: " + catalogDTO.getBusinessId());
        }

        catalog.setName(catalogDTO.getName());
        catalog.setDescription(catalogDTO.getDescription());
        
        catalog = catalogRepository.save(catalog);
        return catalogMapper.toDTO(catalog);
    }

    @Override
    @Transactional(readOnly = true)
    public CatalogDTO getCatalog(UUID id) {
        Catalog catalog = catalogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Catalog not found with id: " + id));
        return catalogMapper.toDTO(catalog);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogDTO> getAllCatalogs() {
        return catalogRepository.findAll().stream()
            .map(catalogMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogDTO> getCatalogsByBusiness(UUID businessId) {
        if (!businessRepository.existsById(businessId)) {
            throw new ResourceNotFoundException("Business not found with id: " + businessId);
        }
        return catalogRepository.findByBusinessId(businessId).stream()
            .map(catalogMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCatalog(UUID id) {
        if (!catalogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Catalog not found with id: " + id);
        }
        catalogRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return catalogRepository.existsById(id);
    }
}
