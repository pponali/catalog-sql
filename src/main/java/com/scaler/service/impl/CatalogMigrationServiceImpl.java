package com.scaler.service.impl;

import com.scaler.dto.CatalogMigrationDTO;
import com.scaler.dto.MigrationRequestDTO;
import com.scaler.dto.MigrationResultDTO;
import com.scaler.entity.*;
import com.scaler.enums.BusinessUnit;
import com.scaler.enums.MigrationStatus;
import com.scaler.exception.BusinessException;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.repository.*;
import com.scaler.service.CatalogMigrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class CatalogMigrationServiceImpl implements CatalogMigrationService {
    @Override
    public MigrationResultDTO migrateCategories(UUID sourceCatalogId, UUID targetCatalogId, List<UUID> categoryIds) {
        return null;
    }

    @Override
    public MigrationResultDTO migrateProducts(UUID sourceCatalogId, UUID targetCatalogId, List<UUID> productIds) {
        return null;
    }

    @Override
    public MigrationResultDTO bulkMigrate(MigrationRequestDTO request) {
        return null;
    }

    @Override
    public boolean validateMigration(UUID sourceCatalogId, UUID targetCatalogId) {
        return false;
    }

    @Override
    public List<MigrationResultDTO> getMigrationHistory(UUID catalogId) {
        return List.of();
    }

    /*private final CatalogRepository catalogRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CatalogMigrationRepository migrationRepository;
    private final MerchantRepository merchantRepository;

    @Override
    @Transactional
    public MigrationResultDTO migrateCategories(UUID sourceCatalogId, UUID targetCatalogId, List<UUID> categoryIds) {
        validateCatalogs(sourceCatalogId, targetCatalogId);
        //validateBusinessRules(sourceCatalogId, targetCatalogId);
        
        CatalogMigration migration = new CatalogMigration();
        migration.setSourceCatalogId(sourceCatalogId);
        migration.setTargetCatalogId(targetCatalogId);
        migration.setStatus(MigrationStatus.IN_PROGRESS);
        
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        List<UUID> migratedIds = new ArrayList<>();
        List<UUID> failedIds = new ArrayList<>();
        
        for (UUID categoryId : categoryIds) {
            try {
                Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));
                
                if (!category.getCatalog().equals(sourceCatalogId)) {
                    warnings.add("Category " + categoryId + " does not belong to source catalog");
                    continue;
                }
                
                // Create new category in target catalog
                Category newCategory = new Category();
                newCategory.setId(UUID.randomUUID());
                newCategory.setName(category.getName());
                newCategory.setDescription(category.getDescription());
                newCategory.setCatalog(catalogRepository.findById(targetCatalogId).orElseThrow());
                newCategory.setMerchant(category.getMerchant());
                newCategory.setParent(category.getParent());
                
                categoryRepository.save(newCategory);
                migratedIds.add(categoryId);
                
            } catch (Exception e) {
                errors.add("Failed to migrate category " + categoryId + ": " + e.getMessage());
                failedIds.add(categoryId);
            }
        }
        
        migration.setMigratedCategoryIds(migratedIds);
        migration.setFailedCategoryIds(failedIds);
        migration.setCategoriesMigrated(migratedIds.size());
        migration.setWarnings(warnings);
        migration.setErrors(errors);
        migration.setStatus(errors.isEmpty() ? MigrationStatus.COMPLETED: MigrationStatus.COMPLETED_WITH_ERRORS);
        migration.setCompletedAt(LocalDateTime.now());
        
        migration = migrationRepository.save(migration);
        
        return createMigrationResult(migration);
    }

    @Override
    public MigrationResultDTO migrateProducts(UUID sourceCatalogId, UUID targetCatalogId, List<UUID> productIds) {
        return null;
    }

    @Override
    public MigrationResultDTO bulkMigrate(MigrationRequestDTO request) {
        return null;
    }

    @Override
    public boolean validateMigration(UUID sourceCatalogId, UUID targetCatalogId) {
        return false;
    }

    @Override
    @Transactional
    public MigrationResultDTO migrateProducts(UUID sourceCatalogId, UUID targetCatalogId, List<UUID> productIds) {
        validateCatalogs(sourceCatalogId, targetCatalogId);
        validateBusinessRules(sourceCatalogId, targetCatalogId);
        
        CatalogMigration migration = new CatalogMigration();
        migration.setSourceCatalogId(sourceCatalogId);
        migration.setTargetCatalogId(targetCatalogId);
        migration.setStatus(MigrationStatus.IN_PROGRESS);
        
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        List<UUID> migratedIds = new ArrayList<>();
        List<UUID> failedIds = new ArrayList<>();
        
        for (UUID productId : productIds) {
            try {
                Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + productId));
                
                if (!product.getCatalog().equals(sourceCatalogId)) {
                    warnings.add("Product " + productId + " does not belong to source catalog");
                    continue;
                }
                
                // Create new product in target catalog
                Product newProduct = new Product();
                newProduct.setId(UUID.randomUUID());
                newProduct.setName(product.getName());
                newProduct.setDescription(product.getDescription());
                newProduct.setCatalog(catalogRepository.findById(targetCatalogId).orElseThrow(
                        () -> new ResourceNotFoundException("Target catalog not found: " + targetCatalogId)
                ));
                newProduct.setMerchant(product.getMerchant());
                newProduct.setPrice(product.getPrice());
                newProduct.setSku(product.getSku());
                
                productRepository.save(newProduct);
                migratedIds.add(productId);
                
            } catch (Exception e) {
                errors.add("Failed to migrate product " + productId + ": " + e.getMessage());
                failedIds.add(productId);
            }
        }
        
        migration.setMigratedProductIds(migratedIds);
        migration.setFailedProductIds(failedIds);
        migration.setProductsMigrated(migratedIds.size());
        migration.setWarnings(warnings);
        migration.setErrors(errors);
        migration.setStatus(errors.isEmpty() ? MigrationStatus.COMPLETED : MigrationStatus.COMPLETED_WITH_ERRORS);
        migration.setCompletedAt(LocalDateTime.now());
        
        migration = migrationRepository.save(migration);
        
        return createMigrationResult(migration);
    }

    @Override
    @Transactional
    public MigrationResultDTO bulkMigrate(MigrationRequestDTO request) {
        validateCatalogs(request.getSourceCatalogId(), request.getTargetCatalogId());
        validateBusinessRules(request.getSourceCatalogId(), request.getTargetCatalogId());
        
        CatalogMigration migration = new CatalogMigration();
        migration.setSourceCatalogId(request.getSourceCatalogId());
        migration.setTargetCatalogId(request.getTargetCatalogId());
        migration.setStatus(MigrationStatus.IN_PROGRESS);
        migration.setMigrationNotes(request.getMigrationNotes());
        
        MigrationResultDTO categoryResult = null;
        MigrationResultDTO productResult = null;
        
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            categoryResult = migrateCategories(
                request.getSourceCatalogId(), 
                request.getTargetCatalogId(), 
                request.getCategoryIds()
            );
        }
        
        if (request.getProductIds() != null && !request.getProductIds().isEmpty()) {
            productResult = migrateProducts(
                request.getSourceCatalogId(), 
                request.getTargetCatalogId(), 
                request.getProductIds()
            );
        }
        
        // Combine results
        migration.setCategoriesMigrated(categoryResult != null ? categoryResult.getCategoriesMigrated() : 0);
        migration.setProductsMigrated(productResult != null ? productResult.getProductsMigrated() : 0);
        
        List<String> allWarnings = new ArrayList<>();
        List<String> allErrors = new ArrayList<>();
        
        if (categoryResult != null) {
            allWarnings.addAll(categoryResult.getWarnings());
            allErrors.addAll(categoryResult.getErrors());
        }
        if (productResult != null) {
            allWarnings.addAll(productResult.getWarnings());
            allErrors.addAll(productResult.getErrors());
        }
        
        migration.setWarnings(allWarnings);
        migration.setErrors(allErrors);
        migration.setStatus(allErrors.isEmpty() ? MigrationStatus.COMPLETED : MigrationStatus.COMPLETED_WITH_ERRORS);
        migration.setCompletedAt(LocalDateTime.now());
        
        migration = migrationRepository.save(migration);
        
        return createMigrationResult(migration);
    }

    @Override
    public boolean validateMigration(UUID sourceCatalogId, UUID targetCatalogId) {
        try {
            validateCatalogs(sourceCatalogId, targetCatalogId);
            validateBusinessRules(sourceCatalogId, targetCatalogId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<MigrationResultDTO> getMigrationHistory(UUID catalogId) {
        List<CatalogMigration> migrations = migrationRepository.findBySourceCatalogIdOrTargetCatalogId(
            catalogId, catalogId);
        
        return migrations.stream()
            .map(this::createMigrationResult)
            .collect(Collectors.toList());
    }

    private void validateCatalogs(UUID sourceCatalogId, UUID targetCatalogId) {
        Catalog sourceCatalog = catalogRepository.findById(sourceCatalogId)
            .orElseThrow(() -> new ResourceNotFoundException("Source catalog not found: " + sourceCatalogId));
            
        Catalog targetCatalog = catalogRepository.findById(targetCatalogId)
            .orElseThrow(() -> new ResourceNotFoundException("Target catalog not found: " + targetCatalogId));
            
        if (sourceCatalog.equals(targetCatalog)) {
            throw new BusinessException("Cannot migrate to the same catalog");
        }
    }

    private void validateBusinessRules(UUID sourceCatalogId, UUID targetCatalogId) {
        Catalog sourceCatalog = catalogRepository.findById(sourceCatalogId)
            .orElseThrow(() -> new ResourceNotFoundException("Source catalog not found: " + sourceCatalogId));
        Catalog targetCatalog = catalogRepository.findById(targetCatalogId)
            .orElseThrow(() -> new ResourceNotFoundException("Target catalog not found: " + targetCatalogId));

        Merchant sourceMerchant = merchantRepository.findById(sourceCatalog.getMerchant().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Source Merchant not found"));
        Merchant targetMerchant = merchantRepository.findById(targetCatalog.getMerchant().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Target Merchant not found"));

        // Rule 1: Tata CLiQ Fashion can only migrate to Tata Digital
        if (isBusinessUnit(sourceBusiness, BusinessUnit.TATA_CLIQ_FASHION) &&
            !isBusinessUnit(targetMerchant, BusinessUnit.TATA_DIGITAL)) {
            throw new BusinessException("Tata CLiQ Fashion can only migrate to Tata Digital");
        }

        // Rule 2: BigBasket can only migrate to Tata Digital
        if (isBusinessUnit(sourceBusiness, BusinessUnit.BIGBASKET) &&
            !isBusinessUnit(targetMerchant, BusinessUnit.TATA_DIGITAL)) {
            throw new BusinessException("BigBasket can only migrate to Tata Digital");
        }

        // Rule 3: Tata 1mg can only migrate to Tata Digital
        if (isBusinessUnit(sourceBusiness, BusinessUnit.TATA_1MG) &&
            !isBusinessUnit(targetMerchant, BusinessUnit.TATA_DIGITAL)) {
            throw new BusinessException("Tata 1mg can only migrate to Tata Digital");
        }

        // Rule 4: Tanishq can only migrate to Tata Digital
        if (isBusinessUnit(sourceBusiness, BusinessUnit.TANISHQ) &&
            !isBusinessUnit(targetMerchant, BusinessUnit.TATA_DIGITAL)) {
            throw new BusinessException("Tanishq can only migrate to Tata Digital");
        }

        // Rule 5: Tata Digital can migrate to any business
        if (isBusinessUnit(sourceBusiness, BusinessUnit.TATA_DIGITAL)) {
            // No restrictions needed as Tata Digital can migrate to any business
            return;
        }

        // Additional validation for specific product types
        validateProductTypeRules(sourceCatalog, targetCatalog);
    }

    private void validateProductTypeRules(Catalog sourceCatalog, Catalog targetCatalog) {
        // Rule 1: Fashion products require size chart mapping
        if (isFashionCatalog(sourceCatalog) && !isFashionCatalog(targetCatalog)) {
            validateSizeChartMapping(sourceCatalog, targetCatalog);
        }

        // Rule 2: Food products require nutritional information
        if (isFoodCatalog(sourceCatalog) && !isFoodCatalog(targetCatalog)) {
            validateNutritionalInfo(sourceCatalog, targetCatalog);
        }

        // Rule 3: Healthcare products require medical information
        if (isHealthcareCatalog(sourceCatalog) && !isHealthcareCatalog(targetCatalog)) {
            validateMedicalInfo(sourceCatalog, targetCatalog);
        }

        // Rule 4: Jewelry products require certification
        if (isJewelryCatalog(sourceCatalog) && !isJewelryCatalog(targetCatalog)) {
            validateJewelryCertification(sourceCatalog, targetCatalog);
        }
    }

    private boolean isBusinessUnit(Merchant business, BusinessUnit unit) {
        return merchant.getCode().equals(unit.name());
    }

    private boolean isFashionCatalog(Catalog catalog) {
        return catalog.getMerchant().equals(merchantRepository.findByName(BusinessUnit.TATA_CLIQ_FASHION.getDisplayName()));
    }

    private boolean isFoodCatalog(Catalog catalog) {
        return catalog.getMerchant().equals(merchantRepository.findByName(BusinessUnit.BIGBASKET.getDisplayName()));
    }

    private boolean isHealthcareCatalog(Catalog catalog) {
        return catalog.getMerchant().equals(merchantRepository.findByName(BusinessUnit.TATA_1MG.getDisplayName()));
    }

    private boolean isJewelryCatalog(Catalog catalog) {
        return catalog.getMerchant().equals(merchantRepository.findByName(BusinessUnit.TANISHQ.getDisplayName()));
    }

    private void validateSizeChartMapping(Catalog sourceCatalog, Catalog targetCatalog) {
        List<Product> products = productRepository.findByCatalogId(sourceCatalog.getId());
        
        for (Product product : products) {
            List<ProductAttribute> attributes = product.getAttributes();
            boolean hasSizeAttribute = attributes.stream()
                .anyMatch(attr -> attr.getAttributeName().equalsIgnoreCase("SIZE") ||
                                attr.getAttributeName().equalsIgnoreCase("SIZE_CHART"));
            
            if (!hasSizeAttribute) {
                throw new BusinessException("Fashion product " + product.getSku() + 
                    " missing required size chart information for migration");
            }
            
            // Validate size chart format
            Optional<ProductAttribute> sizeChart = attributes.stream()
                .filter(attr -> attr.getAttributeName().equalsIgnoreCase("SIZE_CHART"))
                .findFirst();
                
            if (sizeChart.isPresent()) {
                validateSizeChartFormat(sizeChart.get().getAttributeValue());
            }
        }
    }

    private void validateNutritionalInfo(Catalog sourceCatalog, Catalog targetCatalog) {
        List<Product> products = productRepository.findByCatalogId(sourceCatalog.getId());
        
        for (Product product : products) {
            List<ProductAttribute> attributes = product.getAttributes();
            boolean hasNutritionalInfo = attributes.stream()
                .anyMatch(attr -> attr.getAttributeName().equalsIgnoreCase("NUTRITIONAL_INFO") ||
                                attr.getAttributeName().equalsIgnoreCase("NUTRITION_FACTS"));
            
            if (!hasNutritionalInfo) {
                throw new BusinessException("Food product " + product.getSku() + 
                    " missing required nutritional information for migration");
            }
            
            // Validate nutritional info format
            validateRequiredNutritionalFields(attributes);
        }
    }

    private void validateMedicalInfo(Catalog sourceCatalog, Catalog targetCatalog) {
        List<Product> products = productRepository.findByCatalogId(sourceCatalog.getId());
        
        for (Product product : products) {
            List<ProductAttribute> attributes = product.getAttributes();
            
            // Check for required medical attributes
            List<String> requiredAttributes = Arrays.asList(
                "COMPOSITION", "DOSAGE", "SIDE_EFFECTS", "PRECAUTIONS"
            );
            
            List<String> missingAttributes = requiredAttributes.stream()
                .filter(required -> attributes.stream()
                    .noneMatch(attr -> attr.getAttributeName().equalsIgnoreCase(required)))
                .collect(Collectors.toList());
            
            if (!missingAttributes.isEmpty()) {
                throw new BusinessException("Healthcare product " + product.getSku() + 
                    " missing required medical information: " + String.join(", ", missingAttributes));
            }
            
            // Validate medical info format
            validateMedicalInfoFormat(attributes);
        }
    }

    private void validateJewelryCertification(Catalog sourceCatalog, Catalog targetCatalog) {
        List<Product> products = productRepository.findByCatalogId(sourceCatalog.getId());
        
        for (Product product : products) {
            List<ProductAttribute> attributes = product.getAttributes();
            
            // Check for required jewelry attributes
            List<String> requiredAttributes = Arrays.asList(
                "METAL_PURITY", "CERTIFICATION_TYPE", "HALLMARK"
            );
            
            List<String> missingAttributes = requiredAttributes.stream()
                .filter(required -> attributes.stream()
                    .noneMatch(attr -> attr.getAttributeName().equalsIgnoreCase(required)))
                .collect(Collectors.toList());
            
            if (!missingAttributes.isEmpty()) {
                throw new BusinessException("Jewelry product " + product.getSku() + 
                    " missing required certification information: " + String.join(", ", missingAttributes));
            }
            
            // Validate certification format
            validateJewelryCertificationFormat(attributes);
        }
    }

    private void validateSizeChartFormat(String sizeChart) {
        try {
            // Expecting JSON format for size chart
            ObjectMapper mapper = new ObjectMapper();
            JsonNode sizeChartNode = mapper.readTree(sizeChart);
            
            // Validate required size chart fields
            if (!sizeChartNode.has("measurements") || !sizeChartNode.has("size_guide")) {
                throw new BusinessException("Invalid size chart format: missing required fields");
            }
        } catch (Exception e) {
            throw new BusinessException("Invalid size chart format: " + e.getMessage());
        }
    }

    private void validateRequiredNutritionalFields(List<ProductAttribute> attributes) {
        List<String> requiredFields = Arrays.asList(
            "CALORIES", "PROTEIN", "CARBOHYDRATES", "FAT", "SERVING_SIZE"
        );
        
        List<String> missingFields = requiredFields.stream()
            .filter(field -> attributes.stream()
                .noneMatch(attr -> attr.getAttributeName().equalsIgnoreCase(field)))
            .collect(Collectors.toList());
        
        if (!missingFields.isEmpty()) {
            throw new BusinessException("Missing required nutritional fields: " + 
                String.join(", ", missingFields));
        }
    }

    private void validateMedicalInfoFormat(List<ProductAttribute> attributes) {
        attributes.stream()
            .filter(attr -> attr.getAttributeName().equalsIgnoreCase("DOSAGE"))
            .findFirst()
            .ifPresent(dosage -> {
                if (!dosage.getAttributeValue().matches("^[0-9]+\\s*(mg|ml|g)\\s*(per|every)\\s*[0-9]+\\s*(hours|days)$")) {
                    throw new BusinessException("Invalid dosage format");
                }
            });
    }

    private void validateJewelryCertificationFormat(List<ProductAttribute> attributes) {
        attributes.stream()
            .filter(attr -> attr.getAttributeName().equalsIgnoreCase("METAL_PURITY"))
            .findFirst()
            .ifPresent(purity -> {
                if (!purity.getAttributeValue().matches("^[0-9]{1,2}K$|^[0-9]{3}$")) {
                    throw new BusinessException("Invalid metal purity format");
                }
            });
    }

    private MigrationResultDTO createMigrationResult(CatalogMigration migration) {
        MigrationResultDTO result = new MigrationResultDTO();
        result.setMigrationId(migration.getId());
        result.setSourceCatalogId(migration.getSourceCatalogId());
        result.setTargetCatalogId(migration.getTargetCatalogId());
        result.setMigrationTime(migration.getMigrationTime());
        result.setStatus(migration.getStatus());
        result.setMessage(migration.getMessage());
        result.setCategoriesMigrated(migration.getCategoriesMigrated());
        result.setProductsMigrated(migration.getProductsMigrated());
        result.setWarnings(migration.getWarnings());
        result.setErrors(migration.getErrors());
        result.setMigratedCategoryIds(migration.getMigratedCategoryIds());
        result.setMigratedProductIds(migration.getMigratedProductIds());
        result.setFailedCategoryIds(migration.getFailedCategoryIds());
        result.setFailedProductIds(migration.getFailedProductIds());
        result.setMigrationNotes(migration.getMigrationNotes());
        result.setCompletedAt(migration.getCompletedAt());
        result.setPerformedBy(migration.getPerformedBy());
        return result;
    }

    public boolean migrateCatalog(CatalogMigrationDTO request) {
        Catalog sourceCatalog = catalogRepository.findById(request.getSourceCatalogId())
            .orElseThrow(() -> new ResourceNotFoundException("Source catalog not found"));
        Catalog targetCatalog = catalogRepository.findById(request.getTargetCatalogId())
            .orElseThrow(() -> new ResourceNotFoundException("Target catalog not found"));

        //validateBusinessRules(sourceCatalog.getId(), targetCatalog.getId());

        CatalogMigration migration = new CatalogMigration();
        migration.setSourceCatalogId(sourceCatalog.getId());
        migration.setTargetCatalogId(targetCatalog.getId());
        migration.setMigrationTime(LocalDateTime.now());
        migration.setStatus(MigrationStatus.IN_PROGRESS);
        migration.setCategoriesMigrated(0);
        migration.setProductsMigrated(0);
        migration.setWarnings(new ArrayList<>());
        migration.setErrors(new ArrayList<>());
        migration.setMigratedCategoryIds(new ArrayList<>());
        migration.setMigratedProductIds(new ArrayList<>());
        migration.setFailedCategoryIds(new ArrayList<>());
        migration.setFailedProductIds(new ArrayList<>());
        migration.setMigrationNotes(request.getMigrationNotes());

        migration = migrationRepository.save(migration);

        try {
            migrateCategories(sourceCatalog.getId(), targetCatalog.getId(), Collections.emptyList());
            migrateProducts(sourceCatalog.getId(), targetCatalog.getId(), Collections.emptyList());
            migration.setStatus(MigrationStatus.COMPLETED);
        } catch (Exception e) {
            migration.setStatus(MigrationStatus.FAILED);
            migration.getErrors().add(e.getMessage());
        }

        migration.setCompletedAt(LocalDateTime.now());
        migrationRepository.save(migration);

        return migration.getStatus() == MigrationStatus.COMPLETED;
    }*/

}
