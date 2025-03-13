package com.scaler.service;

import com.scaler.dto.ProductDTO;
import com.scaler.entity.*;
import com.scaler.mapper.ProductMapper;
import com.scaler.repository.ProductFeatureMappingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Service for product mapping operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductMappingService {

    private final ProductFeatureMappingRepository productFeatureMappingRepository;
    private final ProductMapper productMapper;

    /**
     * Creates and saves a mapping between a product and a feature
     * 
     * @param product The product to map
     * @param feature The feature to map
     * @return The created and saved mapping
     */
    public ProductFeatureMapping createAndSaveMapping(Product product, ProductFeature feature) {
        if (product == null || feature == null) {
            throw new IllegalArgumentException("Product and feature must not be null");
        }
        return productFeatureMappingRepository.save(
                ProductFeatureMapping.builder()
                        .product(product)
                        .feature(feature)
                        .createdBy("SYSTEM")
                        .build()
        );
    }

    /**
     * Creates sample prices and inventory for products of a merchant
     * 
     * @param merchant The merchant
     * @param channel The channel
     * @param seller The seller
     * @param productRepository Repository for accessing products
     * @param productPriceRepository Repository for saving prices
     * @param productInventoryRepository Repository for saving inventory
     */
    public void createSamplePricesAndInventory(
            Merchant merchant, 
            Channel channel, 
            Seller seller,
            org.springframework.data.repository.Repository<Product, ?> productRepository,
            org.springframework.data.repository.Repository<ProductPrice, ?> productPriceRepository,
            org.springframework.data.repository.Repository<ProductInventory, ?> productInventoryRepository) {
        
        // Get all products for the merchant
        List<Product> products = ((com.scaler.repository.ProductRepository)productRepository).findByMerchantId(merchant.getId());

        for (Product product : products) {
            // Create price for each product using builder
            ProductPrice price = com.scaler.builder.ProductPriceBuilder.createPrice(product, merchant, channel, seller);
            ((com.scaler.repository.ProductPriceRepository)productPriceRepository).save(price);

            // Create inventory for each product using builder
            ProductInventory inventory = com.scaler.builder.ProductInventoryBuilder.createInventory(product, merchant, channel, seller);
            ((com.scaler.repository.ProductInventoryRepository)productInventoryRepository).save(inventory);
        }
    }

    /**
     * Validates a product against category-specific rules
     * 
     * @param product The product to validate
     * @param categoryValidationService Service for category validation
     * @return Map of validation results with feature code as key and list of error messages as value
     */
    public Map<String, List<String>> validateProduct(Product product, com.scaler.validation.service.CategoryValidationService categoryValidationService) {
        log.info("Validating product: {}", product.getName());
        
        // Get all product categories
        java.util.Set<ProductCategory> productCategories = product.getProductCategories();
        if (productCategories == null || productCategories.isEmpty()) {
            log.warn("Product has no categories: {}", product.getName());
            return Map.of();
        }
        
        // Validate against each category
        Map<String, List<String>> validationResults = new HashMap<>();
        for (ProductCategory productCategory : productCategories) {
            Category category = productCategory.getCategory();
            Map<String, List<String>> categoryResults = categoryValidationService.validateProduct(product, category);
            
            // Merge results
            for (Map.Entry<String, List<String>> entry : categoryResults.entrySet()) {
                validationResults.computeIfAbsent(entry.getKey(), k -> new java.util.ArrayList<>()).addAll(entry.getValue());
            }
        }
        
        return validationResults;
    }
    
    /**
     * Converts a Product entity to a ProductDTO
     * 
     * @param product The product entity to convert
     * @return The converted ProductDTO
     */
    public ProductDTO toDTO(Product product) {
        return productMapper.toDTO(product);
    }
    
    /**
     * Converts a ProductDTO to a Product entity
     * 
     * @param productDTO The ProductDTO to convert
     * @return The converted Product entity
     */
    public Product toEntity(ProductDTO productDTO) {
        return productMapper.toEntity(productDTO);
    }
}
