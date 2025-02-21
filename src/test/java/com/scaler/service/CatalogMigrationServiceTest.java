package com.scaler.service;

import com.scaler.dto.CatalogMigrationDTO;
import com.scaler.dto.MigrationRequestDTO;
import com.scaler.dto.MigrationResultDTO;
import com.scaler.entity.*;
import com.scaler.enums.BusinessUnit;
import com.scaler.enums.MigrationStatus;
import com.scaler.exception.BusinessException;
import com.scaler.repository.*;
import com.scaler.service.impl.CatalogMigrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogMigrationServiceTest {/*

    @Mock
    private CatalogRepository catalogRepository;
    
    @Mock
    private CategoryRepository categoryRepository;
    
    @Mock
    private ProductRepository productRepository;
    
    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private CatalogMigrationRepository migrationRepository;

    @InjectMocks
    private CatalogMigrationServiceImpl migrationService;

    private Merchant sourceBusiness;
    private Merchant targetMerchant;
    private Catalog sourceCatalog;
    private Catalog targetCatalog;
    private Category category;
    private Product product;
    private UUID sourceBusinessId;
    private UUID targetMerchantId;
    private UUID sourceCatalogId;
    private UUID targetCatalogId;
    private UUID categoryId;
    private UUID productId;
    private CatalogMigration migration;

    @BeforeEach
    void setUp() {
        sourceBusinessId = UUID.randomUUID();
        targetMerchantId = UUID.randomUUID();
        sourceCatalogId = UUID.randomUUID();
        targetCatalogId = UUID.randomUUID();
        categoryId = UUID.randomUUID();
        productId = UUID.randomUUID();

        Merchant sourceMerchant = Merchant.builder()
                .id(sourceBusinessId)
                .name("Source Business")
                .code(BusinessUnit.TATA_CLIQ_FASHION.name())
                .build();

        targetMerchant = Merchant.builder()
                .id(targetMerchantId)
                .name("Target Business")
                .code(BusinessUnit.TATA_DIGITAL.name())
                .build();

        sourceCatalog = Catalog.builder()
                .id(sourceCatalogId)
                .name("Source Catalog")
                .business(sourceBusiness)
                .build();

        targetCatalog = Catalog.builder()
                .id(targetCatalogId)
                .name("Target Catalog")
                .business(targetMerchant)
                .build();

        category = Category.builder()
                .id(categoryId)
                .name("Test Category")
                .catalog(sourceCatalog)
                .merchant(sourceBusiness)
                .build();

        product = Product.builder()
                .id(productId)
                .name("Test Product")
                .catalog(sourceCatalog)
                .merchant(sourceBusiness)
                .build();

        migration = new CatalogMigration();
        migration.setId(UUID.randomUUID());
        migration.setSourceCatalogId(sourceCatalogId);
        migration.setTargetCatalogId(targetCatalogId);
        migration.setStatus(MigrationStatus.COMPLETED);
        migration.setMigrationTime(LocalDateTime.now());
        migration.setCompletedAt(LocalDateTime.now());
        migration.setErrors(new ArrayList<>());
        migration.setWarnings(new ArrayList<>());
        migration.setMigratedCategoryIds(new ArrayList<>());
        migration.setMigratedProductIds(new ArrayList<>());
        migration.setFailedCategoryIds(new ArrayList<>());
        migration.setFailedProductIds(new ArrayList<>());
    }

    @Test
    void migrateCategories_Success() {
        when(catalogRepository.findById(sourceCatalogId)).thenReturn(Optional.of(sourceCatalog));
        when(catalogRepository.findById(targetCatalogId)).thenReturn(Optional.of(targetCatalog));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any())).thenReturn(category);
        when(merchantRepository.findById(sourceBusiness.getId())).thenReturn(Optional.of(sourceBusiness));
        when(merchantRepository.findById(targetMerchant.getId())).thenReturn(Optional.of(targetMerchant));
        when(migrationRepository.save(any())).thenReturn(migration);

        MigrationResultDTO result = migrationService.migrateCategories(
            sourceCatalogId, 
            targetCatalogId, 
            Collections.singletonList(categoryId)
        );

        assertNotNull(result);
        assertEquals(MigrationStatus.COMPLETED.name(), result.getStatus());
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
        when(merchantRepository.findById(sourceBusiness.getId())).thenReturn(Optional.of(sourceBusiness));
        when(merchantRepository.findById(targetMerchant.getId())).thenReturn(Optional.of(targetMerchant));
        when(migrationRepository.save(any())).thenReturn(migration);

        MigrationResultDTO result = migrationService.migrateProducts(
            sourceCatalogId, 
            targetCatalogId, 
            Collections.singletonList(productId)
        );

        assertNotNull(result);
        assertEquals(MigrationStatus.COMPLETED.name(), result.getStatus());
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
        when(merchantRepository.findById(sourceBusiness.getId())).thenReturn(Optional.of(sourceBusiness));
        when(merchantRepository.findById(targetMerchant.getId())).thenReturn(Optional.of(targetMerchant));
        when(migrationRepository.save(any())).thenReturn(migration);

        MigrationRequestDTO request = new MigrationRequestDTO();
        request.setSourceCatalogId(sourceCatalogId);
        request.setTargetCatalogId(targetCatalogId);
        request.setCategoryIds(Collections.singletonList(categoryId));
        request.setProductIds(Collections.singletonList(productId));

        MigrationResultDTO result = migrationService.bulkMigrate(request);

        assertNotNull(result);
        assertEquals(MigrationStatus.COMPLETED.name(), result.getStatus());
        assertTrue(result.getErrors().isEmpty());
        verify(categoryRepository).save(any());
        verify(productRepository).save(any());
    }

    @Test
    void getMigrationHistory_Success() {
        when(migrationRepository.findBySourceCatalogIdOrTargetCatalogId(sourceCatalogId, sourceCatalogId))
            .thenReturn(Collections.singletonList(migration));

        List<MigrationResultDTO> history = migrationService.getMigrationHistory(sourceCatalogId);

        assertNotNull(history);
        assertFalse(history.isEmpty());
        assertEquals(1, history.size());
        assertEquals(sourceCatalogId, history.get(0).getSourceCatalogId());
    }

    @Test
    void migrateCatalog_Success() {
        when(catalogRepository.findById(sourceCatalogId)).thenReturn(Optional.of(sourceCatalog));
        when(catalogRepository.findById(targetCatalogId)).thenReturn(Optional.of(targetCatalog));
        when(productRepository.findByCatalogId(sourceCatalogId)).thenReturn(Collections.singletonList(product));
        when(categoryRepository.save(any())).thenReturn(category);
        when(productRepository.save(any())).thenReturn(product);
        when(merchantRepository.findById(sourceBusiness.getId())).thenReturn(Optional.of(sourceBusiness));
        when(merchantRepository.findById(targetMerchant.getId())).thenReturn(Optional.of(targetMerchant));
        when(migrationRepository.save(any())).thenReturn(migration);

        CatalogMigrationDTO migrationDTO = CatalogMigrationDTO.builder()
                .sourceCatalogId(sourceCatalogId)
                .targetCatalogId(targetCatalogId)
                .build();

        boolean result = migrationService.migrateCatalog(migrationDTO);

        assertTrue(result);
        verify(migrationRepository, times(4)).save(any());
    }

    @Test
    void migrateCatalog_BusinessMismatch() {
        Merchant invalidTargetMerchant = Merchant.builder()
                .id(targetMerchantId)
                .name("Invalid Target")
                .code(BusinessUnit.BIGBASKET.name())
                .build();

        targetCatalog.setBusiness((invalidTargetMerchant));

        when(catalogRepository.findById(sourceCatalogId)).thenReturn(Optional.of(sourceCatalog));
        when(catalogRepository.findById(targetCatalogId)).thenReturn(Optional.of(targetCatalog));
        when(merchantRepository.findById(sourceBusiness.getId())).thenReturn(Optional.of(sourceBusiness));
        when(merchantRepository.findById(targetMerchantId)).thenReturn(Optional.of(invalidTargetMerchant));

        CatalogMigrationDTO migrationDTO = CatalogMigrationDTO.builder()
                .sourceCatalogId(sourceCatalogId)
                .targetCatalogId(targetCatalogId)
                .build();

        assertThrows(BusinessException.class, () -> migrationService.migrateCatalog(migrationDTO));
        verify(categoryRepository, never()).save(any());
        verify(productRepository, never()).save(any());
    }*/
}
