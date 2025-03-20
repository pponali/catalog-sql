package com.scaler.vendor.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.scaler.vendor.dto.ImportResult;
import com.scaler.vendor.model.Merchant;
import com.scaler.vendor.model.Seller;
import com.scaler.vendor.repository.MerchantRepository;
import com.scaler.vendor.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service for loading vendor-related data from CSV files in the resources/csv directory
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoadDataFromCsvService {

    private final ResourceLoader resourceLoader;
    private final MerchantRepository merchantRepository;
    private final SellerRepository sellerRepository;

    /**
     * Load merchants from merchants.csv
     * 
     * @return ImportResult with details of the import operation
     */
    public ImportResult loadMerchants() {
        log.info("Loading merchants from CSV");
        
        ImportResult result = new ImportResult();
        result.setEntityType("Merchant");
        
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
                
                List<Merchant> merchants = new ArrayList<>();
                String[] line;
                int rowCount = 0;
                
                while ((line = csvReader.readNext()) != null) {
                    rowCount++;
                    try {
                        Merchant merchant = parseMerchant(line, header);
                        merchants.add(merchant);
                    } catch (Exception e) {
                        log.error("Error parsing merchant at row {}: {}", rowCount, e.getMessage());
                        result.getErrors().add("Row " + rowCount + ": " + e.getMessage());
                    }
                }
                
                // Save all valid merchants
                if (!merchants.isEmpty()) {
                    merchantRepository.saveAll(merchants);
                    result.setSuccess(true);
                    result.setImportedCount(merchants.size());
                } else {
                    result.setSuccess(false);
                    result.setErrorMessage("No valid merchants found");
                }
                
            } catch (IOException | CsvValidationException e) {
                log.error("Error reading merchants CSV", e);
                result.setSuccess(false);
                result.setErrorMessage("Error reading CSV: " + e.getMessage());
            }
            
        } catch (Exception e) {
            log.error("Error loading merchants resource", e);
            result.setSuccess(false);
            result.setErrorMessage("Error loading resource: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Load sellers from sellers.csv
     * 
     * @return ImportResult with details of the import operation
     */
    public ImportResult loadSellers() {
        log.info("Loading sellers from CSV");
        
        ImportResult result = new ImportResult();
        result.setEntityType("Seller");
        
        try {
            Resource resource = resourceLoader.getResource("classpath:csv/sellers.csv");
            
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {
                
                // Skip header
                String[] header = csvReader.readNext();
                if (header == null) {
                    result.setSuccess(false);
                    result.setErrorMessage("Empty CSV file");
                    return result;
                }
                
                List<Seller> sellers = new ArrayList<>();
                String[] line;
                int rowCount = 0;
                
                while ((line = csvReader.readNext()) != null) {
                    rowCount++;
                    try {
                        Seller seller = parseSeller(line, header);
                        sellers.add(seller);
                    } catch (Exception e) {
                        log.error("Error parsing seller at row {}: {}", rowCount, e.getMessage());
                        result.getErrors().add("Row " + rowCount + ": " + e.getMessage());
                    }
                }
                
                // Save all valid sellers
                if (!sellers.isEmpty()) {
                    sellerRepository.saveAll(sellers);
                    result.setSuccess(true);
                    result.setImportedCount(sellers.size());
                } else {
                    result.setSuccess(false);
                    result.setErrorMessage("No valid sellers found");
                }
                
            } catch (IOException | CsvValidationException e) {
                log.error("Error reading sellers CSV", e);
                result.setSuccess(false);
                result.setErrorMessage("Error reading CSV: " + e.getMessage());
            }
            
        } catch (Exception e) {
            log.error("Error loading sellers resource", e);
            result.setSuccess(false);
            result.setErrorMessage("Error loading resource: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Parse a merchant from a CSV row
     */
    private Merchant parseMerchant(String[] row, String[] header) {
        Map<String, String> rowMap = mapRowToHeader(row, header);
        
        Merchant merchant = new Merchant();
        
        String id = rowMap.get("id");
        if (id != null && !id.isEmpty()) {
            merchant.setId(id);
        } else {
            merchant.setId(UUID.randomUUID().toString());
        }
        
        merchant.setName(requireField(rowMap, "name"));
        merchant.setCode(requireField(rowMap, "code"));
        
        String description = rowMap.get("description");
        if (description != null) {
            merchant.setDescription(description);
        }
        
        String status = rowMap.get("status");
        if (status != null) {
            merchant.setStatus(status);
        } else {
            merchant.setStatus("ACTIVE");
        }
        
        merchant.setCreatedDate(LocalDateTime.now());
        merchant.setLastModifiedDate(LocalDateTime.now());
        
        return merchant;
    }
    
    /**
     * Parse a seller from a CSV row
     */
    private Seller parseSeller(String[] row, String[] header) {
        Map<String, String> rowMap = mapRowToHeader(row, header);
        
        Seller seller = new Seller();
        
        String id = rowMap.get("id");
        if (id != null && !id.isEmpty()) {
            seller.setId(id);
        } else {
            seller.setId(UUID.randomUUID().toString());
        }
        
        seller.setName(requireField(rowMap, "name"));
        seller.setCode(requireField(rowMap, "code"));
        
        String description = rowMap.get("description");
        if (description != null) {
            seller.setDescription(description);
        }
        
        String status = rowMap.get("status");
        if (status != null) {
            seller.setStatus(status);
        } else {
            seller.setStatus("ACTIVE");
        }
        
        String merchantId = rowMap.get("merchant_id");
        if (merchantId != null && !merchantId.isEmpty()) {
            seller.setMerchantId(merchantId);
        }
        
        seller.setCreatedDate(LocalDateTime.now());
        seller.setLastModifiedDate(LocalDateTime.now());
        
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
        results.put("Merchants", loadMerchants());
        results.put("Sellers", loadSellers());
        
        return results;
    }
}