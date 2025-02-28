package com.scaler.builder;

import com.scaler.entity.*;
import com.scaler.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Test cases for various entity combinations in the catalog system.
 * This class provides comprehensive test scenarios for:
 * - Channel-Catalog relationships
 * - Product categorization and channel assignment
 * - Product feature mappings and values
 * - Seller product relationships
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TestUseCases {

    @Autowired private ChannelCatalogRepository channelCatalogRepository;
    @Autowired private ProductCategoryRepository productCategoryRepository;
    @Autowired private ProductChannelRepository productChannelRepository;
    @Autowired private ProductFeatureMappingRepository productFeatureMappingRepository;
    @Autowired private ProductFeatureValueMappingRepository productFeatureValueMappingRepository;
    @Autowired private SellerProductRepository sellerProductRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ChannelRepository channelRepository;
    @Autowired private CatalogRepository catalogRepository;
    @Autowired private ProductFeatureRepository productFeatureRepository;
    @Autowired private ProductFeatureValueRepository productFeatureValueRepository;
    @Autowired private SellerRepository sellerRepository;

    /**
     * Test case: Multi-channel catalog with product features
     * Scenario: A product is listed in multiple channels with different features per channel
     */
    @Transactional
    public void testMultiChannelCatalogWithFeatures(UUID productId, List<UUID> channelIds) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Assign product to multiple channels
        for (UUID channelId : channelIds) {
            Channel channel = channelRepository.findById(channelId)
                    .orElseThrow(() -> new RuntimeException("Channel not found"));

            ProductChannel productChannel = ProductChannel.builder()
                    .product(product)
                    .channel(channel)
                    .createdBy("SYSTEM")
                    .build();

            productChannelRepository.save(productChannel);
        }

        log.info("Product {} assigned to {} channels", productId, channelIds.size());
    }

    /**
     * Test case: Product with category-specific features
     * Scenario: Product is assigned to a category with specific feature requirements
     */
    @Transactional
    public void testProductCategoryFeatures(UUID productId, UUID categoryId, List<ProductFeature> features) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Assign product to category
        ProductCategory productCategory = ProductCategory.builder()
                .product(product)
                .category(category)
                .createdBy("SYSTEM")
                .build();

        productCategoryRepository.save(productCategory);

        // Assign features to product
        for (ProductFeature feature : features) {
            ProductFeatureMapping mapping = ProductFeatureMapping.builder()
                    .product(product)
                    .feature(feature)
                    .createdBy("SYSTEM")
                    .build();

            productFeatureMappingRepository.save(mapping);
        }

        log.info("Product {} assigned to category {} with {} features", 
                productId, categoryId, features.size());
    }

    /**
     * Test case: Seller product with channel-specific pricing
     * Scenario: A seller lists a product across multiple channels with different prices
     */
    @Transactional
    public void testSellerProductChannelPricing(UUID productId, UUID sellerId, List<UUID> channelIds) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        // Create seller product
        SellerProduct sellerProduct = SellerProduct.builder()
                .product(product)
                .seller(seller)
                .status("ACTIVE")
                .createdBy("SYSTEM")
                .build();

        sellerProductRepository.save(sellerProduct);

        // Assign to channels
        for (UUID channelId : channelIds) {
            Channel channel = channelRepository.findById(channelId)
                    .orElseThrow(() -> new RuntimeException("Channel not found"));

            ProductChannel productChannel = ProductChannel.builder()
                    .product(product)
                    .channel(channel)
                    .createdBy("SYSTEM")
                    .build();

            productChannelRepository.save(productChannel);
        }

        log.info("Seller product created for seller {} and assigned to {} channels", 
                sellerId, channelIds.size());
    }

    /**
     * Test case: Channel catalog with feature validation
     * Scenario: Products in a channel-specific catalog must meet feature requirements
     */
    @Transactional
    public void testChannelCatalogFeatureValidation(UUID catalogId, UUID channelId, List<ProductFeature> requiredFeatures) {
        Catalog catalog = catalogRepository.findById(catalogId)
                .orElseThrow(() -> new RuntimeException("Catalog not found"));
        
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        // Create channel catalog
        ChannelCatalog channelCatalog = ChannelCatalog.builder()
                .catalog(catalog)
                .channel(channel)
                .isEnabled(true)
                .isDefault(false)
                .displayOrder(1)
                .createdBy("SYSTEM")
                .build();

        channelCatalogRepository.save(channelCatalog);

        // Add required features
        for (ProductFeature feature : requiredFeatures) {
            feature.setCreatedBy("SYSTEM");
            productFeatureRepository.save(feature);
        }

        log.info("Channel catalog created with {} required features", requiredFeatures.size());
    }

    /**
     * Test case: Product feature value inheritance
     * Scenario: Product inherits feature values from category template
     */
    @Transactional
    public void testProductFeatureValueInheritance(UUID productId, UUID categoryId, List<ProductFeatureValue> values) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Assign product to category
        ProductCategory productCategory = ProductCategory.builder()
                .product(product)
                .category(category)
                .createdBy("SYSTEM")
                .build();

        productCategoryRepository.save(productCategory);

        // Assign feature values
        for (ProductFeatureValue value : values) {
            value.setCreatedBy("SYSTEM");
            productFeatureValueRepository.save(value);

            ProductFeatureValueMapping mapping = ProductFeatureValueMapping.builder()
                    .product(product)
                    .category(category)
                    .featureValue(value)
                    .createdBy("SYSTEM")
                    .isActive(true)
                    .isPrimary(false)
                    .displayOrder(1)
                    .build();

            productFeatureValueMappingRepository.save(mapping);
        }

        log.info("Product {} inherited {} feature values from category {}", 
                productId, values.size(), categoryId);
    }
}
