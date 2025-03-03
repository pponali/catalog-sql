package com.scaler.service.impl;

import com.scaler.entity.*;
import com.scaler.repository.ProductFeatureValueMappingRepository;
import com.scaler.service.ProductFeatureValueMappingQueryService;
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
public class ProductFeatureValueMappingQueryServiceImpl implements ProductFeatureValueMappingQueryService {

    private final ProductFeatureValueMappingRepository productFeatureValueMappingRepository;

    @Override
    public List<ProductFeatureValueMapping> getAllProductFeatureValueMappings() {
        return productFeatureValueMappingRepository.findAll();
    }

    @Override
    public Optional<ProductFeatureValueMapping> getProductFeatureValueMappingById(UUID id) {
        return productFeatureValueMappingRepository.findById(id);
    }

    @Override
    public List<ProductFeatureValueMapping> getProductFeatureValueMappingsByProductId(UUID productId) {
        return productFeatureValueMappingRepository.findByProductId(productId);
    }

    @Override
    public List<ProductFeatureValueMapping> getProductFeatureValueMappingsByFeatureId(UUID featureId) {
        return productFeatureValueMappingRepository.findByFeatureId(featureId);
    }

    @Override
    public List<ProductFeatureValueMapping> getProductFeatureValueMappingsByFeatureValueId(UUID featureValueId) {
        return productFeatureValueMappingRepository.findByFeatureValueId(featureValueId);
    }

    @Override
    public List<ProductFeatureValueMapping> getProductFeatureValueMappingsByTemplateId(UUID templateId) {
        return productFeatureValueMappingRepository.findByTemplateId(templateId);
    }

    @Override
    public Optional<ProductFeatureValueMapping> getProductFeatureValueMappingByProductAndFeature(UUID productId, UUID featureId) {
        return productFeatureValueMappingRepository.findByProductIdAndFeatureId(productId, featureId);
    }

    @Override
    public List<ProductFeatureValueMapping> getProductFeatureValueMappingsByProductAndTemplate(UUID productId, UUID templateId) {
        return productFeatureValueMappingRepository.findByProductIdAndTemplateId(productId, templateId);
    }

    @Override
    public List<ProductFeature> getFeaturesByProductId(UUID productId) {
        return productFeatureValueMappingRepository.findByProductId(productId).stream()
                .map(ProductFeatureValueMapping::getFeature)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValue> getFeatureValuesByProductId(UUID productId) {
        return productFeatureValueMappingRepository.findByProductId(productId).stream()
                .map(ProductFeatureValueMapping::getFeatureValue)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductFeatureValue> getFeatureValuesByProductAndFeature(UUID productId, UUID featureId) {
        return productFeatureValueMappingRepository.findByProductIdAndFeatureId(productId, featureId)
                .map(mapping -> List.of(mapping.getFeatureValue()))
                .orElse(List.of());
    }

    @Override
    public List<Product> getProductsByFeatureId(UUID featureId) {
        return productFeatureValueMappingRepository.findByFeatureId(featureId).stream()
                .map(ProductFeatureValueMapping::getProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsByFeatureValueId(UUID featureValueId) {
        return productFeatureValueMappingRepository.findByFeatureValueId(featureValueId).stream()
                .map(ProductFeatureValueMapping::getProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsByTemplateId(UUID templateId) {
        return productFeatureValueMappingRepository.findByTemplateId(templateId).stream()
                .map(ProductFeatureValueMapping::getProduct)
                .collect(Collectors.toList());
    }
}