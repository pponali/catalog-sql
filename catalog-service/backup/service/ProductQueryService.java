package com.scaler.service;

import com.scaler.entity.*;
import com.scaler.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryService {

    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductInventoryRepository productInventoryRepository;
    private final CatalogRepository catalogRepository;

    public Optional<Product> getProductById(UUID productId) {
        return productRepository.findById(productId);
    }

    public Optional<Product> getProductByIdAndMerchant(UUID productId, UUID merchantId) {
        return productRepository.findByIdAndMerchantId(productId, merchantId);
    }

    public Optional<Product> getProductByIdAndChannel(UUID productId, UUID channelId) {
        return productRepository.findByIdAndChannelId(productId, channelId);
    }

    public Optional<Product> getProductByIdAndSeller(UUID productId, UUID sellerId) {
        return productRepository.findByIdAndSellerId(productId, sellerId);
    }

    public Optional<Product> getProductByIdAndCategory(UUID productId, UUID categoryId) {
        return productRepository.findByIdAndCategoryId(productId, categoryId);
    }

    public Optional<Product> getProductByIdMerchantAndChannel(UUID productId, UUID merchantId, UUID channelId) {
        return productRepository.findByIdAndMerchantIdAndChannelId(productId, merchantId, channelId);
    }

    public Optional<Product> getProductByIdMerchantAndCategory(UUID productId, UUID merchantId, UUID categoryId) {
        return productRepository.findByIdAndMerchantIdAndCategoryId(productId, merchantId, categoryId);
    }

    public Optional<Product> getProductByIdSellerAndCategory(UUID productId, UUID sellerId, UUID categoryId) {
        return productRepository.findByIdAndSellerIdAndCategoryId(productId, sellerId, categoryId);
    }

    public Optional<Product> getProductByIdChannelAndCategory(UUID productId, UUID channelId, UUID categoryId) {
        return productRepository.findByIdAndChannelIdAndCategoryId(productId, channelId, categoryId);
    }

    public Optional<Product> getProductByIdMerchantChannelAndSeller(UUID productId, UUID merchantId, UUID channelId, UUID sellerId) {
        return productRepository.findByIdAndMerchantIdAndChannelIdAndSellerId(productId, merchantId, channelId, sellerId);
    }

    public Optional<Product> getProductByIdMerchantChannelAndCategory(UUID productId, UUID merchantId, UUID channelId, UUID categoryId) {
        return productRepository.findByIdAndMerchantIdAndChannelIdAndCategoryId(productId, merchantId, channelId, categoryId);
    }

    public Optional<Product> getProductByIdMerchantSellerAndCategory(UUID productId, UUID merchantId, UUID sellerId, UUID categoryId) {
        return productRepository.findByIdAndMerchantIdAndSellerIdAndCategoryId(productId, merchantId, sellerId, categoryId);
    }

    public Optional<Product> getProductByIdChannelSellerAndCategory(UUID productId, UUID channelId, UUID sellerId, UUID categoryId) {
        return productRepository.findByIdAndChannelIdAndSellerIdAndCategoryId(productId, channelId, sellerId, categoryId);
    }

    public Optional<Product> getProductByIdMerchantChannelSellerAndCategory(UUID productId, UUID merchantId, UUID channelId, UUID sellerId, UUID categoryId) {
        return productRepository.findByIdAndMerchantIdAndChannelIdAndSellerIdAndCategoryId(productId, merchantId, channelId, sellerId, categoryId);
    }

    public List<ProductPrice> getProductPricing(UUID productId, UUID channelId, UUID sellerId) {
        return productPriceRepository.findActiveProductPrices(productId, channelId, sellerId);
    }

    public Optional<ProductInventory> getProductInventory(UUID productId, UUID merchantId, UUID channelId, UUID sellerId) {
        return productInventoryRepository.findByProductIdAndMerchantIdAndChannelId(productId, merchantId, channelId, sellerId);
    }

    public Optional<Product> getProductByIdChannelAndSeller(UUID productId, UUID channelId, UUID sellerId) {
        return productRepository.findByIdAndChannelIdAndSellerId(productId, channelId, sellerId);
    }

}
