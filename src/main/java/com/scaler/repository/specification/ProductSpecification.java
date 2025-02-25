package com.scaler.repository.specification;

import com.scaler.dto.ProductSearchCriteria;
import com.scaler.entity.Product;
import com.scaler.entity.Seller;
import com.scaler.entity.Category;
import com.scaler.entity.ProductCategory;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    
    public static Specification<Product> withSearchCriteria(ProductSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Join tables
            Join<Product, Seller> sellerJoin = null;
            
            // Seller criteria
            if (criteria.getSellerId() != null) {
                if (sellerJoin == null) {
                    sellerJoin = root.join("sellers");
                }
                predicates.add(cb.equal(sellerJoin.get("id"), criteria.getSellerId()));
            }
            
            // Merchant criteria
            if (criteria.getMerchantId() != null) {
                predicates.add(cb.equal(root.get("merchant").get("id"), criteria.getMerchantId()));
            }
            
            // Channel criteria
            if (criteria.getChannelId() != null) {
                predicates.add(cb.equal(root.get("channel").get("id"), criteria.getChannelId()));
            }
            
            // Catalog criteria
            if (criteria.getCatalogId() != null) {
                predicates.add(cb.equal(root.get("catalog").get("id"), criteria.getCatalogId()));
            }
            
            // Category criteria
            if (criteria.getCategoryIds() != null && !criteria.getCategoryIds().isEmpty()) {
                Join<Product, ProductCategory> productCategoryJoin = root.join("productCategories");
                Join<ProductCategory, Category> categoryJoin = productCategoryJoin.join("category");
                predicates.add(categoryJoin.get("id").in(criteria.getCategoryIds()));
            }
            
            // Basic product attributes
            if (criteria.getName() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), 
                    "%" + criteria.getName().toLowerCase() + "%"));
            }
            
            if (criteria.getCode() != null) {
                predicates.add(cb.equal(root.get("code"), criteria.getCode()));
            }
            
            if (criteria.getSku() != null) {
                predicates.add(cb.equal(root.get("sku"), criteria.getSku()));
            }
            
            if (criteria.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
            }
            
            if (criteria.getProductType() != null) {
                predicates.add(cb.equal(root.get("productType"), criteria.getProductType()));
            }
            
            // Price range
            if (criteria.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), criteria.getMinPrice()));
            }
            
            if (criteria.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), criteria.getMaxPrice()));
            }
            
            // Active status
            if (criteria.getIsActive() != null) {
                predicates.add(cb.equal(root.get("isActive"), criteria.getIsActive()));
            }
            
            // Tags
            if (criteria.getTags() != null && !criteria.getTags().isEmpty()) {
                Expression<String> tagsExpression = root.get("tags");
                List<Predicate> tagPredicates = criteria.getTags().stream()
                    .map(tag -> cb.like(tagsExpression, "%" + tag + "%"))
                    .toList();
                predicates.add(cb.or(tagPredicates.toArray(new Predicate[0])));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
