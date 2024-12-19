package com.example.service.impl;

import com.example.entity.CategoryFeatureTemplate;
import com.example.entity.Product;
import com.example.entity.ProductFeature;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.ProductFeatureRepository;
import com.example.service.ProductFeatureService;
import com.example.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductFeatureServiceImpl implements ProductFeatureService {

    private final ProductFeatureRepository featureRepository;
    private final ValidationService validationService;

    @Override
    public ProductFeature findById(Long id) {
        return featureRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Feature not found: " + id));
    }

    @Override
    public ProductFeature findByProductAndTemplate(Product product, CategoryFeatureTemplate template) {
        return featureRepository.findByProductAndTemplate(product, template)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Feature not found for product %s and template %s", 
                    product.getId(), template.getCode())));
    }

    @Override
    public List<ProductFeature> findByProduct(Product product) {
        return featureRepository.findByProduct(product);
    }

    @Override
    public ProductFeature save(ProductFeature feature) {
        // Validate all values before saving
        feature.getValues().forEach(value -> {
            if (!validationService.validateValue(feature.getTemplate(), value.getValue())) {
                throw new IllegalArgumentException(
                    String.format("Invalid value %s for feature template %s", 
                        value.getValue(), feature.getTemplate().getCode()));
            }
        });
        return featureRepository.save(feature);
    }

    @Override
    public void delete(ProductFeature feature) {
        featureRepository.delete(feature);
    }

    @Override
    public void deleteByProduct(Product product) {
        featureRepository.deleteByProduct(product);
    }
}
