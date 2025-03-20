package com.scaler.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Table(name = "product")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true, exclude = {"catalog", "productCategories", "features", "merchant"})
@EqualsAndHashCode(callSuper = true, exclude = {"catalog", "productCategories", "features", "merchant"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product extends BaseEntity {
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "product_type", nullable = false)
    private ProductType productType;

    @Column(name = "status")
    private String status;

    @Column(name = "metadata", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private com.fasterxml.jackson.databind.JsonNode metadata;
    
    public JsonNode getMetadata() {
        return metadata;
    }
    
    public void setMetadata(JsonNode metadata) {
        this.metadata = metadata;
    }

    @Column(name = "sku")
    private String sku;

    @Column(name = "price")
    private Double price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIdentityReference(alwaysAsId = true)
    @lombok.Builder.Default
    private Set<ProductCategory> productCategories = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @lombok.Builder.Default
    private List<ProductAttribute> attributes = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("product-features")
    @lombok.Builder.Default
    private Set<ProductFeatureMapping> featureMappings = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference("product-feature-values")
    @lombok.Builder.Default
    private Set<ProductFeatureValueMapping> featureValueMappings = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_of_measure_id")
    private UnitOfMeasure unitOfMeasure;

    public void addProductCategory(ProductCategory productCategory) {
        productCategories.add(productCategory);
        productCategory.setProduct(this);
    }

    public void removeProductCategory(ProductCategory productCategory) {
        productCategories.remove(productCategory);
        productCategory.setProduct(null);
    }

    public void addFeatureMapping(ProductFeatureMapping mapping) {
        featureMappings.add(mapping);
        mapping.setProduct(this);
    }


    /*public Set<Channel> getEnabledChannels() {
        return productChannels.stream()
                .filter(ProductChannel::getIsEnabled)
                .map(ProductChannel::getChannel)
                .collect(Collectors.toSet());
    }*/

    public void removeFeatureMapping(ProductFeatureMapping mapping) {
        featureMappings.remove(mapping);
        mapping.setProduct(null);
    }

    public Product() {
        super();
    }
    
    /**
     * Gets the feature values for this product via feature value mappings
     * @return Set of ProductFeatureValue objects
     */
    public Set<ProductFeatureValue> getFeatureValues() {
        if (featureValueMappings == null) {
            return new HashSet<>();
        }
        return featureValueMappings.stream()
                .map(ProductFeatureValueMapping::getFeatureValue)
                .collect(java.util.stream.Collectors.toSet());
    }
    
    /**
     * Sets the feature values for this product
     * @param featureValues Set of ProductFeatureValue objects
     */
    public void setFeatureValues(Set<ProductFeatureValue> featureValues) {
        // Clear existing mappings
        if (featureValueMappings != null) {
            featureValueMappings.clear();
        } else {
            featureValueMappings = new HashSet<>();
        }
        
        // Add new mappings
        if (featureValues != null) {
            for (ProductFeatureValue value : featureValues) {
                ProductFeatureValueMapping mapping = new ProductFeatureValueMapping();
                mapping.setProduct(this);
                mapping.setFeatureValue(value);
                mapping.setIsActive(true);
                featureValueMappings.add(mapping);
            }
        }
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }



    /**
     * Returns the primary category of the product.
     * @return The primary category or null if no primary category exists
     */
    public Category getPrimaryCategory() {
        if (productCategories == null) {
            return null;
        }
        return productCategories.stream()
                .filter(pc -> Boolean.TRUE.equals(pc.getIsPrimary()))
                .findFirst()
                .map(ProductCategory::getCategory)
                .orElse(null);
    }
}
