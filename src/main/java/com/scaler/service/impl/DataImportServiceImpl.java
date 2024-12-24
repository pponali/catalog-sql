package com.scaler.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaler.dto.ImportResult;
import com.scaler.entity.Product;
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
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DataImportServiceImpl implements DataImportService {

    private final ProductRepository productRepository;
    private final UnitOfMeasureRepository unitRepository;
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
                        UnitOfMeasure unit = UnitOfMeasure.builder()
                                .code((String) unitData.get("code"))
                                .name((String) unitData.get("name"))
                                .description((String) unitData.get("description"))
                                .baseUnit((String) unitData.get("baseUnit"))
                                .conversionFactor(Double.valueOf(unitData.get("conversionFactor").toString()))
                                .build();
                        
                        unitRepository.save(unit);
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
                        Product product = Product.builder()
                                .name((String) productData.get("name"))
                                .description((String) productData.get("description"))
                                .sku((String) productData.get("sku"))
                                .status((String) productData.get("status"))
                                .category(categoryService.findByCode((String) productData.get("categoryCode")))
                                .build();
                        
                        productRepository.save(product);
                        successCount++;
                    } catch (Exception e) {
                        errors.add("Failed to import product: " + productData.get("sku") + " - " + e.getMessage());
                    }
                }
            }
            
            return ImportResult.builder()
                    .totalRecords(totalRecords)
                    .successCount(successCount)
                    .failureCount(totalRecords - successCount)
                    .errors(errors)
                    .build();
                    
        } catch (IOException e) {
            return ImportResult.builder()
                    .totalRecords(0)
                    .successCount(0)
                    .failureCount(0)
                    .errors(List.of("Failed to parse import file: " + e.getMessage()))
                    .build();
        }
    }
}
