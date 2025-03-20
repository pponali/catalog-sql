package com.scaler.service.impl;

import com.scaler.dto.ProductDTO;
import com.scaler.dto.ValidationResultDTO;
import com.scaler.entity.Product;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.exception.ServiceException;
import com.scaler.exception.ValidationException;
import com.scaler.repository.CategoryRepository;
import com.scaler.repository.ProductCategoryRepository;
import com.scaler.repository.ProductRepository;
import com.scaler.repository.specification.ProductSpecification;
// import com.scaler.service.ProductMappingService; // Removed as part of microservices separation
import com.scaler.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the ProductService interface
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    // private final ProductMappingService productMappingService; // Removed as part of microservices separation
    private final com.scaler.grpc.client.ValidationServiceClient validationServiceClient;
    private final com.scaler.grpc.client.VendorServiceClient vendorServiceClient;
    private final com.scaler.grpc.client.RulesServiceClient rulesServiceClient;
    private final com.scaler.grpc.client.ChannelServiceClient channelServiceClient;
    private final com.scaler.grpc.client.PartnerServiceClient partnerServiceClient;

    // New method implementations for gRPC service
    
    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) {
        log.info("Creating new product: {}", productDTO.getName());
        
        // Generate a new ID if not provided
        if (productDTO.getId() == null) {
            productDTO.setId(UUID.randomUUID());
        }
        
        // Verify merchant exists if merchantId is provided
        if (productDTO.getMerchantId() != null) {
            try {
                log.debug("Verifying merchant with ID: {}", productDTO.getMerchantId());
                vendorServiceClient.getMerchant(productDTO.getMerchantId());
            } catch (ResourceNotFoundException e) {
                log.warn("Merchant not found: {}", e.getMessage());
                throw new ValidationException("Invalid merchant ID: " + productDTO.getMerchantId());
            }
        }
        
        // Validate categories
        validateCategories(productDTO);
        
        // Validate the product using the validation service
        log.debug("Validating product before creation");
        ValidationResultDTO validationResult = validationServiceClient.validateProduct(productDTO);
        
        // If validation fails, throw an exception
        if (!validationResult.isValid()) {
            String errorMessage = "Product validation failed: " + 
                    String.join(", ", validationResult.getErrors());
            log.warn("Product validation failed: {}", errorMessage);
            throw new ValidationException(errorMessage);
        }
        
        // Evaluate product against business rules
        try {
            log.debug("Evaluating product against business rules");
            com.scaler.grpc.rules.RuleEvaluationResponse ruleEvaluation = 
                    rulesServiceClient.evaluateProductRule(productDTO, "PRODUCT_CREATION");
            
            if (!ruleEvaluation.getResult()) {
                String errorMessage = "Product rule evaluation failed: " + 
                        String.join(", ", ruleEvaluation.getMessagesList());
                log.warn("Product rule evaluation failed: {}", errorMessage);
                throw new ValidationException(errorMessage);
            }
            
            log.info("Product passed rule evaluation: rules applied: {}", 
                    String.join(", ", ruleEvaluation.getAppliedRulesList()));
        } catch (ServiceException e) {
            // Don't fail if rules service is unavailable, just log a warning
            log.warn("Could not evaluate product rules: {}", e.getMessage());
        }
        
        // Convert to entity and save - direct conversion temporarily for microservices transition
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setCode(productDTO.getCode());
        // Other properties would be set here
        
        log.info("Product validated successfully, saving to database");
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateProduct(UUID id, ProductDTO productDTO) {
        log.info("Updating product with ID: {}", id);
        
        // Check if product exists
        if (!productRepository.existsById(id)) {
            log.warn("Product not found with id: {}", id);
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        
        // Ensure ID is set correctly
        productDTO.setId(id);
        
        // Verify merchant exists if merchantId is provided
        if (productDTO.getMerchantId() != null) {
            try {
                log.debug("Verifying merchant with ID: {}", productDTO.getMerchantId());
                vendorServiceClient.getMerchant(productDTO.getMerchantId());
            } catch (ResourceNotFoundException e) {
                log.warn("Merchant not found: {}", e.getMessage());
                throw new ValidationException("Invalid merchant ID: " + productDTO.getMerchantId());
            }
        }
        
        // Validate categories
        validateCategories(productDTO);
        
        // Validate the product using the validation service
        log.debug("Validating product before update");
        ValidationResultDTO validationResult = validationServiceClient.validateProduct(productDTO);
        
        // If validation fails, throw an exception
        if (!validationResult.isValid()) {
            String errorMessage = "Product validation failed: " + 
                    String.join(", ", validationResult.getErrors());
            log.warn("Product validation failed: {}", errorMessage);
            throw new ValidationException(errorMessage);
        }
        
        // Evaluate product against business rules
        try {
            log.debug("Evaluating product against business rules");
            com.scaler.grpc.rules.RuleEvaluationResponse ruleEvaluation = 
                    rulesServiceClient.evaluateProductRule(productDTO, "PRODUCT_UPDATE");
            
            if (!ruleEvaluation.getResult()) {
                String errorMessage = "Product rule evaluation failed: " + 
                        String.join(", ", ruleEvaluation.getMessagesList());
                log.warn("Product rule evaluation failed: {}", errorMessage);
                throw new ValidationException(errorMessage);
            }
            
            log.info("Product passed rule evaluation: rules applied: {}", 
                    String.join(", ", ruleEvaluation.getAppliedRulesList()));
        } catch (ServiceException e) {
            // Don't fail if rules service is unavailable, just log a warning
            log.warn("Could not evaluate product rules: {}", e.getMessage());
        }
        
        // Convert to entity and save - direct conversion temporarily for microservices transition
        Product updatedProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        
        updatedProduct.setName(productDTO.getName());
        updatedProduct.setDescription(productDTO.getDescription());
        updatedProduct.setCode(productDTO.getCode());
        // Other properties would be set here
        
        log.info("Product validated successfully, updating in database");
        return productRepository.save(updatedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(UUID id) {
        log.info("Fetching product with ID: {}", id);
        
        // Get product from database
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        
        // Seller information enrichment removed as part of microservices separation
        
        return product;
    }
    
    // Seller information enrichment removed as part of microservices separation
    
    /**
     * Validates that the categories associated with a product exist
     * 
     * @param productDTO The product DTO containing categories to validate
     * @throws ValidationException if category validation fails
     */
    private void validateCategories(ProductDTO productDTO) {
        if (productDTO.getCategoryIds() == null || productDTO.getCategoryIds().isEmpty()) {
            log.debug("No categories to validate for product");
            return;
        }
        
        log.debug("Validating {} categories for product", productDTO.getCategoryIds().size());
        
        for (UUID categoryId : productDTO.getCategoryIds()) {
            try {
                // Try to validate using the partner service
                if (!partnerServiceClient.validateCategoryExists(categoryId)) {
                    throw new ValidationException("Invalid category ID: " + categoryId);
                }
                log.debug("Category validated successfully: {}", categoryId);
            } catch (ResourceNotFoundException e) {
                log.warn("Category not found: {}", e.getMessage());
                throw new ValidationException("Invalid category ID: " + categoryId);
            } catch (ServiceException e) {
                // Fall back to local repository if partner service is unavailable
                log.warn("Partner service unavailable for category validation, falling back to local check: {}", e.getMessage());
                if (!categoryRepository.existsById(categoryId)) {
                    throw new ValidationException("Invalid category ID: " + categoryId);
                }
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(UUID categoryId) {
        log.info("Fetching products for category with ID: {}", categoryId);
        
        // Check if category exists - try the partner service first, fall back to local repository
        try {
            partnerServiceClient.validateCategoryExists(categoryId);
        } catch (Exception e) {
            log.warn("Partner service lookup failed, falling back to local category check: {}", e.getMessage());
            if (!categoryRepository.existsById(categoryId)) {
                throw new ResourceNotFoundException("Category not found with id: " + categoryId);
            }
        }
        
        // Find products by category
        List<Product> products = productRepository.findByCategoriesContaining(categoryId);
        
        // Seller information enrichment removed as part of microservices separation
        
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> searchProducts(String query, List<UUID> categoryIds, List<String> filters, int page, int size) {
        log.info("Searching products with query: {}, categories: {}, filters: {}, page: {}, size: {}", 
                query, categoryIds, filters, page, size);
        
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("updatedAt").descending());
        
        // Parse filters
        Map<String, String> filterMap = new HashMap<>();
        if (filters != null && !filters.isEmpty()) {
            for (String filter : filters) {
                String[] parts = filter.split(":");
                if (parts.length == 2) {
                    filterMap.put(parts[0], parts[1]);
                }
            }
        }
        
        // Build specification
        Specification<Product> spec = Specification.where(null);
        
        // Add search query
        if (query != null && !query.trim().isEmpty()) {
            spec = spec.and(ProductSpecification.nameLike(query)
                    .or(ProductSpecification.descriptionLike(query))
                    .or(ProductSpecification.skuLike(query)));
        }
        
        // Add category filters
        if (categoryIds != null && !categoryIds.isEmpty()) {
            spec = spec.and(ProductSpecification.inCategories(categoryIds));
        }
        
        // Add other filters
        for (Map.Entry<String, String> entry : filterMap.entrySet()) {
            switch (entry.getKey()) {
                case "price_min":
                    spec = spec.and(ProductSpecification.priceGreaterThanOrEqual(Double.parseDouble(entry.getValue())));
                    break;
                case "price_max":
                    spec = spec.and(ProductSpecification.priceLessThanOrEqual(Double.parseDouble(entry.getValue())));
                    break;
                case "brand":
                    spec = spec.and(ProductSpecification.brandEquals(entry.getValue()));
                    break;
                // Add more filters as needed
            }
        }
        
        // Execute search
        Page<Product> productPage = productRepository.findAll(spec, pageable);
        List<Product> products = productPage.getContent();
        
        // Seller information enrichment removed as part of microservices separation
        
        return products;
    }

    @Override
    @Transactional
    public boolean deleteProduct(UUID id) {
        log.info("Deleting product with ID: {}", id);
        
        if (!productRepository.existsById(id)) {
            log.warn("Product not found with ID: {}", id);
            return false;
        }
        
        try {
            productRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error("Error deleting product with ID: {}", id, e);
            return false;
        }
    }

    // Legacy method implementations
    
    @Override
    @Transactional
    public ProductDTO create(ProductDTO productDTO) {
        // Implementation simplified for microservices transition
        // Convert DTO to entity, create product, then convert back to DTO
        Product createdProduct = createProduct(productDTO);
        // Simplified implementation - real implementation would use a proper mapper
        ProductDTO result = new ProductDTO();
        result.setId(createdProduct.getId());
        result.setName(createdProduct.getName());
        result.setCode(createdProduct.getCode());
        result.setDescription(createdProduct.getDescription());
        return result;
    }

    @Override
    @Transactional
    public ProductDTO update(UUID id, ProductDTO productDTO) {
        // Implementation simplified for microservices transition
        // Similar to create but updates existing product
        Product updatedProduct = updateProduct(id, productDTO);
        // Simplified implementation - real implementation would use a proper mapper
        ProductDTO result = new ProductDTO();
        result.setId(updatedProduct.getId());
        result.setName(updatedProduct.getName());
        result.setCode(updatedProduct.getCode());
        result.setDescription(updatedProduct.getDescription());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findById(UUID id) {
        try {
            Product product = getProductById(id);
            // Simplified implementation - real implementation would use a proper mapper
            ProductDTO dto = new ProductDTO();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setCode(product.getCode());
            dto.setDescription(product.getDescription());
            return Optional.of(dto);
        } catch (ResourceNotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        log.info("Finding all products (legacy method)");
        // Simplified implementation - query all products and convert to DTOs
        List<Product> products = productRepository.findAll();
        
        // Simplified mapper - real implementation would use a proper mapper
        List<ProductDTO> result = new ArrayList<>();
        for (Product product : products) {
            ProductDTO dto = new ProductDTO();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setCode(product.getCode());
            dto.setDescription(product.getDescription());
            result.add(dto);
        }
        
        return result;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return productRepository.existsById(id);
    }
}
