package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product")
@Data
@EqualsAndHashCode(exclude = {"categories", "features"})
@ToString(exclude = {"categories", "features"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(unique = true)
    private String sku;

    @ManyToMany
    @JoinTable(
        name = "product_categories",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(
        mappedBy = "product",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<ProductFeature> features = new HashSet<>();

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
    }

    public void addFeature(ProductFeature feature) {
        features.add(feature);
        feature.setProduct(this);
    }

    public void removeFeature(ProductFeature feature) {
        features.remove(feature);
        feature.setProduct(null);
    }
}
