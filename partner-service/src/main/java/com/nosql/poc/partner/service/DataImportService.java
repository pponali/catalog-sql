package com.nosql.poc.partner.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.nosql.poc.partner.model.SimplePartner;
import com.nosql.poc.partner.model.SimplePartnerContract;
import com.nosql.poc.partner.repository.PartnerRepository;
import com.nosql.poc.partner.repository.PartnerContractRepository;
import com.nosql.poc.partner.dto.ImportResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service for loading partner-related data from CSV files in the resources/csv directory
 */
@Service
@RequiredArgsConstructor
public class DataImportService {
    
    private static final Logger log = LoggerFactory.getLogger(DataImportService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final ResourceLoader resourceLoader;
    private final PartnerRepository partnerRepository;
    private final PartnerContractRepository partnerContractRepository;

    /**
     * Load partners from partners.csv
     * 
     * @return ImportResult with details of the import operation
     */
    public ImportResult loadPartners() {
        log.info("Loading partners from CSV");
        
        ImportResult result = new ImportResult();
        result.setEntityType("Partner");
        result.setErrors(new ArrayList<>());
        
        try {
            Resource resource = resourceLoader.getResource("classpath:csv/partners.csv");
            
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {
                
                // Skip header
                String[] header = csvReader.readNext();
                if (header == null) {
                    result.setSuccess(false);
                    result.setErrorMessage("Empty CSV file");
                    return result;
                }
                
                List<SimplePartner> partners = new ArrayList<>();
                String[] line;
                int rowCount = 0;
                
                while ((line = csvReader.readNext()) != null) {
                    rowCount++;
                    try {
                        SimplePartner partner = parsePartner(line, header);
                        partners.add(partner);
                    } catch (Exception e) {
                        log.error("Error parsing partner at row {}: {}", rowCount, e.getMessage());
                        result.getErrors().add("Row " + rowCount + ": " + e.getMessage());
                    }
                }
                
                // Save all valid partners
                if (!partners.isEmpty()) {
                    partnerRepository.saveAll(partners);
                    result.setSuccess(true);
                    result.setImportedCount(partners.size());
                } else {
                    result.setSuccess(false);
                    result.setErrorMessage("No valid partners found");
                }
                
            } catch (IOException | CsvValidationException e) {
                log.error("Error reading partners CSV", e);
                result.setSuccess(false);
                result.setErrorMessage("Error reading CSV: " + e.getMessage());
            }
            
        } catch (Exception e) {
            log.error("Error loading partners resource", e);
            result.setSuccess(false);
            result.setErrorMessage("Error loading resource: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Load partner contracts from partner_contracts.csv
     * 
     * @return ImportResult with details of the import operation
     */
    public ImportResult loadPartnerContracts() {
        log.info("Loading partner contracts from CSV");
        
        ImportResult result = new ImportResult();
        result.setEntityType("PartnerContract");
        result.setErrors(new ArrayList<>());
        
        try {
            Resource resource = resourceLoader.getResource("classpath:csv/partner_contracts.csv");
            
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {
                
                // Skip header
                String[] header = csvReader.readNext();
                if (header == null) {
                    result.setSuccess(false);
                    result.setErrorMessage("Empty CSV file");
                    return result;
                }
                
                List<SimplePartnerContract> contracts = new ArrayList<>();
                String[] line;
                int rowCount = 0;
                
                while ((line = csvReader.readNext()) != null) {
                    rowCount++;
                    try {
                        SimplePartnerContract contract = parsePartnerContract(line, header);
                        contracts.add(contract);
                    } catch (Exception e) {
                        log.error("Error parsing partner contract at row {}: {}", rowCount, e.getMessage());
                        result.getErrors().add("Row " + rowCount + ": " + e.getMessage());
                    }
                }
                
                // Save all valid contracts
                if (!contracts.isEmpty()) {
                    partnerContractRepository.saveAll(contracts);
                    result.setSuccess(true);
                    result.setImportedCount(contracts.size());
                } else {
                    result.setSuccess(false);
                    result.setErrorMessage("No valid partner contracts found");
                }
                
            } catch (IOException | CsvValidationException e) {
                log.error("Error reading partner contracts CSV", e);
                result.setSuccess(false);
                result.setErrorMessage("Error reading CSV: " + e.getMessage());
            }
            
        } catch (Exception e) {
            log.error("Error loading partner contracts resource", e);
            result.setSuccess(false);
            result.setErrorMessage("Error loading resource: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Parse a partner from a CSV row
     */
    private SimplePartner parsePartner(String[] row, String[] header) {
        Map<String, String> rowMap = mapRowToHeader(row, header);
        
        SimplePartner partner = new SimplePartner();
        
        partner.setId(UUID.randomUUID().toString());
        partner.setPartnerId(requireField(rowMap, "partner_id"));
        partner.setName(requireField(rowMap, "name"));
        partner.setDescription(rowMap.get("description"));
        partner.setType(rowMap.get("type"));
        partner.setCategory(rowMap.get("category"));
        partner.setStatus(rowMap.getOrDefault("status", "ACTIVE"));
        partner.setContactPerson(rowMap.get("contact_person"));
        partner.setEmail(rowMap.get("email"));
        partner.setPhone(rowMap.get("phone"));
        partner.setAddress(rowMap.get("address"));
        partner.setTaxId(rowMap.get("tax_id"));
        
        // Parse boolean values
        Boolean verified = Boolean.FALSE;
        if (rowMap.containsKey("verified") && !rowMap.get("verified").isEmpty()) {
            verified = Boolean.parseBoolean(rowMap.get("verified"));
        }
        partner.setVerified(verified);
        
        partner.setCreatedAt(LocalDateTime.now());
        partner.setUpdatedAt(LocalDateTime.now());
        
        return partner;
    }
    
    /**
     * Parse a partner contract from a CSV row
     */
    private SimplePartnerContract parsePartnerContract(String[] row, String[] header) {
        Map<String, String> rowMap = mapRowToHeader(row, header);
        
        SimplePartnerContract contract = new SimplePartnerContract();
        
        contract.setId(UUID.randomUUID().toString());
        contract.setContractId(requireField(rowMap, "contract_id"));
        contract.setPartnerId(requireField(rowMap, "partner_id"));
        contract.setContractType(rowMap.get("contract_type"));
        
        // Parse date values
        LocalDate startDate = null;
        try {
            startDate = LocalDate.parse(requireField(rowMap, "start_date"), DATE_FORMATTER);
        } catch (Exception e) {
            log.warn("Invalid start date: {}, using current date", rowMap.get("start_date"));
            startDate = LocalDate.now();
        }
        contract.setStartDate(startDate);
        
        LocalDate endDate = null;
        try {
            endDate = LocalDate.parse(requireField(rowMap, "end_date"), DATE_FORMATTER);
        } catch (Exception e) {
            log.warn("Invalid end date: {}, using start date + 1 year", rowMap.get("end_date"));
            endDate = startDate.plusYears(1);
        }
        contract.setEndDate(endDate);
        
        // Parse BigDecimal values
        BigDecimal contractValue = new BigDecimal("0.00");
        try {
            contractValue = new BigDecimal(rowMap.get("contract_value"));
        } catch (Exception e) {
            log.warn("Invalid contract value: {}, using 0.00", rowMap.get("contract_value"));
        }
        contract.setContractValue(contractValue);
        
        contract.setCurrency(rowMap.getOrDefault("currency", "USD"));
        contract.setPaymentTerms(rowMap.get("payment_terms"));
        contract.setDeliveryTerms(rowMap.get("delivery_terms"));
        contract.setStatus(rowMap.getOrDefault("status", "ACTIVE"));
        contract.setTerminationClause(rowMap.get("termination_clause"));
        contract.setRenewalTerms(rowMap.get("renewal_terms"));
        
        // Parse boolean values
        Boolean autoRenewal = Boolean.FALSE;
        if (rowMap.containsKey("auto_renewal") && !rowMap.get("auto_renewal").isEmpty()) {
            autoRenewal = Boolean.parseBoolean(rowMap.get("auto_renewal"));
        }
        contract.setAutoRenewal(autoRenewal);
        
        contract.setCreatedBy(rowMap.get("created_by"));
        contract.setCreatedAt(LocalDateTime.now());
        contract.setUpdatedAt(LocalDateTime.now());
        
        return contract;
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
     * Load all partner data from CSV files
     * 
     * @return Map of entity types to import results
     */
    public Map<String, ImportResult> loadAllPartnerData() {
        log.info("Loading all partner data from CSV files");
        
        Map<String, ImportResult> results = new HashMap<>();
        
        // Load in a specific order to handle dependencies
        ImportResult partnerResult = loadPartners();
        ImportResult contractResult = loadPartnerContracts();
        
        results.put("Partners", partnerResult);
        results.put("PartnerContracts", contractResult);
        
        return results;
    }
}