package com.scaler.service.impl;

import com.scaler.entity.CategoryFeatureTemplate;
import com.scaler.entity.Product;
import com.scaler.entity.ProductFeature;
import com.scaler.entity.ProductFeatureValue;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.exception.ServiceException;
import com.scaler.exception.ValidationException;
import com.scaler.repository.ProductFeatureRepository;
import com.scaler.repository.ProductFeatureValueRepository;
import com.scaler.service.ProductFeatureService;
import com.scaler.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductFeatureServiceImpl implements ProductFeatureService {

    private final ProductFeatureRepository featureRepository;
    private final ProductFeatureValueRepository productFeatureValueRepository;
    private final ValidationService validationService;

    @Override
    public ProductFeature findById(Long id) {
        String operationId = UUID.randomUUID().toString();
        log.debug("Operation ID: {} - Finding ProductFeature by ID: {}", operationId, id);
        
        try {
            return featureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Product feature not found with ID: %d", id)));
        } catch (ResourceNotFoundException e) {
            log.error("Operation ID: {} - {}", operationId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Operation ID: {} - Unexpected error while finding ProductFeature by ID", operationId, e);
            throw new ServiceException("Failed to retrieve product feature", e);
        }
    }

    @Override
    public ProductFeature findByProductAndTemplate(Product product, CategoryFeatureTemplate template) {
        String operationId = UUID.randomUUID().toString();
        log.debug("Operation ID: {} - Finding ProductFeature by Product ID: {} and Template Code: {}", 
            operationId, product.getId(), template.getCode());
        
        try {
            return featureRepository.findByProductAndTemplate(product, template)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("Feature not found for product %s and template %s", 
                        product.getId(), template.getCode())));
        } catch (ResourceNotFoundException e) {
            log.error("Operation ID: {} - {}", operationId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Operation ID: {} - Unexpected error while finding ProductFeature by Product and Template", 
                operationId, e);
            throw new ServiceException("Failed to retrieve product feature", e);
        }
    }

    @Override
    public ProductFeature save(ProductFeature feature) {
        String operationId = UUID.randomUUID().toString();
        LocalDateTime start = LocalDateTime.now();
        log.info("Operation ID: {} - Starting save operation for ProductFeature", operationId);
        
        try {
            if (feature == null) {
                throw new ValidationException("ProductFeature cannot be null");
            }
            
            validateFeature(feature);
            
            ProductFeature savedFeature = featureRepository.save(feature);
            
            Duration duration = Duration.between(start, LocalDateTime.now());
            log.info("Operation ID: {} - Successfully saved ProductFeature in {} ms", 
                operationId, duration.toMillis());
            
            return savedFeature;
            
        } catch (ValidationException e) {
            log.error("Operation ID: {} - Validation error: {}", operationId, e.getMessage());
            throw e;
        } catch (DataIntegrityViolationException e) {
            log.error("Operation ID: {} - Data integrity violation while saving ProductFeature", operationId, e);
            throw new ValidationException("Failed to save product feature due to data constraint violation", e);
        } catch (Exception e) {
            log.error("Operation ID: {} - Unexpected error during save operation", operationId, e);
            throw new ServiceException("Failed to save product feature", e);
        }
    }

    @Override
    public void delete(ProductFeature feature) {
        String operationId = UUID.randomUUID().toString();
        log.info("Operation ID: {} - Deleting ProductFeature with ID: {}", 
            operationId, feature != null ? feature.getId() : null);
        
        try {
            if (feature == null) {
                throw new ValidationException("ProductFeature cannot be null");
            }
            
            featureRepository.delete(feature);
            log.info("Operation ID: {} - Successfully deleted ProductFeature", operationId);
            
        } catch (ValidationException e) {
            log.error("Operation ID: {} - Validation error: {}", operationId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Operation ID: {} - Unexpected error while deleting ProductFeature", operationId, e);
            throw new ServiceException("Failed to delete product feature", e);
        }
    }

    @Override
    public void deleteByProduct(Product product) {
        String operationId = UUID.randomUUID().toString();
        log.info("Operation ID: {} - Deleting ProductFeatures for Product ID: {}", 
            operationId, product != null ? product.getId() : null);
        
        try {
            if (product == null) {
                throw new ValidationException("Product cannot be null");
            }
            
            featureRepository.deleteByProduct(product);
            log.info("Operation ID: {} - Successfully deleted ProductFeatures for Product", operationId);
            
        } catch (ValidationException e) {
            log.error("Operation ID: {} - Validation error: {}", operationId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Operation ID: {} - Unexpected error while deleting ProductFeatures by Product", operationId, e);
            throw new ServiceException("Failed to delete product features", e);
        }
    }

    @Override
    public List<ProductFeature> findByProduct(Product product) {
        String operationId = UUID.randomUUID().toString();
        log.debug("Operation ID: {} - Finding ProductFeatures by Product ID: {}", 
            operationId, product != null ? product.getId() : null);
        
        try {
            if (product == null) {
                throw new ValidationException("Product cannot be null");
            }
            
            return featureRepository.findByProduct(product);
            
        } catch (ValidationException e) {
            log.error("Operation ID: {} - Validation error: {}", operationId, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Operation ID: {} - Unexpected error while finding ProductFeatures by Product", operationId, e);
            throw new ServiceException("Failed to retrieve product features", e);
        }
    }

    private void validateFeature(ProductFeature feature) {
        CategoryFeatureTemplate template = feature.getTemplate();
        Set<ProductFeatureValue> values = new HashSet<>(productFeatureValueRepository.findByFeature(feature));

        if (template == null) {
            throw new ValidationException("Feature template cannot be null");
        }

        if (template.isMandatory() && (values == null || values.isEmpty())) {
            throw new ValidationException(
                String.format("Feature '%s' is mandatory but no value provided", template.getCode()));
        }

        if (!template.isMultiValued() && values != null && values.size() > 1) {
            throw new ValidationException(
                String.format("Feature '%s' does not support multiple values", template.getCode()));
        }

        if (values != null) {
            for (ProductFeatureValue value : values) {
                if (!validationService.validateValue(value, template)) {
                    throw new ValidationException(
                        String.format("Invalid value for feature: %s", template.getCode()));
                }
            }
        }
    }
}
