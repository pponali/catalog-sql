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
                .productCategories(new HashSet<>())
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
                .productCategories(new HashSet<>())
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
                .productCategories(new HashSet<>())
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
                .productCategories(new HashSet<>())
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


    public static Product createSamsungGalaxy(Category smartphoneCategory, Merchant tataCliq) {
        return Product.builder()
                .code("SAMSUNG-GALAXY-15")
                .productType(ProductType.SIMPLE)
                .merchant(tataCliq)
                .status("ACTIVE")
                .productCategories(new HashSet<>())
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .metadata("{}")
                .name("Samsung Galaxy 15")
                .description("Samsung Galaxy 15 with Snapdragon 8 Gen 2 processor")
                .build();
    }

    public static Product createAppleWatch(Category smartphoneCategory, Merchant tataCliq) {
        return Product.builder()
                .code("APPLE-WATCH-15")
                .productType(ProductType.SIMPLE)
                .merchant(tataCliq)
                .status("ACTIVE")
                .productCategories(new HashSet<>())
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .metadata("{}")
                .name("Apple Watch 15")
                .description("Apple Watch 15 with M2 chip")
                .build();
    }

    public static Product createHPSpectre(Category laptopCategory, Merchant croma) {
        return Product.builder()
                .code("HP-SPECTRE-15")
                .productType(ProductType.SIMPLE)
                .merchant(croma)
                .status("ACTIVE")
                .productCategories(new HashSet<>())
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .metadata("{}")
                .name("HP Spectre 15")
                .description("HP Spectre 15 with 12th Gen Intel Core i7 processor")
                .build();
    }

    public static Product createDiamondRing(Category necklaceCategory, Merchant tanishq) {
        return Product.builder()
                .code("TNSHQ-RNG-001")
                .productType(ProductType.SIMPLE)
                .merchant(tanishq)
                .status("ACTIVE")
                .productCategories(new HashSet<>())
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .metadata("{}")
                .name("Divyam Diamond Ring")
                .description("22K Diamond Traditional Wedding Ring")
                .build();


    }

    public static Product createParacetamol(Category medicinesCategory, Merchant tata1mg) {
        return Product.builder()
                .code("TATA-1MG-001")
                .productType(ProductType.SIMPLE)
                .merchant(tata1mg)
                .status("ACTIVE")
                .productCategories(new HashSet<>())
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .metadata("{}")
                .name("Paracetamol")
                .description("Paracetamol for fever")
                .build();
    }

    public static Product createFreshApples(Category freshProduceCategory, Merchant bigBasket) {
        return Product.builder()
                .code("BIG-BASKET-001")
                .productType(ProductType.SIMPLE)
                .merchant(bigBasket)
                .status("ACTIVE")
                .productCategories(new HashSet<>())
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .metadata("{}")
                .name("Fresh Apples")
                .description("Fresh Apples")
                .build();
    }
}
