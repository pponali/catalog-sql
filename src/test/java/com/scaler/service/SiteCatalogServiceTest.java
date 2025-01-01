package com.scaler.service;

import com.scaler.dto.SiteCatalogDTO;
import com.scaler.entity.Business;
import com.scaler.entity.Catalog;
import com.scaler.entity.Site;
import com.scaler.entity.SiteCatalog;
import com.scaler.exception.BusinessException;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.mapper.SiteCatalogMapper;
import com.scaler.repository.BusinessRepository;
import com.scaler.repository.CatalogRepository;
import com.scaler.repository.SiteCatalogRepository;
import com.scaler.repository.SiteRepository;
import com.scaler.service.impl.SiteCatalogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SiteCatalogServiceTest {

    @Mock
    private SiteCatalogRepository siteCatalogRepository;

    @Mock
    private SiteRepository siteRepository;

    @Mock
    private CatalogRepository catalogRepository;

    @Mock
    private SiteCatalogMapper siteCatalogMapper;

    @InjectMocks
    private SiteCatalogServiceImpl siteCatalogService;

    private Business business;
    private Site site;
    private Catalog catalog;
    private SiteCatalog siteCatalog;
    private SiteCatalogDTO siteCatalogDTO;
    private UUID businessId;
    private UUID siteId;
    private UUID catalogId;

    @BeforeEach
    void setUp() {
        businessId = UUID.randomUUID();
        siteId = UUID.randomUUID();
        catalogId = UUID.randomUUID();

        business = Business.builder()
                .id(businessId)
                .name("Test Business")
                .build();

        site = Site.builder()
                .id(siteId)
                .name("Test Site")
                .business(business)
                .build();

        catalog = Catalog.builder()
                .id(catalogId)
                .name("Test Catalog")
                .business(business)
                .build();

        siteCatalog = SiteCatalog.builder()
                .id(UUID.randomUUID())
                .site(site)
                .catalog(catalog)
                .isDefault(true)
                .build();

        siteCatalogDTO = SiteCatalogDTO.builder()
                .id(siteCatalog.getId())
                .siteId(siteId)
                .catalogId(catalogId)
                .isDefault(true)
                .build();
    }

    @Test
    void assignCatalogToSite_Success() {
        when(siteRepository.findById(siteId)).thenReturn(Optional.of(site));
        when(catalogRepository.findById(catalogId)).thenReturn(Optional.of(catalog));
        when(siteCatalogRepository.save(any(SiteCatalog.class))).thenReturn(siteCatalog);
        when(siteCatalogMapper.toDTO(any(SiteCatalog.class))).thenReturn(siteCatalogDTO);

        SiteCatalogDTO result = siteCatalogService.assignCatalogToSite(siteId, catalogId, true);

        assertNotNull(result);
        assertEquals(siteCatalog.getId(), result.getId());
        assertTrue(result.getIsDefault());
        verify(siteCatalogRepository).save(any(SiteCatalog.class));
    }

    @Test
    void assignCatalogToSite_DifferentBusinesses_ThrowsException() {
        Business differentBusiness = Business.builder()
                .id(UUID.randomUUID())
                .name("Different Business")
                .build();

        catalog = Catalog.builder()
                .id(catalogId)
                .name("Test Catalog")
                .business(differentBusiness)
                .build();

        when(siteRepository.findById(siteId)).thenReturn(Optional.of(site));
        when(catalogRepository.findById(catalogId)).thenReturn(Optional.of(catalog));

        assertThrows(BusinessException.class, () -> siteCatalogService.assignCatalogToSite(siteId, catalogId, true));
        verify(siteCatalogRepository, never()).save(any(SiteCatalog.class));
    }

    @Test
    void assignCatalogToSite_SiteNotFound_ThrowsException() {
        when(siteRepository.findById(siteId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> siteCatalogService.assignCatalogToSite(siteId, catalogId, true));
        verify(siteCatalogRepository, never()).save(any(SiteCatalog.class));
    }

    @Test
    void assignCatalogToSite_CatalogNotFound_ThrowsException() {
        when(siteRepository.findById(siteId)).thenReturn(Optional.of(site));
        when(catalogRepository.findById(catalogId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> siteCatalogService.assignCatalogToSite(siteId, catalogId, true));
        verify(siteCatalogRepository, never()).save(any(SiteCatalog.class));
    }
}
