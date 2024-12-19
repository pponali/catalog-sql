package com.example.service.impl;

import com.example.entity.CategoryFeatureTemplate;
import com.example.entity.Product;
import com.example.entity.ProductFeature;
import com.example.entity.ProductFeatureValue;
import com.example.exception.ResourceNotFoundException;
import com.example.repository.ProductFeatureRepository;
import com.example.service.ProductFeatureService;
import com.example.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductFeatureServiceImpl implements ProductFeatureService {

    private final ProductFeatureRepository featureRepository;
    private final ValidationService validationService;

    @Override
    public ProductFeature findById(Long id) {
        return featureRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product feature not found with id: " + id));
    }

    @Override
    public ProductFeature findByProductAndTemplate(Product product, CategoryFeatureTemplate template) {
        return featureRepository.findByProductAndTemplate(product, template)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Feature not found for product %s and template %s", 
                    product.getId(), template.getCode())));
    }

    @Override
    public ProductFeature save(ProductFeature feature) {
        validateFeature(feature);
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

    @Override
    public List<ProductFeature> findByProduct(Product product) {
        return featureRepository.findByProduct(product);
    }

    private void validateFeature(ProductFeature feature) {
        CategoryFeatureTemplate template = feature.getTemplate();
        Set<ProductFeatureValue> values = feature.getValues();

        if (template.isMandatory() && (values == null || values.isEmpty())) {
            throw new IllegalArgumentException("Feature is mandatory but no value provided");
        }

        if (!template.isMultiValued() && values != null && values.size() > 1) {
            throw new IllegalArgumentException("Feature does not support multiple values");
        }

        if (values != null) {
            for (ProductFeatureValue value : values) {
                if (!validationService.validateValue(value, template)) {
                    throw new IllegalArgumentException("Invalid value for feature: " + template.getCode());
                }
            }
        }
    }
}
