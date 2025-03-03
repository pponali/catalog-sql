package com.scaler.service.impl;

import com.scaler.entity.Platform;
import com.scaler.entity.Product;
import com.scaler.entity.ProductPlatform;
import com.scaler.repository.ProductPlatformRepository;
import com.scaler.service.ProductPlatformQueryService;
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
public class ProductPlatformQueryServiceImpl implements ProductPlatformQueryService {

    private final ProductPlatformRepository productPlatformRepository;

    @Override
    public List<ProductPlatform> getAllProductPlatforms() {
        return productPlatformRepository.findAll();
    }

    @Override
    public Optional<ProductPlatform> getProductPlatformById(UUID id) {
        return productPlatformRepository.findById(id);
    }

    @Override
    public List<ProductPlatform> getProductPlatformsByProductId(UUID productId) {
        return productPlatformRepository.findByProductId(productId);
    }

    @Override
    public List<ProductPlatform> getProductPlatformsByPlatformId(UUID platformId) {
        return productPlatformRepository.findByPlatformId(platformId);
    }

    @Override
    public Optional<ProductPlatform> getProductPlatformByProductAndPlatform(UUID productId, UUID platformId) {
        return productPlatformRepository.findByProductIdAndPlatformId(productId, platformId);
    }

    @Override
    public List<Platform> getPlatformsByProductId(UUID productId) {
        return productPlatformRepository.findByProductId(productId).stream()
                .map(ProductPlatform::getPlatform)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsByPlatformId(UUID platformId) {
        return productPlatformRepository.findByPlatformId(platformId).stream()
                .map(ProductPlatform::getProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsByPlatformCode(String platformCode) {
        return productPlatformRepository.findByPlatformCode(platformCode).stream()
                .map(ProductPlatform::getProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsByPlatformIdAndActive(UUID platformId, boolean isActive) {
        return productPlatformRepository.findByPlatformIdAndIsActive(platformId, isActive).stream()
                .map(ProductPlatform::getProduct)
                .collect(Collectors.toList());
    }
}