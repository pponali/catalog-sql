package com.scaler.util;

import com.scaler.entity.Catalog;
import com.scaler.entity.Category;
import com.scaler.entity.Merchant;
import com.scaler.entity.Product;
import com.scaler.repository.CatalogRepository;
import com.scaler.repository.CategoryRepository;
import com.scaler.repository.MerchantRepository;
import com.scaler.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SampleProductImporterTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private CatalogRepository catalogRepository;

    @InjectMocks
    private SampleProductImporter sampleProductImporter;

    private Merchant mockMerchant;
    private Category mockCategory;
    private Catalog mockCatalog;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create mock entities
        mockMerchant = Merchant.builder()
                .id(UUID.randomUUID())
                .code("TCLQ-001")
                .name("Tata CLiQ")
                .build();

        mockCategory = Category.builder()
                .id(UUID.randomUUID())
                .code("MPH1111101100")
                .name("Men's T-Shirts")
                .build();

        mockCatalog = Catalog.builder()
                .id(UUID.randomUUID())
                .code("FASH-001")
                .name("Fashion Catalog")
                .build();

        // Set up repository mocks
        when(merchantRepository.findByCode("TCLQ-001")).thenReturn(Optional.of(mockMerchant));
        when(categoryRepository.findByCode("MPH1111101100")).thenReturn(Optional.of(mockCategory));
        when(catalogRepository.findByCode("FASH-001")).thenReturn(Optional.of(mockCatalog));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void testImportSampleProducts() throws IOException {
        // Verify that the CSV file exists
        ClassPathResource resource = new ClassPathResource("csv/sample_products.csv");
        assertNotNull(resource.getInputStream());

        // Count the number of product lines in the CSV (excluding header)
        Path path = resource.getFile().toPath();
        long lineCount = Files.lines(path).skip(1).count();

        // Import products
        List<Product> importedProducts = sampleProductImporter.importSampleProducts();

        // Verify that products were imported
        assertNotNull(importedProducts);
        
        // Verify that the correct number of products were saved
        verify(productRepository, times((int) lineCount)).save(any(Product.class));
    }
}
