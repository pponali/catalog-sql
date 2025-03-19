package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "category")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@DiscriminatorValue("STANDARD")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"templates", "productCategories", "business", "catalog", "merchant"})
@ToString(callSuper = true, exclude = {"templates", "productCategories", "business", "catalog", "merchant"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category extends BaseEntity {
    @Column(unique = false, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    private String status;
    
    @Column(columnDefinition = "TEXT")
    private String metadata;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Merchant merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id", nullable = false)
    private Catalog catalog;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @lombok.Builder.Default
    private Set<CategoryFeatureTemplate> templates = new HashSet<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIdentityReference(alwaysAsId = true)
    @lombok.Builder.Default
    private Set<ProductCategory> productCategories = new HashSet<>();

    public void addTemplate(CategoryFeatureTemplate template) {
        templates.add(template);
        template.setCategory(this);
    }

    public void removeTemplate(CategoryFeatureTemplate template) {
        templates.remove(template);
        template.setCategory(null);
    }

    public void addProductCategory(ProductCategory productCategory) {
        productCategories.add(productCategory);
        productCategory.setCategory(this);
    }

    public void removeProductCategory(ProductCategory productCategory) {
        productCategories.remove(productCategory);
        productCategory.setCategory(null);
    }
}
