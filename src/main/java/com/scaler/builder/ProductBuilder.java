package com.scaler.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scaler.entity.*;

import java.time.LocalDateTime;
import java.util.HashSet;

public class ProductBuilder {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Product createMacBookPro(Category category, Merchant merchant) {
        ObjectNode metadata = objectMapper.createObjectNode();
        metadata.put("brand", "Apple");
        metadata.put("model", "MacBook Pro");
        metadata.put("year", "2023");

        Product product = Product.builder()
                .code("MBP-2023")
                .name("MacBook Pro 16-inch")
                .description("Apple MacBook Pro with M2 Pro chip")
                .status("ACTIVE")
                .merchant(merchant)
                .productType(ProductType.SIMPLE)
                .metadata(metadata.toString())
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        ProductCategory productCategory = ProductCategoryBuilder.createProductCategory(product, category, merchant);
        product.getProductCategories().add(productCategory);

        return product;
    }

    public static Product createDellXPS(Category category, Merchant merchant) {
        ObjectNode metadata = objectMapper.createObjectNode();
        metadata.put("brand", "Dell");
        metadata.put("model", "XPS");
        metadata.put("year", "2023");

        Product product = Product.builder()
                .code("DELL-XPS-2023")
                .name("Dell XPS 15")
                .productType(ProductType.SIMPLE)
                .merchant(merchant)
                .description("Dell XPS 15 with Intel i9 processor")
                .status("ACTIVE")
                .metadata(metadata.toString())
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        ProductCategory productCategory = ProductCategoryBuilder.createProductCategory(product, category, merchant);
        product.getProductCategories().add(productCategory);

        return product;
    }

    public static Product createIPhone(Category category, Merchant merchant) {
        ObjectNode metadata = objectMapper.createObjectNode();
        metadata.put("brand", "Apple");
        metadata.put("model", "iPhone");
        metadata.put("year", "2023");

        Product product = Product.builder()
                .code("IPHONE-15")
                .name("iPhone 15 Pro Max")
                .productType(ProductType.SIMPLE)
                .merchant(merchant)
                .description("Apple iPhone 15 Pro Max")
                .status("ACTIVE")
                .metadata(metadata.toString())
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        ProductCategory productCategory = ProductCategoryBuilder.createProductCategory(product, category, merchant);
        product.getProductCategories().add(productCategory);

        return product;
    }

    public static Product createGoldNecklace(Category category, Merchant merchant) {
        ObjectNode metadata = objectMapper.createObjectNode();
        metadata.put("brand", "Tanishq");
        metadata.put("collection", "Divyam");
        metadata.put("occasion", "Wedding");

        Product product = Product.builder()
                .code("TNSHQ-NKLC-001")
                .name("Divyam Gold Necklace")
                .productType(ProductType.SIMPLE)
                .merchant(merchant)
                .description("22K Gold Traditional Wedding Necklace")
                .status("ACTIVE")
                .metadata(metadata.toString())
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        ProductCategory productCategory = ProductCategoryBuilder.createProductCategory(product, category, merchant);
        product.getProductCategories().add(productCategory);

        return product;
    }

    public static Product createGoldBangles(Category category, Merchant merchant) {
        ObjectNode metadata = objectMapper.createObjectNode();
        metadata.put("brand", "Tanishq");
        metadata.put("collection", "Rivaah");
        metadata.put("occasion", "Wedding");

        Product product = Product.builder()
                .code("TNSHQ-BNGL-001")
                .name("Rivaah Gold Bangles")
                .productType(ProductType.SIMPLE)
                .merchant(merchant)
                .description("22K Gold Traditional Wedding Bangles")
                .status("ACTIVE")
                .metadata(metadata.toString())
                .productCategories(new HashSet<>())
                .createdBy("system")
                .lastModifiedBy("Prakash Ponali")
                .build();

        ProductCategory productCategory = ProductCategoryBuilder.createProductCategory(product, category, merchant);
        product.getProductCategories().add(productCategory);

        return product;
    }





}
