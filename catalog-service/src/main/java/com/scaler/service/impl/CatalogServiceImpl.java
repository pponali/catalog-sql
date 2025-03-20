package com.scaler.service.impl;

import com.scaler.dto.CatalogDTO;
import com.scaler.entity.Catalog;
import com.scaler.repository.CatalogRepository;
import com.scaler.service.CatalogService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;

    @Override
    @Transactional
    public CatalogDTO createCatalog(CatalogDTO catalogDTO) {
        log.info("Creating new catalog: {}", catalogDTO);
        Catalog catalog = mapToEntity(catalogDTO);
        Catalog savedCatalog = catalogRepository.save(catalog);
        return mapToDTO(savedCatalog);
    }

    @Override
    @Transactional
    public CatalogDTO updateCatalog(UUID id, CatalogDTO catalogDTO) {
        log.info("Updating catalog with id: {}", id);
        
        return catalogRepository.findById(id)
                .map(catalog -> {
                    catalog.setCode(catalogDTO.getCode());
                    catalog.setName(catalogDTO.getName());
                    catalog.setDescription(catalogDTO.getDescription());
                    catalog.setStatus(catalogDTO.getStatus());
                    catalog.setType(catalogDTO.getType());
                    
                    if (catalogDTO.getBusinessId() != null) {
                        // Set business relationship if needed
                    }
                    
                    Catalog updatedCatalog = catalogRepository.save(catalog);
                    return mapToDTO(updatedCatalog);
                })
                .orElseThrow(() -> new EntityNotFoundException("Catalog not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public CatalogDTO getCatalog(UUID id) {
        log.info("Fetching catalog with id: {}", id);
        return catalogRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Catalog not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogDTO> getAllCatalogs() {
        log.info("Fetching all catalogs");
        return catalogRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CatalogDTO> getCatalogsByBusiness(UUID businessId) {
        log.info("Fetching catalogs for business id: {}", businessId);
        return catalogRepository.findByBusinessId(businessId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCatalog(UUID id) {
        log.info("Deleting catalog with id: {}", id);
        if (!catalogRepository.existsById(id)) {
            throw new EntityNotFoundException("Catalog not found with id: " + id);
        }
        catalogRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return catalogRepository.existsById(id);
    }

    /**
     * Maps a Catalog entity to a CatalogDTO
     */
    private CatalogDTO mapToDTO(Catalog catalog) {
        return CatalogDTO.builder()
                .id(catalog.getId())
                .code(catalog.getCode())
                .name(catalog.getName())
                .description(catalog.getDescription())
                .status(catalog.getStatus())
                .type(catalog.getType())
                .businessId(catalog.getBusiness() != null ? catalog.getBusiness().getId() : null)
                .createdDate(catalog.getCreatedDate() != null ? catalog.getCreatedDate().toString() : null)
                .lastModifiedDate(catalog.getLastModifiedDate() != null ? catalog.getLastModifiedDate().toString() : null)
                .createdBy(catalog.getCreatedBy())
                .lastModifiedBy(catalog.getLastModifiedBy())
                .build();
    }

    /**
     * Maps a CatalogDTO to a Catalog entity
     */
    private Catalog mapToEntity(CatalogDTO catalogDTO) {
        return Catalog.builder()
                .id(catalogDTO.getId())
                .code(catalogDTO.getCode())
                .name(catalogDTO.getName())
                .description(catalogDTO.getDescription())
                .status(catalogDTO.getStatus())
                .type(catalogDTO.getType())
                // Business relationship would be set separately if needed
                .build();
    }
}
