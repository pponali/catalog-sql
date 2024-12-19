package com.example.service.impl;

import com.example.entity.*;
import com.example.repository.*;
import com.example.service.DataImportService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DataImportServiceImpl implements DataImportService {
    private final CategoryRepository categoryRepository;
    private final CategoryFeatureTemplateRepository templateRepository;
    private final ProductRepository productRepository;
    private final ProductFeatureRepository featureRepository;

    @Override
    public void importData(String data) {
        // Create root category
        Category electronics = new Category();
        electronics.setName("Electronics");
        electronics.setDescription("Electronic devices and accessories");
        categoryRepository.save(electronics);

        // Create subcategories
        Category phones = new Category();
        phones.setName("Phones");
        phones.setDescription("Mobile phones and accessories");
        phones.setParent(electronics);
        categoryRepository.save(phones);

        Category laptops = new Category();
        laptops.setName("Laptops");
        laptops.setDescription("Laptops and accessories");
        laptops.setParent(electronics);
        categoryRepository.save(laptops);

        // Create feature templates for phones
        createPhoneTemplates(phones);

        // Create feature templates for laptops
        createLaptopTemplates(laptops);

        // Create sample products
        createSampleProducts(phones, laptops);
    }

    private void createPhoneTemplates(Category phones) {
        // Brand template
        CategoryFeatureTemplate brandTemplate = new CategoryFeatureTemplate();
        brandTemplate.setCode("BRAND");
        brandTemplate.setName("Brand");
        brandTemplate.setAttributeType("string");
        brandTemplate.setMandatory(true);
        brandTemplate.setCategory(phones);
        templateRepository.save(brandTemplate);

        // Model template
        CategoryFeatureTemplate modelTemplate = new CategoryFeatureTemplate();
        modelTemplate.setCode("MODEL");
        modelTemplate.setName("Model");
        modelTemplate.setAttributeType("string");
        modelTemplate.setMandatory(true);
        modelTemplate.setCategory(phones);
        templateRepository.save(modelTemplate);

        // Storage template
        CategoryFeatureTemplate storageTemplate = new CategoryFeatureTemplate();
        storageTemplate.setCode("STORAGE");
        storageTemplate.setName("Storage");
        storageTemplate.setAttributeType("numeric");
        storageTemplate.setUnit("GB");
        storageTemplate.setMinValue(32.0);
        storageTemplate.setMaxValue(1024.0);
        storageTemplate.setCategory(phones);
        templateRepository.save(storageTemplate);
    }

    private void createLaptopTemplates(Category laptops) {
        // Brand template
        CategoryFeatureTemplate brandTemplate = new CategoryFeatureTemplate();
        brandTemplate.setCode("BRAND");
        brandTemplate.setName("Brand");
        brandTemplate.setAttributeType("string");
        brandTemplate.setMandatory(true);
        brandTemplate.setCategory(laptops);
        templateRepository.save(brandTemplate);

        // Model template
        CategoryFeatureTemplate modelTemplate = new CategoryFeatureTemplate();
        modelTemplate.setCode("MODEL");
        modelTemplate.setName("Model");
        modelTemplate.setAttributeType("string");
        modelTemplate.setMandatory(true);
        modelTemplate.setCategory(laptops);
        templateRepository.save(modelTemplate);

        // RAM template
        CategoryFeatureTemplate ramTemplate = new CategoryFeatureTemplate();
        ramTemplate.setCode("RAM");
        ramTemplate.setName("RAM");
        ramTemplate.setAttributeType("numeric");
        ramTemplate.setUnit("GB");
        ramTemplate.setMinValue(4.0);
        ramTemplate.setMaxValue(128.0);
        ramTemplate.setCategory(laptops);
        templateRepository.save(ramTemplate);
    }

    private void createSampleProducts(Category phones, Category laptops) {
        // Create a phone product
        Product iphone = new Product();
        iphone.setName("iPhone 13");
        iphone.setDescription("Apple iPhone 13");
        iphone.setSku("IPHONE-13");
        iphone.addCategory(phones);
        productRepository.save(iphone);

        // Create features for iPhone
        List<CategoryFeatureTemplate> phoneTemplates = templateRepository.findByCategory(phones);
        for (CategoryFeatureTemplate template : phoneTemplates) {
            ProductFeature feature = new ProductFeature();
            feature.setProduct(iphone);
            feature.setTemplate(template);
            
            ProductFeatureValue value = new ProductFeatureValue();
            switch (template.getCode()) {
                case "BRAND" -> value.setValue("Apple");
                case "MODEL" -> value.setValue("iPhone 13");
                case "STORAGE" -> {
                    value.setValue("128");
                    value.setUnit("GB");
                }
            }
            feature.addValue(value);
            featureRepository.save(feature);
        }

        // Create a laptop product
        Product macbook = new Product();
        macbook.setName("MacBook Pro");
        macbook.setDescription("Apple MacBook Pro");
        macbook.setSku("MACBOOK-PRO");
        macbook.addCategory(laptops);
        productRepository.save(macbook);

        // Create features for MacBook
        List<CategoryFeatureTemplate> laptopTemplates = templateRepository.findByCategory(laptops);
        for (CategoryFeatureTemplate template : laptopTemplates) {
            ProductFeature feature = new ProductFeature();
            feature.setProduct(macbook);
            feature.setTemplate(template);
            
            ProductFeatureValue value = new ProductFeatureValue();
            switch (template.getCode()) {
                case "BRAND" -> value.setValue("Apple");
                case "MODEL" -> value.setValue("MacBook Pro");
                case "RAM" -> {
                    value.setValue("16");
                    value.setUnit("GB");
                }
            }
            feature.addValue(value);
            featureRepository.save(feature);
        }
    }
}
