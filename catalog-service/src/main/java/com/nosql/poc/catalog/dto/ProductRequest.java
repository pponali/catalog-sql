package com.nosql.poc.catalog.dto;

import com.nosql.poc.catalog.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class ProductRequest {
    @NotBlank(message = "SKU is required")
    @Size(max = 50, message = "SKU cannot exceed 50 characters")
    private String sku;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Category is required")
    private String category;

    private List<String> subCategories;

    @NotNull(message = "Base price is required")
    private BigDecimal basePrice;

    private String currency;

    private Map<String, Object> attributes;

    private List<String> tags;

    private String vendorId;

    private List<MediaRequest> media;

    public Product toProduct() {
        Product product = new Product();
        product.setSku(this.sku);
        product.setName(this.name);
        product.setDescription(this.description);
        product.setBrand(this.brand);
        product.setCategory(this.category);
        product.setSubCategories(this.subCategories);
        product.setBasePrice(this.basePrice);
        product.setCurrency(this.currency);
        product.setAttributes(this.attributes);
        product.setTags(this.tags);
        product.setVendorId(this.vendorId);
        if (this.media != null) {
            product.setMedia(this.media.stream()
                .map(MediaRequest::toProductMedia)
                .toList());
        }
        return product;
    }
}
