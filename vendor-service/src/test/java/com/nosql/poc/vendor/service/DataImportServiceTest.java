package com.nosql.poc.vendor.service;

import com.nosql.poc.vendor.dto.ImportResult;
import com.nosql.poc.vendor.model.SimpleProductSeller;
import com.nosql.poc.vendor.model.SimpleVendor;
import com.nosql.poc.vendor.repository.ProductSellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DataImportServiceTest {

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private ProductSellerRepository productSellerRepository;

    @Mock
    private Resource mockResource;

    @InjectMocks
    private DataImportService dataImportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadProductSellers_Success() throws IOException {
        // Sample CSV content
        String csvContent = 
            "product_id,vendor_id,seller_type,selling_price,stock_quantity,fulfillment_type,processing_time,shipping_charge,seller_sku,condition,warranty_period\n" +
            "PROD-001,TCLQ_DIRECT,PRIMARY,24999.00,50,DIRECT,1,0.00,TCLQ-MBP-2023-16,NEW,1 year standard warranty\n" +
            "PROD-002,TCLQ_MARKETPLACE,SECONDARY,23999.00,30,MARKETPLACE,2,199.00,MPLACE-MBP-2023-16,NEW,1 year standard warranty";

        // Mock resource loader behavior
        when(resourceLoader.getResource(anyString())).thenReturn(mockResource);
        when(mockResource.getInputStream()).thenReturn(new ByteArrayInputStream(csvContent.getBytes()));
        when(productSellerRepository.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));

        // Execute the method
        ImportResult result = dataImportService.loadProductSellers();

        // Verify the result
        assertTrue(result.isSuccess());
        assertEquals(2, result.getImportedCount());
        assertEquals("SimpleProductSeller", result.getEntityType());
        
        // Verify repository was called
        verify(productSellerRepository).saveAll(anyList());
    }

    @Test
    void loadProductSellers_EmptyFile() throws IOException {
        // Empty CSV with only header
        String csvContent = "product_id,vendor_id,seller_type,selling_price,stock_quantity,fulfillment_type,processing_time,shipping_charge,seller_sku,condition,warranty_period";

        // Mock resource loader behavior
        when(resourceLoader.getResource(anyString())).thenReturn(mockResource);
        when(mockResource.getInputStream()).thenReturn(new ByteArrayInputStream(csvContent.getBytes()));

        // Execute the method
        ImportResult result = dataImportService.loadProductSellers();

        // Verify the result
        assertFalse(result.isSuccess());
        assertEquals(0, result.getImportedCount());
        assertEquals("No valid product sellers found", result.getErrorMessage());
        
        // Verify repository was not called
        verify(productSellerRepository, never()).saveAll(anyList());
    }

    @Test
    void loadProductSellers_InvalidData() throws IOException {
        // CSV with invalid data
        String csvContent = 
            "product_id,vendor_id,seller_type,selling_price,stock_quantity,fulfillment_type,processing_time,shipping_charge,seller_sku,condition,warranty_period\n" +
            "PROD-001,TCLQ_DIRECT,PRIMARY,24999.00,50,DIRECT,1,0.00,TCLQ-MBP-2023-16,NEW,1 year standard warranty\n" +
            ",TCLQ_MARKETPLACE,INVALID,NOT_A_NUMBER,30,MARKETPLACE,2,199.00,MPLACE-MBP-2023-16,NEW,1 year standard warranty";

        // Mock resource loader behavior
        when(resourceLoader.getResource(anyString())).thenReturn(mockResource);
        when(mockResource.getInputStream()).thenReturn(new ByteArrayInputStream(csvContent.getBytes()));
        when(productSellerRepository.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));

        // Execute the method
        ImportResult result = dataImportService.loadProductSellers();

        // Verify the result
        assertTrue(result.isSuccess());
        assertEquals(1, result.getImportedCount());
        assertFalse(result.getErrors().isEmpty());
        
        // Verify repository was called with only the valid item
        verify(productSellerRepository).saveAll(argThat(list -> ((List<SimpleProductSeller>)list).size() == 1));
    }

    @Test
    void loadAllVendorData_Success() throws IOException {
        // Sample CSV content
        String csvContent = 
            "code,name,email,phone\n" +
            "TCLQ-001,TataCliq,info@tatacliq.com,1234567890\n" +
            "CRMA-001,Croma,info@croma.com,0987654321";

        // Mock resource loader behavior
        when(resourceLoader.getResource(anyString())).thenReturn(mockResource);
        when(mockResource.getInputStream()).thenReturn(new ByteArrayInputStream(csvContent.getBytes()));
        
        // Since loadAllVendorData calls loadVendors and loadProductSellers, we need to mock them
        // to avoid NullPointerException during test
        doReturn(new ImportResult()).when(mockResource).getInputStream();

        // Execute the method
        Map<String, ImportResult> results = dataImportService.loadAllVendorData();

        // Verify the result
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.containsKey("Vendors"));
        assertTrue(results.containsKey("ProductSellers"));
    }
}