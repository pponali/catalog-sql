package com.scaler.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.dto.ImportResult;
import com.scaler.entity.Product;
import com.scaler.entity.ProductCategory;
import com.scaler.entity.UnitOfMeasure;
import com.scaler.repository.ProductRepository;
import com.scaler.repository.UnitOfMeasureRepository;
import com.scaler.service.CategoryService;
import com.scaler.service.DataImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DataImportServiceImpl implements DataImportService {

    private final ProductRepository productRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;
    private final CategoryService categoryService;
    private final ObjectMapper objectMapper;

    @Override
    public ImportResult importData(MultipartFile file) {
        try {
            String content = new String(file.getBytes());
            Map<String, Object> data = objectMapper.readValue(content, Map.class);
            
            List<String> errors = new ArrayList<>();
            int totalRecords = 0;
            int successCount = 0;
            
            // Import units
            if (data.containsKey("units")) {
                List<Map<String, Object>> units = (List<Map<String, Object>>) data.get("units");
                totalRecords += units.size();
                
                for (Map<String, Object> unitData : units) {
                    try {
                        UnitOfMeasure unit = buildUnit(unitData);
                        unitOfMeasureRepository.save(unit);
                        successCount++;
                    } catch (Exception e) {
                        errors.add("Failed to import unit: " + unitData.get("code") + " - " + e.getMessage());
                    }
                }
            }
            
            // Import products
            if (data.containsKey("products")) {
                List<Map<String, Object>> products = (List<Map<String, Object>>) data.get("products");
                totalRecords += products.size();
                
                for (Map<String, Object> productData : products) {
                    try {
                        Product product = new Product();
                        product.setName((String) productData.get("name"));
                        product.setDescription((String) productData.get("description"));
                        product.setSku((String) productData.get("sku"));
                        product.setStatus((String) productData.get("status"));
                        // Create ProductCategory relationship
                        ProductCategory productCategory = ProductCategory.builder()
                            .product(product)
                            .category(categoryService.findByCode((String) productData.get("categoryCode")))
                            .isPrimary(true)
                            .displayOrder(0)
                            .build();
                        product.setProductCategories(Collections.singleton(productCategory));
                        
                        if (productData.get("unitOfMeasure") != null) {
                            UnitOfMeasure unitOfMeasure = unitOfMeasureRepository.findById(UUID.fromString((String) productData.get("unitOfMeasure"))).orElse(null);
                            product.setUnitOfMeasure(unitOfMeasure);
                        }
                        
                        productRepository.save(product);
                        successCount++;
                    } catch (Exception e) {
                        errors.add("Failed to import product: " + productData.get("sku") + " - " + e.getMessage());
                    }
                }
            }
            
            ImportResult result = new ImportResult();
            result.setTotalRecords(totalRecords);
            result.setSuccessCount(successCount);
            result.setFailureCount(totalRecords - successCount);
            result.setErrors(errors);
            return result;
                    
        } catch (IOException e) {
            ImportResult result = new ImportResult();
            result.setTotalRecords(0);
            result.setSuccessCount(0);
            result.setFailureCount(0);
            result.setErrors(List.of("Failed to parse import file: " + e.getMessage()));
            return result;
        }
    }
    
    private UnitOfMeasure buildUnit(Map<String, Object> unitData) {
        return UnitOfMeasure.builder()
                .id(UUID.fromString((String) unitData.get("id")))
                .code((String) unitData.get("code"))
                .name((String) unitData.get("name"))
                .description((String) unitData.get("description"))
                .type((String) unitData.get("type"))
                .displaySymbol((String) unitData.get("displaySymbol"))
                .active(Boolean.TRUE.equals(unitData.get("active")))
                .metadata(objectMapper.valueToTree(unitData.get("metadata")))
                .conversionFactor(Double.valueOf(unitData.get("conversionFactor").toString()))
                .build();
    }
}
