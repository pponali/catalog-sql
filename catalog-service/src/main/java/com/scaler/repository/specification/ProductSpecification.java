package com.scaler.repository.specification;

import com.scaler.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ProductSpecification {

    public static Specification<Product> nameLike(String name) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
    
    public static Specification<Product> descriptionLike(String description) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }
    
    public static Specification<Product> skuLike(String sku) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.like(criteriaBuilder.lower(root.get("sku")), "%" + sku.toLowerCase() + "%");
    }
    
    public static Specification<Product> inCategories(List<UUID> categoryIds) {
        return (root, query, criteriaBuilder) -> 
            root.join("productCategories").get("category").get("id").in(categoryIds);
    }
    
    public static Specification<Product> priceGreaterThanOrEqual(Double price) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.greaterThanOrEqualTo(root.get("price"), price);
    }
    
    public static Specification<Product> priceLessThanOrEqual(Double price) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.lessThanOrEqualTo(root.get("price"), price);
    }
    
    public static Specification<Product> brandEquals(String brand) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(criteriaBuilder.lower(root.get("brand")), brand.toLowerCase());
    }
}