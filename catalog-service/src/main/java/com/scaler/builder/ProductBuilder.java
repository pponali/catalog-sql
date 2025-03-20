package com.scaler.builder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scaler.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
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
                .metadata(metadata)
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
                .metadata(metadata)
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
                .metadata(metadata)
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
                .metadata(metadata)
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
                .metadata(metadata)
                .productCategories(new HashSet<>())
                .createdBy("system")
                .lastModifiedBy("Prakash Ponali")
                .build();

        ProductCategory productCategory = ProductCategoryBuilder.createProductCategory(product, category, merchant);
        product.getProductCategories().add(productCategory);

        return product;
    }

    public static Product createSamsungPhone(Merchant croma, Category smartphoneCategory, Catalog electronicsCatalog) {
        ObjectNode metadata = objectMapper.createObjectNode();
        metadata.put("brand", "Samsung");
        metadata.put("model", "Galaxy S23");
        metadata.put("year", "2023");
        metadata.put("color", "Black");

        Product product = Product.builder()
                .code("SAMSUNG-S23")
                .name("Samsung Galaxy S23")
                .productType(ProductType.SIMPLE)

                .merchant(croma)
                .description("Samsung Galaxy S23 with 5G")
                .status("ACTIVE")
                .metadata(metadata)
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        ProductCategory productCategory = ProductCategoryBuilder.createProductCategory(product, smartphoneCategory, croma);
        product.getProductCategories().add(productCategory);

        return product;
    }

    public static Product createDellXps(Merchant croma, Category laptopCategory, Catalog electronicsCatalog) {
        ObjectNode metadata = objectMapper.createObjectNode();
        metadata.put("brand", "Dell");
        metadata.put("model", "XPS");
        metadata.put("year", "2023");
        metadata.put("color", "Black");

        Product product = Product.builder()
                .code("DELL-XPS")
                .name("Dell XPS")
                .productType(ProductType.SIMPLE)
                .merchant(croma)
                .description("Dell XPS with 5G")
                .status("ACTIVE")
                .metadata(metadata)
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        ProductCategory productCategory = ProductCategoryBuilder.createProductCategory(product, laptopCategory, croma);
        product.getProductCategories().add(productCategory);

        return product;
    }

    public static Product createMacbookPro(Merchant croma, Category laptopCategory, Catalog electronicsCatalog) {
        ObjectNode metadata = objectMapper.createObjectNode();
        metadata.put("brand", "Apple");
        metadata.put("model", "Macbook Pro");
        metadata.put("year", "2023");
        metadata.put("color", "Black");

        Product product = Product.builder()
                .code("APPLE-MBP")
                .name("Apple Macbook Pro")
                .productType(ProductType.SIMPLE)
                .merchant(croma)
                .description("Apple Macbook Pro with 5G")
                .status("ACTIVE")
                .metadata(metadata)
                .createdBy("system")
                .lastModifiedBy("system")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        ProductCategory productCategory = ProductCategoryBuilder.createProductCategory(product, laptopCategory, croma);
        product.getProductCategories().add(productCategory);

        return product;
    }

    /**
     * Creates a product with metadata from a map of attributes
     *
     * @param attributes Map of product attributes
     * @return Product entity with metadata
     * @throws JsonProcessingException If there is an error processing JSON
     */
    public Product createProductWithMetadata(Map<String, String> attributes) throws JsonProcessingException {
        log.info("Creating product with attributes: {}", attributes);

        Product product = new Product();

        // Set basic product properties
        product.setId(UUID.randomUUID());
        product.setCode(attributes.getOrDefault("Product_Code", "PROD-" + UUID.randomUUID().toString().substring(0, 8)));
        product.setName(attributes.getOrDefault("Product_Name", "Product " + UUID.randomUUID().toString().substring(0, 8)));
        product.setDescription(attributes.getOrDefault("Description", ""));
        // Always set product type to SIMPLE to avoid null constraint violations
        product.setProductType(ProductType.SIMPLE);

        // Set system fields
        product.setCreatedBy("system");
        product.setCreatedDate(LocalDateTime.now());
        product.setLastModifiedBy("system");
        product.setLastModifiedDate(LocalDateTime.now());

        // Initialize collections
        product.setProductCategories(new HashSet<>());
        product.setFeatureMappings(new HashSet<>());

        // Create and set metadata as JSON
        ObjectNode metadataNode = createProductMetadataNode(attributes);
        product.setMetadata(metadataNode);

        return product;
    }

    /**
     * Creates a product feature value with metadata
     *
     * @param product Product to associate with the feature value
     * @param featureCode Feature code
     * @param value Feature value
     * @param productFeature Product feature associated with this value
     * @param category Category associated with this feature value
     * @return ProductFeatureValue entity
     * @throws JsonProcessingException If there is an error processing JSON
     */
    public ProductFeatureValue createProductFeatureValueWithMetadata(
            Product product, String featureCode, String value, ProductFeature productFeature, Category category) throws JsonProcessingException {
        log.info("Creating product feature value for product: {}, feature: {}, value: {}",
                product.getName(), featureCode, value);

        ProductFeatureValue featureValue = new ProductFeatureValue();
        featureValue.setId(UUID.randomUUID());
        
        // Set the feature
        featureValue.setFeature(productFeature);

        // Set system fields
        featureValue.setCreatedBy("system");
        featureValue.setCreatedDate(LocalDateTime.now());

        // Create metadata node for the feature value
        ObjectNode metadataNode = objectMapper.createObjectNode();
        metadataNode.put("featureCode", featureCode);
        metadataNode.put("value", value);
        featureValue.setMetadata(metadataNode);

        // Set the attribute value as a JSON node
        featureValue.setAttributeValue(objectMapper.valueToTree(value));
        
        // Create product mapping
        if (product != null) {
            ProductFeatureValueMapping mapping = new ProductFeatureValueMapping();
            mapping.setProduct(product);
            mapping.setFeatureValue(featureValue);
            mapping.setCategory(category);
            mapping.setIsActive(true);
            
            if (featureValue.getProductMappings() == null) {
                featureValue.setProductMappings(new HashSet<>());
            }
            featureValue.getProductMappings().add(mapping);
            
            if (product.getFeatureValueMappings() == null) {
                product.setFeatureValueMappings(new HashSet<>());
            }
            product.getFeatureValueMappings().add(mapping);
        }

        return featureValue;
    }
    
    /**
     * Simplified version that creates a product feature value with metadata
     *
     * @param product Product to associate with the feature value
     * @param featureCode Feature code
     * @param value Feature value
     * @return ProductFeatureValue entity
     * @throws JsonProcessingException If there is an error processing JSON
     */
    public ProductFeatureValue createProductFeatureValueWithMetadata(
            Product product, String featureCode, String value) throws JsonProcessingException {
        // Use a default ProductFeature if none is provided
        ProductFeature feature = new ProductFeature();
        feature.setId(UUID.randomUUID());
        feature.setCode(featureCode);
        feature.setName(featureCode);
        
        // Use the product's primary category if available
        Category category = product.getPrimaryCategory();
        if (category == null) {
            category = new Category();
            category.setId(UUID.randomUUID());
            category.setName("Default Category");
        }
        
        return createProductFeatureValueWithMetadata(product, featureCode, value, feature, category);
    }

    /**
     * Creates metadata for a product as a JSON node
     *
     * @param attributes Map of product attributes
     * @return ObjectNode containing product metadata
     */
    private ObjectNode createProductMetadataNode(Map<String, String> attributes) {
        ObjectNode metadataNode = objectMapper.createObjectNode();

        // Add all attributes to the metadata node
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            metadataNode.put(entry.getKey(), entry.getValue());
        }

        return metadataNode;
    }

    /**
     * Adds a category to a product
     *
     * @param product Product to add the category to
     * @param productCategory ProductCategory to add
     * @return Updated product
     */
    public Product addCategoryToProduct(Product product, ProductCategory productCategory) {
        log.info("Adding category {} to product {}",
                productCategory.getCategory().getName(), product.getName());

        if (product.getProductCategories() == null) {
            product.setProductCategories(new HashSet<>());
        }

        product.getProductCategories().add(productCategory);
        productCategory.setProduct(product);

        return product;
    }

    /**
     * Adds a feature value to a product
     *
     * @param product Product to add the feature value to
     * @param featureValue Feature value to add
     * @return Updated product
     */
    public Product addFeatureValueToProduct(Product product, ProductFeatureValue featureValue) {
        log.info("Adding feature value {} to product {}",
                featureValue.getId(), product.getName());

        // Get current feature values
        Set<ProductFeatureValue> featureValues = product.getFeatureValues();
        if (featureValues == null) {
            featureValues = new HashSet<>();
        }
        
        // Add the new feature value
        featureValues.add(featureValue);
        
        // Set the updated feature values
        product.setFeatureValues(featureValues);
        
        // Ensure bidirectional relationship is set
        featureValue.setProduct(product);
        
        return product;
    }





}
