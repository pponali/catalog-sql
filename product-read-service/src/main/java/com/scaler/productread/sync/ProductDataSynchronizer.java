package com.scaler.productread.sync;

import com.scaler.productread.client.CatalogServiceClient;
import com.scaler.productread.document.ProductDocument;
import com.scaler.productread.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Component responsible for synchronizing product data from the catalog service to Elasticsearch.
 */
@Slf4j
@Component
public class ProductDataSynchronizer {
    
    private final CatalogServiceClient catalogServiceClient;
    private final ProductService productService;
    private final ProductMapper productMapper;
    
    private LocalDateTime lastSyncTime = LocalDateTime.now().minusDays(7); // Initialize to sync last 7 days data
    
    @Autowired
    public ProductDataSynchronizer(
            CatalogServiceClient catalogServiceClient,
            ProductService productService,
            ProductMapper productMapper) {
        this.catalogServiceClient = catalogServiceClient;
        this.productService = productService;
        this.productMapper = productMapper;
    }
    
    /**
     * Scheduled task to synchronize updated products from catalog service.
     * Runs every 5 minutes by default.
     */
    @Scheduled(fixedDelayString = "${sync.product.interval:300000}")
    public void synchronizeUpdatedProducts() {
        log.info("Starting product data synchronization from catalog service. Last sync time: {}", lastSyncTime);
        try {
            // Get updated products from catalog service
            // Use default pagination values (page 0, size 100)
            Map<String, Object> response = catalogServiceClient.getUpdatedProducts(lastSyncTime, 0, 100);
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> productDataList = (List<Map<String, Object>>) response.get("content");
            
            if (productDataList == null || productDataList.isEmpty()) {
                log.info("No updated products found since last sync time");
                lastSyncTime = LocalDateTime.now();
                return;
            }
            
            log.info("Found {} updated products to synchronize", productDataList.size());
            
            // Process and index each product
            for (Map<String, Object> productData : productDataList) {
                try {
                    UUID productId = UUID.fromString(productData.get("id").toString());
                    
                    // Get complete product data including related entities
                    enrichProductData(productData, productId);
                    
                    // Convert to ProductDocument and index
                    ProductDocument productDocument = productMapper.mapToProductDocument(productData);
                    productService.indexProduct(productDocument);
                    
                    log.debug("Synchronized product: {}", productId);
                } catch (Exception e) {
                    log.error("Error synchronizing product: {}", productData.get("id"), e);
                }
            }
            
            // Update last sync time
            lastSyncTime = LocalDateTime.now();
            log.info("Product data synchronization completed. New last sync time: {}", lastSyncTime);
        } catch (Exception e) {
            log.error("Error during product data synchronization", e);
        }
    }
    
    /**
     * Triggered to synchronize a specific product by ID.
     * Can be called by message listeners when product update events are received.
     *
     * @param productId The ID of the product to synchronize
     * @return true if synchronization was successful, false otherwise
     */
    public boolean synchronizeProduct(UUID productId) {
        log.info("Synchronizing single product: {}", productId);
        
        try {
            // Get product data from catalog service
            Map<String, Object> productData = catalogServiceClient.getProduct(productId);
            
            if (productData == null || productData.isEmpty()) {
                log.warn("Product not found in catalog service: {}", productId);
                return false;
            }
            
            // Enrich with related data
            enrichProductData(productData, productId);
            
            // Convert to ProductDocument and index
            ProductDocument productDocument = productMapper.mapToProductDocument(productData);
            productService.indexProduct(productDocument);
            
            log.info("Product synchronized successfully: {}", productId);
            return true;
        } catch (Exception e) {
            log.error("Error synchronizing product: {}", productId, e);
            return false;
        }
    }
    
    /**
     * Enriches product data with related entities (features, categories, etc.).
     *
     * @param productData Base product data
     * @param productId   Product ID
     */
    private void enrichProductData(Map<String, Object> productData, UUID productId) {
        try {
            // Get product features
            List<Map<String, Object>> features = catalogServiceClient.getProductFeatures(productId);
            if (features != null && !features.isEmpty()) {
                productData.put("features", features);
            }
            
            // Get related sellers
            List<Map<String, Object>> sellers = catalogServiceClient.getProductSellers(productId);
            if (sellers != null && !sellers.isEmpty()) {
                productData.put("sellers", sellers);
            }
            
            // Get channel availability
            List<Map<String, Object>> channels = catalogServiceClient.getProductChannels(productId);
            if (channels != null && !channels.isEmpty()) {
                productData.put("channels", channels);
            }
            
            // Get categories with details
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> categoryMappings = (List<Map<String, Object>>) productData.getOrDefault("categories", List.of());
            
            if (!categoryMappings.isEmpty()) {
                List<Map<String, Object>> categoryDetails = categoryMappings.stream()
                        .map(mapping -> {
                            UUID categoryId = UUID.fromString(mapping.get("categoryId").toString());
                            Map<String, Object> category = catalogServiceClient.getCategory(categoryId);
                            return category;
                        })
                        .filter(category -> !category.isEmpty())
                        .collect(Collectors.toList());
                
                productData.put("categoryDetails", categoryDetails);
            }
        } catch (Exception e) {
            log.error("Error enriching product data for product: {}", productId, e);
        }
    }
    
    /**
     * Force a full synchronization of all products.
     */
    public void forceSyncAllProducts() {
        log.info("Starting full product synchronization...");
        try {
            int page = 0;
            int size = 100;
            int totalPages = 1;
            
            // Paginate through all products
            for (page = 0; page < totalPages; page++) {
                Map<String, Object> response = catalogServiceClient.getProducts(null, page, size, null, null);
                
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> productDataList = (List<Map<String, Object>>) response.get("content");
                totalPages = (int) response.get("totalPages");
                
                log.info("Syncing products batch {}/{}, found {} products", page + 1, totalPages, productDataList.size());
                
                // Process each product
                for (Map<String, Object> productData : productDataList) {
                    UUID productId = UUID.fromString(productData.get("id").toString());
                    
                    try {
                        // Enrich with related data
                        enrichProductData(productData, productId);
                        
                        // Convert to ProductDocument and index
                        ProductDocument productDocument = productMapper.mapToProductDocument(productData);
                        productService.indexProduct(productDocument);
                    } catch (Exception e) {
                        log.error("Error synchronizing product: {}", productId, e);
                    }
                }
            }
            
            log.info("Full product synchronization completed");
        } catch (Exception e) {
            log.error("Error during full product synchronization", e);
        }
    }
}