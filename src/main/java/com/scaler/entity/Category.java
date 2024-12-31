package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "category")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@DiscriminatorValue("STANDARD")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"parent", "children", "templates", "products", "business", "catalog"})
@ToString(callSuper = true, exclude = {"parent", "children", "templates", "products", "business", "catalog"})
public class Category extends BaseEntity {
    @Column(unique = false, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id", nullable = false)
    private Catalog catalog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Category> children = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<CategoryFeatureTemplate> templates = new HashSet<>();

    @ManyToMany(mappedBy = "categories")
    @Builder.Default
    private Set<Product> products = new HashSet<>();

    public void addTemplate(CategoryFeatureTemplate template) {
        templates.add(template);
        template.setCategory(this);
    }

    public void removeTemplate(CategoryFeatureTemplate template) {
        templates.remove(template);
        template.setCategory(null);
    }

    public void addProduct(Product product) {
        products.add(product);
        product.getCategories().add(this);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        product.getCategories().remove(this);
    }

    public void addChild(Category child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(Category child) {
        children.remove(child);
        child.setParent(null);
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }

    public int getLevel() {
        if (isRoot()) {
            return 0;
        }
        return parent.getLevel() + 1;
    }

    public String getPath() {
        if (isRoot()) {
            return code;
        }
        return parent.getPath() + "/" + code;
    }

    public String getCode() {
        return code;
    }
}
