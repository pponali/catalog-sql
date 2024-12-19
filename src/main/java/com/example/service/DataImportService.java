package com.example.service;

import com.example.entity.*;
import com.example.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DataImportService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CategoryFeatureTemplateRepository templateRepository;
    private final ProductFeatureRepository featureRepository;

    @Transactional
    public void importSampleData() {
        // Create categories
        Category electronics = createCategory("ELEC", "Electronics", "Electronic devices and accessories");
        Category phones = createCategory("PHONE", "Phones", "Mobile phones and accessories");
        Category laptops = createCategory("LAPTOP", "Laptops", "Laptop computers");

        // Set up category hierarchy
        phones.addSuperCategory(electronics);
        laptops.addSuperCategory(electronics);

        // Create feature templates
        CategoryFeatureTemplate brandTemplate = createFeatureTemplate(electronics, "BRAND", "Brand", "string");
        CategoryFeatureTemplate modelTemplate = createFeatureTemplate(electronics, "MODEL", "Model", "string");
        CategoryFeatureTemplate priceTemplate = createFeatureTemplate(electronics, "PRICE", "Price", "numeric");
        
        priceTemplate.setMinValue(0.0);
        priceTemplate.setUnit("USD");
        templateRepository.save(priceTemplate);

        // Phone-specific templates
        CategoryFeatureTemplate storageTemplate = createFeatureTemplate(phones, "STORAGE", "Storage", "enum");
        storageTemplate.setAllowedValues("64GB,128GB,256GB,512GB");
        templateRepository.save(storageTemplate);

        CategoryFeatureTemplate colorTemplate = createFeatureTemplate(phones, "COLOR", "Color", "enum");
        colorTemplate.setAllowedValues("Black,White,Gold,Silver");
        templateRepository.save(colorTemplate);

        // Laptop-specific templates
        CategoryFeatureTemplate ramTemplate = createFeatureTemplate(laptops, "RAM", "RAM", "enum");
        ramTemplate.setAllowedValues("8GB,16GB,32GB,64GB");
        templateRepository.save(ramTemplate);

        CategoryFeatureTemplate processorTemplate = createFeatureTemplate(laptops, "CPU", "Processor", "string");
        templateRepository.save(processorTemplate);

        // Create products
        Product iphone = createProduct("IPH13", "iPhone 13", "Latest iPhone model", phones);
        addProductFeature(iphone, brandTemplate, "Apple");
        addProductFeature(iphone, modelTemplate, "iPhone 13");
        addProductFeature(iphone, priceTemplate, "999.99");
        addProductFeature(iphone, storageTemplate, "128GB");
        addProductFeature(iphone, colorTemplate, "Gold");

        Product macbook = createProduct("MAC13", "MacBook Pro 13", "Latest MacBook model", laptops);
        addProductFeature(macbook, brandTemplate, "Apple");
        addProductFeature(macbook, modelTemplate, "MacBook Pro 13");
        addProductFeature(macbook, priceTemplate, "1299.99");
        addProductFeature(macbook, ramTemplate, "16GB");
        addProductFeature(macbook, processorTemplate, "M1 Pro");
    }

    private Category createCategory(String code, String name, String description) {
        Category category = Category.builder()
            .code(code)
            .name(name)
            .description(description)
            .build();
        return categoryRepository.save(category);
    }

    private CategoryFeatureTemplate createFeatureTemplate(Category category, String code, String name, String attributeType) {
        CategoryFeatureTemplate template = CategoryFeatureTemplate.builder()
            .category(category)
            .code(code)
            .name(name)
            .attributeType(attributeType)
            .visible(true)
            .editable(true)
            .searchable(true)
            .comparable(true)
            .mandatory(true)
            .multiValued(false)
            .build();
        return templateRepository.save(template);
    }

    private Product createProduct(String code, String name, String description, Category category) {
        Product product = Product.builder()
            .code(code)
            .name(name)
            .description(description)
            .active(true)
            .build();
        product.addCategory(category);
        return productRepository.save(product);
    }

    private void addProductFeature(Product product, CategoryFeatureTemplate template, String value) {
        ProductFeature feature = ProductFeature.builder()
            .product(product)
            .classAttributeAssignment(template)
            .build();
        
        ProductFeatureValue featureValue = ProductFeatureValue.builder()
            .productFeature(feature)
            .value(value)
            .unit(template.getUnit())
            .build();
        
        feature.addValue(featureValue);
        featureRepository.save(feature);
    }
}
