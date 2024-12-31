package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {
    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "product_type")
    private String productType;

    @Column
    private String status;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String metadata;

    @Column
    private String sku;

    @ManyToMany
    @JoinTable(
        name = "product_categories",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductFeature> features = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductFeatureValue> featureValues = new HashSet<>();

    public void addCategory(Category category) {
        categories.add(category);
        category.getProducts().add(this);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
        category.getProducts().remove(this);
    }

    public void addFeature(ProductFeature feature) {
        features.add(feature);
        feature.setProduct(this);
    }

    public void removeFeature(ProductFeature feature) {
        features.remove(feature);
        feature.setProduct(null);
    }

    public void addFeatureValue(ProductFeatureValue featureValue) {
        featureValues.add(featureValue);
        featureValue.setProduct(this);
    }

    public void removeFeatureValue(ProductFeatureValue featureValue) {
        featureValues.remove(featureValue);
        featureValue.setProduct(null);
    }
}
