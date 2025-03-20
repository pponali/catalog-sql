package com.scaler.repository.specification;

import com.scaler.dto.ProductSearchCriteria;
import com.scaler.entity.Product;
import com.scaler.entity.Seller;
import com.scaler.entity.Category;
import com.scaler.entity.ProductCategory;
import com.scaler.entity.ProductPrice;
import com.scaler.entity.ProductInventory;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JPA Specification for Product entity to create dynamic queries
 */
public class ProductSpecification {
    
    /**
     * Create a specification based on search criteria DTO
     */
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
    
    /**
     * Search for products where name contains the provided text
     */
    public static Specification<Product> nameLike(String text) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + text.toLowerCase() + "%");
    }

    /**
     * Search for products where description contains the provided text
     */
    public static Specification<Product> descriptionLike(String text) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + text.toLowerCase() + "%");
    }

    /**
     * Search for products where SKU contains the provided text
     */
    public static Specification<Product> skuLike(String text) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.like(criteriaBuilder.lower(root.get("sku")), "%" + text.toLowerCase() + "%");
    }

    /**
     * Filter products by exact brand
     */
    public static Specification<Product> brandEquals(String brand) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("brand"), brand);
    }

    /**
     * Filter products that are in any of the specified categories
     */
    public static Specification<Product> inCategories(List<UUID> categoryIds) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, ProductCategory> categoryJoin = root.join("categories", JoinType.LEFT);
            return categoryJoin.get("categoryId").in(categoryIds);
        };
    }

    /**
     * Filter products with price greater than or equal to the specified amount
     */
    public static Specification<Product> priceGreaterThanOrEqual(double price) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, ProductPrice> priceJoin = root.join("prices", JoinType.LEFT);
            return criteriaBuilder.greaterThanOrEqualTo(priceJoin.get("amount"), price);
        };
    }

    /**
     * Filter products with price less than or equal to the specified amount
     */
    public static Specification<Product> priceLessThanOrEqual(double price) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, ProductPrice> priceJoin = root.join("prices", JoinType.LEFT);
            return criteriaBuilder.lessThanOrEqualTo(priceJoin.get("amount"), price);
        };
    }

    /**
     * Filter products by availability (inventory > 0)
     */
    public static Specification<Product> inStock() {
        return (root, query, criteriaBuilder) -> {
            Join<Product, ProductInventory> inventoryJoin = root.join("inventory", JoinType.LEFT);
            return criteriaBuilder.greaterThan(inventoryJoin.get("quantity"), 0);
        };
    }
}
