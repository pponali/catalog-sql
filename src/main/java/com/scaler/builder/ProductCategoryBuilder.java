package com.scaler.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scaler.entity.Category;
import com.scaler.entity.Merchant;
import com.scaler.entity.Product;
import com.scaler.entity.ProductCategory;

public class ProductCategoryBuilder {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ProductCategory createProductCategory(Product product, Category category, Merchant merchant) {


        ProductCategory productCategory = ProductCategory.builder()
                .product(product)
                .category(category)
                .displayOrder(1)
                .merchant(merchant)
                .createdBy("system")
                .lastModifiedBy("system")
                .build();

        return productCategory;
    }

}
