package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@DiscriminatorValue("STANDARD")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"parent", "children", "templates", "products"})
@ToString(callSuper = true, exclude = {"parent", "children", "templates", "products"})
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @lombok.Builder.Default
    private Set<Category> children = new HashSet<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @lombok.Builder.Default
    private Set<CategoryFeatureTemplate> templates = new HashSet<>();

    @ManyToMany(mappedBy = "categories")
    @lombok.Builder.Default
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
