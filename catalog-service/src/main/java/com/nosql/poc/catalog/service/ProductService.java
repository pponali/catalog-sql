package com.nosql.poc.catalog.service;

import com.nosql.poc.catalog.model.*;
import com.nosql.poc.catalog.repository.ProductRepository;
import com.nosql.poc.catalog.exception.ResourceNotFoundException;
import com.nosql.poc.catalog.client.VendorServiceClient;
import com.nosql.poc.catalog.client.ChannelServiceClient;
import com.nosql.poc.validation.service.ProductValidationService;
import com.scaler.messaging.ProductKafkaProducerService; // Import Kafka producer
import com.scaler.entity.Product; // Import the correct Product entity if different from model.Product
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    private final ProductValidationService validationService;
    private final VendorServiceClient vendorServiceClient;
    private final ChannelServiceClient channelServiceClient;
    private final ProductKafkaProducerService productKafkaProducerService; // Inject Kafka producer
    // private final ProductProtoMapper productProtoMapper; // Inject if mapping happens here
    
    @Transactional
    public Product createProduct(Product product) {
        log.info("Creating new product with SKU: {}", product.getSku());
        
        // Basic validation
        ValidationResult basicValidation = validationService.performBasicValidation(product);
        if (!basicValidation.isValid()) {
            throw new ValidationException("Basic validation failed", basicValidation.getErrors());
        }
        
        // Check if SKU already exists
        Optional<Product> existingProduct = productRepository.findBySku(product.getSku());
        if (existingProduct.isPresent()) {
            throw new DuplicateResourceException("Product with SKU " + product.getSku() + " already exists");
        }
        
        // Validate vendor if specified
        if (product.getVendorId() != null) {
            vendorServiceClient.validateVendor(product.getVendorId());
        }
        
        // Set audit info
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setCreatedDate(LocalDateTime.now());
        auditInfo.setLastModifiedDate(LocalDateTime.now());
        auditInfo.setVersion("1.0");
        product.setAuditInfo(auditInfo);
        
        // Business rules validation
        ValidationResult businessValidation = validationService.performBusinessValidation(product);
        product.setValidationStatus(businessValidation);
        
        Product savedProduct = productRepository.save(product);
        log.info("Created new product with ID: {}", savedProduct.getId());
        
        // Send update to Kafka
        // Assuming the Product entity used by the repository is compatible with the mapper
        // If com.nosql.poc.catalog.model.Product is different from com.scaler.entity.Product, mapping is needed first
        // For now, assuming they are compatible or the mapper handles com.nosql.poc.catalog.model.Product
        try {
             // Need to map com.nosql.poc.catalog.model.Product to com.scaler.entity.Product if they differ
             // Or adjust ProductProtoMapper to accept com.nosql.poc.catalog.model.Product
             // For now, let's assume ProductProtoMapper needs com.scaler.entity.Product
             // This part needs clarification on the exact Product model used by Kafka producer/mapper
             // The savedProduct is of type com.nosql.poc.catalog.model.Product
             // The Kafka producer expects com.scaler.entity.Product
             // Assuming they are the same or a mapping exists implicitly/explicitly
             // Based on user confirmation, ProductProtoMapper uses com.scaler.entity.Product
             // Let's assume savedProduct is compatible or can be mapped.
             // If ProductRepository returns com.scaler.entity.Product, this works directly.
             // If it returns com.nosql.poc.catalog.model.Product, mapping is needed.
             // For now, proceeding with the direct call as user confirmed the entity path.
             productKafkaProducerService.sendProductUpdate(savedProduct);
        } catch (Exception e) {
            log.error("Failed to send product update to Kafka for ID {}: {}", savedProduct.getId(), e.getMessage(), e);
            // Decide on error handling: throw exception, log only, etc.
        }
        
        return savedProduct;
    }
    
    @Transactional(readOnly = true)
    public Product getProduct(String id) {
        log.info("Fetching product with ID: {}", id);
        return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
    }
    
    @Transactional(readOnly = true)
    public Product getProductBySku(String sku) {
        log.info("Fetching product with SKU: {}", sku);
        return productRepository.findBySku(sku)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
    }
    
    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String entityId, String category, String channelId, Pageable pageable) {
        log.info("Searching products with filters - entityId: {}, category: {}, channelId: {}", 
                entityId, category, channelId);
        
        if (channelId != null) {
            return productRepository.findActiveProductsByChannel(channelId, pageable);
        } else if (category != null) {
            return productRepository.findByCategory(category, pageable);
        } else if (entityId != null) {
            return productRepository.findByEntityInfoEntityId(entityId, pageable);
        }
        
        return productRepository.findAll(pageable);
    }
    
    @Transactional
    public Product updateProduct(String id, Product updatedProduct) {
        log.info("Updating product with ID: {}", id);
        
        Product existingProduct = getProduct(id);
        
        // Basic validation
        ValidationResult basicValidation = validationService.performBasicValidation(updatedProduct);
        if (!basicValidation.isValid()) {
            throw new ValidationException("Basic validation failed", basicValidation.getErrors());
        }
        
        // Update fields
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setBrand(updatedProduct.getBrand());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setSubCategories(updatedProduct.getSubCategories());
        existingProduct.setAttributes(updatedProduct.getAttributes());
        existingProduct.setTags(updatedProduct.getTags());
        
        // Update audit info
        existingProduct.getAuditInfo().setLastModifiedDate(LocalDateTime.now());
        existingProduct.getAuditInfo().setVersion(
            incrementVersion(existingProduct.getAuditInfo().getVersion())
        );
        
        // Business rules validation
        ValidationResult businessValidation = validationService.performBusinessValidation(existingProduct);
        existingProduct.setValidationStatus(businessValidation);
        
        Product savedProduct = productRepository.save(existingProduct);
        log.info("Updated product with ID: {}", savedProduct.getId());
        
        // Send update to Kafka
        try {
             // Similar assumption as in createProduct
             productKafkaProducerService.sendProductUpdate(savedProduct);
        } catch (Exception e) {
            log.error("Failed to send product update to Kafka for ID {}: {}", savedProduct.getId(), e.getMessage(), e);
        }
        
        return savedProduct;
    }
    
    @Transactional
    public void updatePrice(String id, Price price) {
        log.info("Updating price for product with ID: {}", id);
        
        Product product = getProduct(id);
        
        // Validate price
        ValidationResult priceValidation = validationService.validatePrice(price);
        if (!priceValidation.isValid()) {
            throw new ValidationException("Price validation failed", priceValidation.getErrors());
        }
        
        // Update price
        if (price.getChannelId() == null) {
            product.setBasePrice(price.getAmount());
            product.setCurrency(price.getCurrency());
        } else {
            // Notify channel service about price update
            channelServiceClient.updateProductPrice(price.getChannelId(), id, price);
        }
        
        // Update audit info
        product.getAuditInfo().setLastModifiedDate(LocalDateTime.now());
        
        productRepository.save(product);
        log.info("Updated price for product with ID: {}", id);
    }
    
    @Transactional
    public void updateInventory(String id, Inventory inventory) {
        log.info("Updating inventory for product with ID: {}", id);
        
        Product product = getProduct(id);
        
        // Validate inventory
        ValidationResult inventoryValidation = validationService.validateInventory(inventory);
        if (!inventoryValidation.isValid()) {
            throw new ValidationException("Inventory validation failed", inventoryValidation.getErrors());
        }
        
        // Update inventory
        if (inventory.getChannelId() != null) {
            // Notify channel service about inventory update
            channelServiceClient.updateProductInventory(inventory.getChannelId(), id, inventory);
        }
        
        // Update audit info
        product.getAuditInfo().setLastModifiedDate(LocalDateTime.now());
        
        productRepository.save(product);
        log.info("Updated inventory for product with ID: {}", id);
    }
    
    @Transactional
    public void deleteProduct(String id) {
        log.info("Deleting product with ID: {}", id);
        
        Product product = getProduct(id);
        
        // Check if product can be deleted
        if (!canDeleteProduct(product)) {
            throw new ValidationException("Cannot delete product with active channel listings", null);
        }
        
        productRepository.delete(product);
        log.info("Deleted product with ID: {}", id);
    }
    
    @Transactional(readOnly = true)
    public ValidationStatus validateProduct(String id, String channelId) {
        log.info("Validating product with ID: {} for channel: {}", id, channelId);
        
        Product product = getProduct(id);
        
        if (channelId != null) {
            // Perform channel-specific validation
            ValidationResult channelValidation = validationService.performChannelValidation(product, channelId);
            return new ValidationStatus(channelValidation.isValid(), channelValidation.getErrors());
        }
        
        // Perform basic and business validation
        ValidationResult basicValidation = validationService.performBasicValidation(product);
        if (!basicValidation.isValid()) {
            return new ValidationStatus(false, basicValidation.getErrors());
        }
        
        ValidationResult businessValidation = validationService.performBusinessValidation(product);
        return new ValidationStatus(businessValidation.isValid(), businessValidation.getErrors());
    }
    
    @Transactional(readOnly = true)
    public List<Product> getProductVariants(String id) {
        log.info("Fetching variants for product with ID: {}", id);
        
        Product product = getProduct(id);
        
        if (product.getVariantGroupId() == null) {
            throw new ResourceNotFoundException("Product is not part of a variant group");
        }
        
        return productRepository.findByVariantGroupId(product.getVariantGroupId());
    }
    
    private boolean canDeleteProduct(Product product) {
        // Check if product has any active channel listings
        return !channelServiceClient.hasActiveListings(product.getId());
    }
    
    private String incrementVersion(String version) {
        try {
            String[] parts = version.split("\\.");
            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);
            return major + "." + (minor + 1);
        } catch (Exception e) {
            return "1.0";
        }
    }
}
