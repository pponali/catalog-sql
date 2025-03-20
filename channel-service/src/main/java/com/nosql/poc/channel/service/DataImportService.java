package com.nosql.poc.channel.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.nosql.poc.channel.dto.ChannelPrice;
import com.nosql.poc.channel.event.AuditInfo;
import com.nosql.poc.channel.model.Channel;
import com.nosql.poc.channel.model.ChannelProduct;
import com.nosql.poc.channel.repository.ChannelRepository;
import com.nosql.poc.channel.repository.ChannelProductRepository;
import com.nosql.poc.channel.dto.ImportResult;
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
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service for loading channel-related data from CSV files in the resources/csv directory
 */
@Service
@RequiredArgsConstructor
public class DataImportService {
    
    private static final Logger log = LoggerFactory.getLogger(DataImportService.class);

    private final ResourceLoader resourceLoader;
    private final ChannelRepository channelRepository;
    private final ChannelProductRepository channelProductRepository;

    /**
     * Load channels from channels.csv
     * 
     * @return ImportResult with details of the import operation
     */
    public ImportResult loadChannels() {
        log.info("Loading channels from CSV");
        
        ImportResult result = new ImportResult();
        result.setEntityType("Channel");
        result.setErrors(new ArrayList<>());
        
        try {
            Resource resource = resourceLoader.getResource("classpath:csv/channels.csv");
            
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {
                
                // Skip header
                String[] header = csvReader.readNext();
                if (header == null) {
                    result.setSuccess(false);
                    result.setErrorMessage("Empty CSV file");
                    return result;
                }
                
                List<Channel> channels = new ArrayList<>();
                String[] line;
                int rowCount = 0;
                
                while ((line = csvReader.readNext()) != null) {
                    rowCount++;
                    try {
                        Channel channel = parseChannel(line, header);
                        channels.add(channel);
                    } catch (Exception e) {
                        log.error("Error parsing channel at row {}: {}", rowCount, e.getMessage());
                        result.getErrors().add("Row " + rowCount + ": " + e.getMessage());
                    }
                }
                
                // Save all valid channels
                if (!channels.isEmpty()) {
                    channelRepository.saveAll(channels);
                    result.setSuccess(true);
                    result.setImportedCount(channels.size());
                } else {
                    result.setSuccess(false);
                    result.setErrorMessage("No valid channels found");
                }
                
            } catch (IOException | CsvValidationException e) {
                log.error("Error reading channels CSV", e);
                result.setSuccess(false);
                result.setErrorMessage("Error reading CSV: " + e.getMessage());
            }
            
        } catch (Exception e) {
            log.error("Error loading channels resource", e);
            result.setSuccess(false);
            result.setErrorMessage("Error loading resource: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Load channel products from channel_products.csv
     * 
     * @return ImportResult with details of the import operation
     */
    public ImportResult loadChannelProducts() {
        log.info("Loading channel products from CSV");
        
        ImportResult result = new ImportResult();
        result.setEntityType("ChannelProduct");
        result.setErrors(new ArrayList<>());
        
        try {
            Resource resource = resourceLoader.getResource("classpath:csv/channel_products.csv");
            
            try (Reader reader = new InputStreamReader(resource.getInputStream());
                 CSVReader csvReader = new CSVReader(reader)) {
                
                // Skip header
                String[] header = csvReader.readNext();
                if (header == null) {
                    result.setSuccess(false);
                    result.setErrorMessage("Empty CSV file");
                    return result;
                }
                
                List<ChannelProduct> channelProducts = new ArrayList<>();
                String[] line;
                int rowCount = 0;
                
                while ((line = csvReader.readNext()) != null) {
                    rowCount++;
                    try {
                        ChannelProduct channelProduct = parseChannelProduct(line, header);
                        channelProducts.add(channelProduct);
                    } catch (Exception e) {
                        log.error("Error parsing channel product at row {}: {}", rowCount, e.getMessage());
                        result.getErrors().add("Row " + rowCount + ": " + e.getMessage());
                    }
                }
                
                // Save all valid channel products
                if (!channelProducts.isEmpty()) {
                    channelProductRepository.saveAll(channelProducts);
                    result.setSuccess(true);
                    result.setImportedCount(channelProducts.size());
                } else {
                    result.setSuccess(false);
                    result.setErrorMessage("No valid channel products found");
                }
                
            } catch (IOException | CsvValidationException e) {
                log.error("Error reading channel products CSV", e);
                result.setSuccess(false);
                result.setErrorMessage("Error reading CSV: " + e.getMessage());
            }
            
        } catch (Exception e) {
            log.error("Error loading channel products resource", e);
            result.setSuccess(false);
            result.setErrorMessage("Error loading resource: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Parse a channel from a CSV row
     */
    private Channel parseChannel(String[] row, String[] header) {
        Map<String, String> rowMap = mapRowToHeader(row, header);
        
        // Parse boolean values
        Boolean active = Boolean.TRUE;
        if (rowMap.containsKey("active") && !rowMap.get("active").isEmpty()) {
            active = Boolean.parseBoolean(rowMap.get("active"));
        }
        
        // Create audit info
        AuditInfo auditInfo = AuditInfo.builder()
                .createdAt(LocalDateTime.now())
                .createdBy("system-import")
                .updatedAt(LocalDateTime.now())
                .updatedBy("system-import")
                .build();
        
        // Build the channel
        return Channel.builder()
                .id(UUID.randomUUID().toString())
                .channelId(requireField(rowMap, "channel_id"))
                .name(requireField(rowMap, "name"))
                .type(requireField(rowMap, "type"))
                .description(rowMap.get("description"))
                .active(active)
                .configuration(extractConfigurationMap(rowMap))
                .attributes(extractAttributesMap(rowMap))
                .auditInfo(auditInfo)
                .build();
    }
    
    /**
     * Parse a channel product from a CSV row
     */
    private ChannelProduct parseChannelProduct(String[] row, String[] header) {
        Map<String, String> rowMap = mapRowToHeader(row, header);
        
        // Parse BigDecimal values
        BigDecimal channelPrice = new BigDecimal("0.00");
        try {
            channelPrice = new BigDecimal(rowMap.get("channel_price"));
        } catch (Exception e) {
            log.warn("Invalid channel price: {}, using 0.00", rowMap.get("channel_price"));
        }
        
        // Parse Integer values
        Integer stockQuantity = 0;
        try {
            stockQuantity = Integer.parseInt(rowMap.get("channel_inventory"));
        } catch (Exception e) {
            log.warn("Invalid channel inventory: {}, using 0", rowMap.get("channel_inventory"));
        }
        
        // Create channel price
        ChannelPrice price = new ChannelPrice(channelPrice, rowMap.getOrDefault("currency", "USD"));
        
        // Build the channel product
        return ChannelProduct.builder()
                .id(UUID.randomUUID().toString())
                .productId(requireField(rowMap, "product_id"))
                .channelId(requireField(rowMap, "channel_id"))
                .sku(rowMap.get("channel_product_id"))
                .price(price)
                .channelPrice(channelPrice)
                .channelCurrency(rowMap.getOrDefault("currency", "USD"))
                .listingStatus(rowMap.getOrDefault("status", "PENDING_APPROVAL"))
                .stockQuantity(stockQuantity)
                .active(Boolean.TRUE)
                .availabilityStatus(stockQuantity > 0 ? "IN_STOCK" : "OUT_OF_STOCK")
                .categories(extractCategories(rowMap))
                .attributes(extractStringAttributesMap(rowMap))
                .channelAttributes(extractChannelAttributesMap(rowMap))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .createdBy("system-import")
                .updatedBy("system-import")
                .build();
    }
    
    /**
     * Extract configuration map from row data
     */
    private Map<String, Object> extractConfigurationMap(Map<String, String> rowMap) {
        Map<String, Object> config = new HashMap<>();
        
        if (rowMap.containsKey("integration_endpoint")) {
            config.put("integrationEndpoint", rowMap.get("integration_endpoint"));
        }
        
        if (rowMap.containsKey("api_key")) {
            config.put("apiKey", rowMap.get("api_key"));
        }
        
        if (rowMap.containsKey("api_secret")) {
            config.put("apiSecret", rowMap.get("api_secret"));
        }
        
        return config;
    }
    
    /**
     * Extract attributes map from row data
     */
    private Map<String, Object> extractAttributesMap(Map<String, String> rowMap) {
        Map<String, Object> attributes = new HashMap<>();
        
        if (rowMap.containsKey("region")) {
            attributes.put("region", rowMap.get("region"));
        }
        
        return attributes;
    }
    
    /**
     * Extract string attributes map from row data
     */
    private Map<String, String> extractStringAttributesMap(Map<String, String> rowMap) {
        Map<String, String> attributes = new HashMap<>();
        
        if (rowMap.containsKey("fulfillment_type")) {
            attributes.put("fulfillmentType", rowMap.get("fulfillment_type"));
        }
        
        if (rowMap.containsKey("channel_url")) {
            attributes.put("channelUrl", rowMap.get("channel_url"));
        }
        
        return attributes;
    }
    
    /**
     * Extract channel-specific attributes map from row data
     */
    private Map<String, Object> extractChannelAttributesMap(Map<String, String> rowMap) {
        Map<String, Object> attributes = new HashMap<>();
        
        // Add sync-related attributes
        attributes.put("syncSuccess", true);
        attributes.put("syncMessage", "Initial import");
        attributes.put("lastSyncAttempt", LocalDateTime.now());
        
        return attributes;
    }
    
    /**
     * Extract categories list from row data
     */
    private List<String> extractCategories(Map<String, String> rowMap) {
        List<String> categories = new ArrayList<>();
        
        if (rowMap.containsKey("category_mapping") && !rowMap.get("category_mapping").isEmpty()) {
            String categoryMapping = rowMap.get("category_mapping");
            if (categoryMapping.contains(",")) {
                // Multiple categories, split by comma
                String[] categoryArray = categoryMapping.split(",");
                for (String category : categoryArray) {
                    categories.add(category.trim());
                }
            } else {
                // Single category
                categories.add(categoryMapping.trim());
            }
        }
        
        return categories;
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
     * Load all channel data from CSV files
     * 
     * @return Map of entity types to import results
     */
    public Map<String, ImportResult> loadAllChannelData() {
        log.info("Loading all channel data from CSV files");
        
        Map<String, ImportResult> results = new HashMap<>();
        
        // Load in a specific order to handle dependencies
        ImportResult channelResult = loadChannels();
        ImportResult channelProductResult = loadChannelProducts();
        
        results.put("Channels", channelResult);
        results.put("ChannelProducts", channelProductResult);
        
        return results;
    }
}