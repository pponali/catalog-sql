package com.nosql.poc.vendor.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.nosql.poc.vendor.model.*;
import com.nosql.poc.vendor.repository.ProductSellerRepository;
import com.nosql.poc.vendor.repository.VendorRepository;
import com.nosql.poc.vendor.dto.ImportResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.ArrayList;

/**
 * Service for loading vendor-related data from CSV files in the resources/csv directory
 */
@Service
@RequiredArgsConstructor
public class DataImportService {
    
    private static final Logger log = LoggerFactory.getLogger(DataImportService.class);

    private final ResourceLoader resourceLoader;
    private final ProductSellerRepository productSellerRepository;
    private final VendorRepository vendorRepository;

    /**
     * Load vendors from vendors.csv
     * 
     * @return ImportResult with details of the import operation
     */
    public ImportResult loadVendors() {
        log.info("Loading vendors from CSV");
        
        ImportResult result = new ImportResult();
        result.setEntityType("Vendor");
        result.setErrors(new ArrayList<>());
        
        try {
            Resource resource = resourceLoader.getResource("classpath:csv/merchants.csv");
            
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {
                
                // Skip header
                String[] header = csvReader.readNext();
                if (header == null) {
                    result.setSuccess(false);
                    result.setErrorMessage("Empty CSV file");
                    return result;
                }
                
                List<SimpleVendor> vendors = new ArrayList<>();
                String[] line;
                int rowCount = 0;
                
                while ((line = csvReader.readNext()) != null) {
                    rowCount++;
                    try {
                        SimpleVendor vendor = parseVendor(line, header);
                        vendors.add(vendor);
                    } catch (Exception e) {
                        log.error("Error parsing vendor at row {}: {}", rowCount, e.getMessage());
                        result.getErrors().add("Row " + rowCount + ": " + e.getMessage());
                    }
                }
                
                // Save all valid vendors
                if (!vendors.isEmpty()) {
                    vendorRepository.saveAll(vendors);
                    result.setSuccess(true);
                    result.setImportedCount(vendors.size());
                } else {
                    result.setSuccess(false);
                    result.setErrorMessage("No valid vendors found");
                }
                
            } catch (IOException | CsvValidationException e) {
                log.error("Error reading vendors CSV", e);
                result.setSuccess(false);
                result.setErrorMessage("Error reading CSV: " + e.getMessage());
            }
            
        } catch (Exception e) {
            log.error("Error loading vendors resource", e);
            result.setSuccess(false);
            result.setErrorMessage("Error loading resource: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Load product sellers from product_sellers.csv
     * 
     * @return ImportResult with details of the import operation
     */
    public ImportResult loadProductSellers() {
        log.info("Loading product sellers from CSV");
        
        ImportResult result = new ImportResult();
        result.setEntityType("SimpleProductSeller");
        result.setErrors(new ArrayList<>());
        
        try {
            Resource resource = resourceLoader.getResource("classpath:csv/product_sellers.csv");
            
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {
                
                // Skip header
                String[] header = csvReader.readNext();
                if (header == null) {
                    result.setSuccess(false);
                    result.setErrorMessage("Empty CSV file");
                    return result;
                }
                
                List<SimpleProductSeller> productSellers = new ArrayList<>();
                String[] line;
                int rowCount = 0;
                
                while ((line = csvReader.readNext()) != null) {
                    rowCount++;
                    try {
                        SimpleProductSeller productSeller = parseProductSellerFromCsv(line, header);
                        productSellers.add(productSeller);
                    } catch (Exception e) {
                        log.error("Error parsing product seller at row {}: {}", rowCount, e.getMessage());
                        result.getErrors().add("Row " + rowCount + ": " + e.getMessage());
                    }
                }
                
                // Save all valid product sellers
                if (!productSellers.isEmpty()) {
                    productSellerRepository.saveAll(productSellers);
                    result.setSuccess(true);
                    result.setImportedCount(productSellers.size());
                } else {
                    result.setSuccess(false);
                    result.setErrorMessage("No valid product sellers found");
                }
                
            } catch (IOException | CsvValidationException e) {
                log.error("Error reading product sellers CSV", e);
                result.setSuccess(false);
                result.setErrorMessage("Error reading CSV: " + e.getMessage());
            }
            
        } catch (Exception e) {
            log.error("Error loading product sellers resource", e);
            result.setSuccess(false);
            result.setErrorMessage("Error loading resource: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Parse a vendor from a CSV row
     */
    private SimpleVendor parseVendor(String[] row, String[] header) {
        Map<String, String> rowMap = mapRowToHeader(row, header);
        
        SimpleVendor vendor = new SimpleVendor();
        
        vendor.setId(UUID.randomUUID().toString());
        vendor.setVendorId(requireField(rowMap, "code"));
        vendor.setBusinessName(requireField(rowMap, "name"));
        vendor.setSellerType(SellerType.SECONDARY);
        
        // Set legal name
        vendor.setLegalName(rowMap.get("name"));
        
        // Set contact information
        vendor.setEmail(rowMap.get("email"));
        vendor.setPhone(rowMap.get("phone"));
        
        // Set active status
        vendor.setActive(true);
        vendor.setCreatedAt(LocalDateTime.now());
        vendor.setUpdatedAt(LocalDateTime.now());
        
        return vendor;
    }
    
    /**
     * Parse a product seller from a seller CSV row
     */
    private ProductSeller parseProductSeller(String[] row, String[] header) {
        Map<String, String> rowMap = mapRowToHeader(row, header);
        
        String id = UUID.randomUUID().toString();
        String productId = "SAMPLE-PRODUCT-" + rowMap.get("code"); // Sample product ID
        String vendorId = requireField(rowMap, "code");
        SellerType sellerType = SellerType.SECONDARY;
        
        // Create a product seller object with all parameters
        ProductSeller seller = new ProductSeller();
        seller.setId(id);
        seller.setProductId(productId);
        seller.setVendorId(vendorId);
        seller.setSellerType(sellerType);
        seller.setSellingPrice(new BigDecimal("100.00")); // Sample price
        seller.setStockQuantity(10); // Sample stock
        seller.setIsActive(true);
        seller.setListingDate(LocalDateTime.now());
        seller.setLastUpdated(LocalDateTime.now());
        seller.setFulfillmentType("MARKETPLACE");
        seller.setProcessingTime(3); // 3 days
        seller.setShippingCharge(new BigDecimal("5.00"));
        seller.setSellerSku("SKU-" + rowMap.get("code"));
        seller.setCondition("NEW");
        seller.setWarrantyPeriod("1 year");
        seller.setSellerRating(4.5);
        seller.setTotalSales(0);
        seller.setReturnRate(0.0);
        seller.setMinOrderQuantity(1);
        seller.setMaxOrderQuantity(5);
        seller.setAllowPartialFulfillment(false);
        
        return seller;
    }
    
    /**
     * Parse a product seller from the product_sellers CSV row
     */
    private SimpleProductSeller parseProductSellerFromCsv(String[] row, String[] header) {
        Map<String, String> rowMap = mapRowToHeader(row, header);
        
        SimpleProductSeller seller = new SimpleProductSeller();
        
        // Set ID and required fields
        seller.setId(UUID.randomUUID().toString());
        seller.setProductId(requireField(rowMap, "product_id"));
        seller.setVendorId(requireField(rowMap, "vendor_id"));
        
        // Parse seller type
        SellerType sellerType = SellerType.SECONDARY;
        try {
            String sellerTypeStr = rowMap.get("seller_type");
            if (sellerTypeStr != null && !sellerTypeStr.isEmpty()) {
                sellerType = SellerType.valueOf(sellerTypeStr);
            }
        } catch (Exception e) {
            log.warn("Invalid seller type: {}, using SECONDARY", rowMap.get("seller_type"));
        }
        seller.setSellerType(sellerType);
        
        // Parse BigDecimal values
        BigDecimal sellingPrice = new BigDecimal("0.00");
        try {
            String priceStr = rowMap.get("selling_price");
            if (priceStr != null && !priceStr.isEmpty()) {
                sellingPrice = new BigDecimal(priceStr);
            }
        } catch (Exception e) {
            log.warn("Invalid selling price: {}, using 0.00", rowMap.get("selling_price"));
        }
        seller.setSellingPrice(sellingPrice);
        
        BigDecimal shippingCharge = new BigDecimal("0.00");
        try {
            String chargeStr = rowMap.get("shipping_charge");
            if (chargeStr != null && !chargeStr.isEmpty()) {
                shippingCharge = new BigDecimal(chargeStr);
            }
        } catch (Exception e) {
            log.warn("Invalid shipping charge: {}, using 0.00", rowMap.get("shipping_charge"));
        }
        seller.setShippingCharge(shippingCharge);
        
        // Parse Integer values
        Integer stockQuantity = 0;
        try {
            String quantityStr = rowMap.get("stock_quantity");
            if (quantityStr != null && !quantityStr.isEmpty()) {
                stockQuantity = Integer.parseInt(quantityStr);
            }
        } catch (Exception e) {
            log.warn("Invalid stock quantity: {}, using 0", rowMap.get("stock_quantity"));
        }
        seller.setStockQuantity(stockQuantity);
        
        Integer processingTime = 1;
        try {
            String timeStr = rowMap.get("processing_time");
            if (timeStr != null && !timeStr.isEmpty()) {
                processingTime = Integer.parseInt(timeStr);
            }
        } catch (Exception e) {
            log.warn("Invalid processing time: {}, using 1", rowMap.get("processing_time"));
        }
        seller.setProcessingTime(processingTime);
        
        // Set other fields
        seller.setIsActive(true);
        seller.setListingDate(LocalDateTime.now());
        seller.setLastUpdated(LocalDateTime.now());
        seller.setFulfillmentType(rowMap.get("fulfillment_type"));
        seller.setSellerSku(rowMap.get("seller_sku"));
        seller.setCondition(rowMap.get("condition"));
        seller.setWarrantyPeriod(rowMap.get("warranty_period"));
        seller.setSellerRating(4.5); // Default rating
        seller.setTotalSales(0); // Default sales
        seller.setReturnRate(0.0); // Default return rate
        seller.setMinOrderQuantity(1); // Default min order
        seller.setMaxOrderQuantity(stockQuantity > 10 ? 10 : stockQuantity); // Default max order
        seller.setAllowPartialFulfillment(false); // Default not allowing partial fulfillment
        
        return seller;
    }
    
    /**
     * Map a CSV row to column headers
     */
    private Map<String, String> mapRowToHeader(String[] row, String[] header) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < Math.min(row.length, header.length); i++) {
            map.put(header[i], row[i]);
        }
        return map;
    }
    
    /**
     * Get a required field from the row map
     */
    private String requireField(Map<String, String> map, String fieldName) {
        String value = map.get(fieldName);
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Required field missing: " + fieldName);
        }
        return value;
    }
    
    /**
     * Load all vendor data from CSV files
     * 
     * @return Map of entity types to import results
     */
    public Map<String, ImportResult> loadAllVendorData() {
        log.info("Loading all vendor data from CSV files");
        
        Map<String, ImportResult> results = new HashMap<>();
        
        // Load in a specific order to handle dependencies
        ImportResult vendorResult = loadVendors();
        ImportResult productSellerResult = loadProductSellers();
        
        results.put("Vendors", vendorResult);
        results.put("ProductSellers", productSellerResult);
        
        return results;
    }
}