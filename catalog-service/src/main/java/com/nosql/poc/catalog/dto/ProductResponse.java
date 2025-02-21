package com.nosql.poc.catalog.dto;

import com.nosql.poc.catalog.model.Product;
import com.nosql.poc.catalog.model.ValidationStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ProductResponse {
    private String id;
    private String sku;
    private String name;
    private String description;
    private String brand;
    private String category;
    private List<String> subCategories;
    private BigDecimal basePrice;
    private String currency;
    private Map<String, Object> attributes;
    private List<String> tags;
    private String vendorId;
    private List<MediaResponse> media;
    private ValidationStatus validationStatus;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String version;

    public static ProductResponse fromProduct(Product product) {
        if (product == null) return null;
        
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setSku(product.getSku());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setBrand(product.getBrand());
        response.setCategory(product.getCategory());
        response.setSubCategories(product.getSubCategories());
        response.setBasePrice(product.getBasePrice());
        response.setCurrency(product.getCurrency());
        response.setAttributes(product.getAttributes());
        response.setTags(product.getTags());
        response.setVendorId(product.getVendorId());
        
        if (product.getMedia() != null) {
            response.setMedia(product.getMedia().stream()
                .map(MediaResponse::fromProductMedia)
                .toList());
        }
        
        response.setValidationStatus(product.getValidationStatus());
        
        if (product.getAuditInfo() != null) {
            response.setCreatedDate(product.getAuditInfo().getCreatedDate());
            response.setLastModifiedDate(product.getAuditInfo().getLastModifiedDate());
            response.setVersion(product.getAuditInfo().getVersion());
        }
        
        return response;
    }
}
