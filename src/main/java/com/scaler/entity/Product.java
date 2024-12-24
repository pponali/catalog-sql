package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"features", "category", "featureValues"})
@ToString(exclude = {"features", "category", "featureValues"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private String metadata;

    @Column
    private String sku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ProductFeature> features = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ProductFeatureValue> featureValues = new HashSet<>();

    public void addFeature(ProductFeature feature) {
        features.add(feature);
        feature.setProduct(this);
    }

    public void removeFeature(ProductFeature feature) {
        features.remove(feature);
        feature.setProduct(null);
    }

    public void addFeatureValue(ProductFeatureValue value) {
        featureValues.add(value);
        value.setProduct(this);
    }

    public void removeFeatureValue(ProductFeatureValue value) {
        featureValues.remove(value);
        value.setProduct(null);
    }

    public void setCategory(Category category) {
        this.category = category;
        if (category != null) {
            category.getProducts().add(this);
        }
    }
}
