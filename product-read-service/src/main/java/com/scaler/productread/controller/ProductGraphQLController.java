package com.scaler.productread.controller;

import com.scaler.productread.document.ProductDocument;
import com.scaler.productread.dto.ProductFilterDTO;
import com.scaler.productread.dto.ProductResultDTO;
import com.scaler.productread.dto.ProductSortDTO;
import com.scaler.productread.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

/**
 * GraphQL controller for product queries.
 */
@Controller
public class ProductGraphQLController {
    
    private final ProductService productService;
    
    @Autowired
    public ProductGraphQLController(ProductService productService) {
        this.productService = productService;
    }
    
    /**
     * Get a product by ID.
     */
    @QueryMapping
    public ProductDocument product(@Argument String id) {
        return productService.findById(UUID.fromString(id));
    }
    
    /**
     * Find products with filtering.
     */
    @QueryMapping
    public ProductResultDTO products(@Argument ProductFilterDTO filter, 
                                     @Argument Integer page,
                                     @Argument Integer size,
                                     @Argument List<ProductSortDTO> sort) {
        return productService.findProducts(filter, page, size, sort);
    }
    
    /**
     * Search products by text query.
     */
    @QueryMapping
    public ProductResultDTO productSearch(@Argument String query,
                                          @Argument Integer page,
                                          @Argument Integer size) {
        return productService.searchProducts(query, page, size);
    }
    
    /**
     * Find products by category.
     */
    @QueryMapping
    public ProductResultDTO productsByCategory(@Argument String categoryId,
                                              @Argument Integer page,
                                              @Argument Integer size) {
        return productService.findByCategory(UUID.fromString(categoryId), page, size);
    }
    
    /**
     * Find products by brand.
     */
    @QueryMapping
    public ProductResultDTO productsByBrand(@Argument String brand,
                                           @Argument Integer page,
                                           @Argument Integer size) {
        return productService.findByBrand(brand, page, size);
    }
    
    /**
     * Find products by channel.
     */
    @QueryMapping
    public ProductResultDTO productsByChannel(@Argument String channelId,
                                             @Argument Integer page,
                                             @Argument Integer size) {
        return productService.findByChannel(UUID.fromString(channelId), page, size);
    }
    
    /**
     * Find products by seller.
     */
    @QueryMapping
    public ProductResultDTO productsBySeller(@Argument String sellerId,
                                            @Argument Integer page,
                                            @Argument Integer size) {
        return productService.findBySeller(UUID.fromString(sellerId), page, size);
    }
}