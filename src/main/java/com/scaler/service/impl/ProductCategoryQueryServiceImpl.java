package com.scaler.service.impl;

import com.scaler.entity.Category;
import com.scaler.entity.Product;
import com.scaler.entity.ProductCategory;
import com.scaler.repository.ProductCategoryRepository;
import com.scaler.service.ProductCategoryQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductCategoryQueryServiceImpl implements ProductCategoryQueryService {

    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public List<ProductCategory> getAllProductCategories() {
        return productCategoryRepository.findAll();
    }

    @Override
    public Optional<ProductCategory> getProductCategoryById(UUID id) {
        return productCategoryRepository.findById(id);
    }

    @Override
    public List<ProductCategory> getProductCategoriesByProductId(UUID productId) {
        return productCategoryRepository.findByProductId(productId);
    }

    @Override
    public List<ProductCategory> getProductCategoriesByCategoryId(UUID categoryId) {
        return productCategoryRepository.findByCategoryId(categoryId);
    }

    @Override
    public List<ProductCategory> getProductCategoriesByMerchantId(UUID merchantId) {
        return productCategoryRepository.findByMerchantId(merchantId);
    }

    @Override
    public Optional<ProductCategory> getProductCategoryByProductAndCategory(UUID productId, UUID categoryId) {
        return productCategoryRepository.findByProductIdAndCategoryId(productId, categoryId);
    }

    @Override
    public Optional<ProductCategory> getProductCategoryByProductCategoryAndMerchant(UUID productId, UUID categoryId, UUID merchantId) {
        return productCategoryRepository.findByProductIdAndCategoryIdAndMerchantId(productId, categoryId, merchantId);
    }

    @Override
    public List<Category> getCategoriesByProductId(UUID productId) {
        return productCategoryRepository.findByProductId(productId).stream()
                .map(ProductCategory::getCategory)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsByCategoryId(UUID categoryId) {
        return productCategoryRepository.findByCategoryId(categoryId).stream()
                .map(ProductCategory::getProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsByCategoryIdAndMerchantId(UUID categoryId, UUID merchantId) {
        return productCategoryRepository.findByCategoryIdAndMerchantId(categoryId, merchantId).stream()
                .map(ProductCategory::getProduct)
                .collect(Collectors.toList());
    }
}