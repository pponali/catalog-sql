package com.scaler.service;

import com.scaler.dto.CatalogMigrationDTO;
import com.scaler.dto.MigrationRequestDTO;
import com.scaler.dto.MigrationResultDTO;
import com.scaler.entity.*;
import com.scaler.exception.BusinessException;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.repository.*;
import com.scaler.service.impl.CatalogMigrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogMigrationServiceTest {

    @Mock
    private CatalogRepository catalogRepository;
    
    @Mock
    private CategoryRepository categoryRepository;
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private BusinessRepository businessRepository;

    @Mock
    private CatalogMigrationRepository migrationRepository;

    @InjectMocks
    private CatalogMigrationServiceImpl migrationService;

    private Business business;
    private Catalog sourceCatalog;
    private Catalog targetCatalog;
    private Category category;
    private Product product;
    private UUID businessId;
    private UUID sourceCatalogId;
    private UUID targetCatalogId;
    private UUID categoryId;
    private UUID productId;

    @BeforeEach
    void setUp() {
        businessId = UUID.randomUUID();
        sourceCatalogId = UUID.randomUUID();
        targetCatalogId = UUID.randomUUID();
        categoryId = UUID.randomUUID();
        productId = UUID.randomUUID();

        business = Business.builder()
                .id(businessId)
                .name("Test Business")
                .build();

        sourceCatalog = Catalog.builder()
                .id(sourceCatalogId)
                .name("Source Catalog")
                .business(business)
                .build();

        targetCatalog = Catalog.builder()
                .id(targetCatalogId)
                .name("Target Catalog")
                .business(business)
                .build();

        category = Category.builder()
                .id(categoryId)
                .name("Test Category")
                .catalog(sourceCatalog)
                .business(business)
                .build();

        product = Product.builder()
                .id(productId)
                .name("Test Product")
                .catalog(sourceCatalog)
                .business(business)
                .build();
    }

    @Test
    void migrateCategories_Success() {
        when(catalogRepository.findById(sourceCatalogId)).thenReturn(Optional.of(sourceCatalog));
        when(catalogRepository.findById(targetCatalogId)).thenReturn(Optional.of(targetCatalog));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any())).thenReturn(category);
        when(migrationRepository.save(any())).thenReturn(new CatalogMigration());

        MigrationResultDTO result = migrationService.migrateCategories(
            sourceCatalogId, 
            targetCatalogId, 
            Collections.singletonList(categoryId)
        );

        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        assertEquals(1, result.getCategoriesMigrated());
        assertTrue(result.getErrors().isEmpty());
        verify(categoryRepository).save(any());
    }

    @Test
    void migrateProducts_Success() {
        when(catalogRepository.findById(sourceCatalogId)).thenReturn(Optional.of(sourceCatalog));
        when(catalogRepository.findById(targetCatalogId)).thenReturn(Optional.of(targetCatalog));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);
        when(migrationRepository.save(any())).thenReturn(new CatalogMigration());

        MigrationResultDTO result = migrationService.migrateProducts(
            sourceCatalogId, 
            targetCatalogId, 
            Collections.singletonList(productId)
        );

        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        assertEquals(1, result.getProductsMigrated());
        assertTrue(result.getErrors().isEmpty());
        verify(productRepository).save(any());
    }

    @Test
    void bulkMigrate_Success() {
        when(catalogRepository.findById(sourceCatalogId)).thenReturn(Optional.of(sourceCatalog));
        when(catalogRepository.findById(targetCatalogId)).thenReturn(Optional.of(targetCatalog));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(categoryRepository.save(any())).thenReturn(category);
        when(productRepository.save(any())).thenReturn(product);
        when(migrationRepository.save(any())).thenReturn(new CatalogMigration());

        MigrationRequestDTO request = new MigrationRequestDTO();
        request.setSourceCatalogId(sourceCatalogId);
        request.setTargetCatalogId(targetCatalogId);
        request.setCategoryIds(Collections.singletonList(categoryId));
        request.setProductIds(Collections.singletonList(productId));

        MigrationResultDTO result = migrationService.bulkMigrate(request);

        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        assertTrue(result.getErrors().isEmpty());
        verify(categoryRepository).save(any());
        verify(productRepository).save(any());
    }

    @Test
    void validateMigration_DifferentBusinesses_ThrowsException() {
        targetCatalog = Catalog.builder()
                .id(targetCatalogId)
                .name("Target Catalog")
                .business(Business.builder()
                        .id(UUID.randomUUID())
                        .name("Different Business")
                        .build())
                .build();

        when(catalogRepository.findById(sourceCatalogId)).thenReturn(Optional.of(sourceCatalog));
        when(catalogRepository.findById(targetCatalogId)).thenReturn(Optional.of(targetCatalog));

        assertThrows(BusinessException.class, () -> 
            migrationService.migrateCategories(
                sourceCatalogId, 
                targetCatalogId, 
                Collections.singletonList(categoryId)
            )
        );
    }

    @Test
    void migrateCategories_CategoryNotFound_PartialSuccess() {
        when(catalogRepository.findById(sourceCatalogId)).thenReturn(Optional.of(sourceCatalog));
        when(catalogRepository.findById(targetCatalogId)).thenReturn(Optional.of(targetCatalog));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        when(migrationRepository.save(any())).thenReturn(new CatalogMigration());

        MigrationResultDTO result = migrationService.migrateCategories(
            sourceCatalogId, 
            targetCatalogId, 
            Collections.singletonList(categoryId)
        );

        assertNotNull(result);
        assertEquals("COMPLETED_WITH_ERRORS", result.getStatus());
        assertFalse(result.getErrors().isEmpty());
        assertEquals(0, result.getCategoriesMigrated());
    }

    @Test
    void getMigrationHistory_Success() {
        when(migrationRepository.findBySourceCatalogIdOrTargetCatalogId(sourceCatalogId, sourceCatalogId))
            .thenReturn(Collections.singletonList(new CatalogMigration()));

        List<MigrationResultDTO> history = migrationService.getMigrationHistory(sourceCatalogId);

        assertNotNull(history);
        assertFalse(history.isEmpty());
        assertEquals(1, history.size());
        assertEquals(sourceCatalogId, history.get(0).getSourceCatalogId());
    }

    @Test
    void migrateCatalog_Success() {
        // Given
        UUID businessId = UUID.randomUUID();
        UUID sourceCatalogId = UUID.randomUUID();
        UUID targetCatalogId = UUID.randomUUID();

        Business business = Business.builder()
                .id(businessId)
                .name("Test Business")
                .build();

        Catalog sourceCatalog = Catalog.builder()
                .id(sourceCatalogId)
                .name("Source Catalog")
                .business(business)
                .build();

        Catalog targetCatalog = Catalog.builder()
                .id(targetCatalogId)
                .name("Target Catalog")
                .business(business)
                .build();

        Category category = Category.builder()
                .id(UUID.randomUUID())
                .name("Test Category")
                .catalog(sourceCatalog)
                .business(business)
                .build();

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("Test Product")
                .catalog(sourceCatalog)
                .business(business)
                .build();

        when(businessRepository.findById(businessId)).thenReturn(Optional.of(business));
        when(catalogRepository.findById(sourceCatalogId)).thenReturn(Optional.of(sourceCatalog));
        when(catalogRepository.findById(targetCatalogId)).thenReturn(Optional.of(targetCatalog));
        //when(categoryRepository.findByCatalog(sourceCatalogId)).thenReturn(Collections.singletonList(category));
        when(productRepository.findByCatalog(sourceCatalogId)).thenReturn(Collections.singletonList(product));
        when(categoryRepository.save(any())).thenReturn(category);
        when(productRepository.save(any())).thenReturn(product);

        CatalogMigrationDTO migrationDTO = CatalogMigrationDTO.builder()
                .sourceCatalogId(sourceCatalogId)
                .targetCatalogId(targetCatalogId)
                .build();

        // When
        boolean result = migrationService.migrateCatalog(migrationDTO);

        // Then
        assertTrue(result);
        verify(categoryRepository).save(any());
        verify(productRepository).save(any());
    }

    @Test
    void migrateCatalog_BusinessMismatch() {
        // Given
        UUID businessId1 = UUID.randomUUID();
        UUID businessId2 = UUID.randomUUID();
        UUID sourceCatalogId = UUID.randomUUID();
        UUID targetCatalogId = UUID.randomUUID();

        Business business1 = Business.builder()
                .id(businessId1)
                .name("Test Business")
                .build();

        Business business2 = Business.builder()
                .id(businessId2)
                .name("Different Business")
                .build();

        Catalog sourceCatalog = Catalog.builder()
                .id(sourceCatalogId)
                .name("Source Catalog")
                .business(business1)
                .build();

        Catalog targetCatalog = Catalog.builder()
                .id(targetCatalogId)
                .name("Target Catalog")
                .business(business2)
                .build();

        when(catalogRepository.findById(sourceCatalogId)).thenReturn(Optional.of(sourceCatalog));
        when(catalogRepository.findById(targetCatalogId)).thenReturn(Optional.of(targetCatalog));

        CatalogMigrationDTO migrationDTO = CatalogMigrationDTO.builder()
                .sourceCatalogId(sourceCatalogId)
                .targetCatalogId(targetCatalogId)
                .build();

        // When & Then
        assertThrows(BusinessException.class, () -> migrationService.migrateCatalog(migrationDTO));
        verify(categoryRepository, never()).save(any());
        verify(productRepository, never()).save(any());
    }
}
