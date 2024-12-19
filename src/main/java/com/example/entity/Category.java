package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "category_super_categories",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "super_category_id")
    )
    private Set<Category> superCategories = new HashSet<>();

    @ManyToMany(mappedBy = "superCategories")
    private Set<Category> subCategories = new HashSet<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CategoryFeatureTemplate> featureTemplates = new HashSet<>();

    @Column(name = "inherit_features")
    private boolean inheritFeatures = true;

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "active")
    private boolean active = true;

    public void addSuperCategory(Category superCategory) {
        superCategories.add(superCategory);
        superCategory.getSubCategories().add(this);
    }

    public void removeSuperCategory(Category superCategory) {
        superCategories.remove(superCategory);
        superCategory.getSubCategories().remove(this);
    }

    public void addProduct(Product product) {
        products.add(product);
        product.getCategories().add(this);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        product.getCategories().remove(this);
    }

    public void addFeatureTemplate(CategoryFeatureTemplate template) {
        featureTemplates.add(template);
        template.setCategory(this);
    }

    public void removeFeatureTemplate(CategoryFeatureTemplate template) {
        featureTemplates.remove(template);
        template.setCategory(null);
    }

    public Set<CategoryFeatureTemplate> getAllFeatureTemplates() {
        Set<CategoryFeatureTemplate> allTemplates = new HashSet<>(featureTemplates);
        if (inheritFeatures) {
            for (Category superCategory : superCategories) {
                allTemplates.addAll(superCategory.getAllFeatureTemplates());
            }
        }
        return allTemplates;
    }

    public List<Category> getAllSuperCategories() {
        Set<Category> allSupers = new HashSet<>();
        Queue<Category> queue = new LinkedList<>(superCategories);
        
        while (!queue.isEmpty()) {
            Category current = queue.poll();
            if (allSupers.add(current)) {  // if this category hasn't been processed
                queue.addAll(current.getSuperCategories());
            }
        }
        
        return new ArrayList<>(allSupers);
    }

    public boolean isDescendantOf(Category potentialSuper) {
        if (superCategories.contains(potentialSuper)) {
            return true;
        }
        
        for (Category superCategory : superCategories) {
            if (superCategory.isDescendantOf(potentialSuper)) {
                return true;
            }
        }
        
        return false;
    }

    @PrePersist
    @PreUpdate
    private void validateHierarchy() {
        if (superCategories.contains(this)) {
            throw new IllegalStateException("A category cannot be its own super category");
        }
        
        for (Category superCategory : superCategories) {
            if (superCategory.isDescendantOf(this)) {
                throw new IllegalStateException("Circular reference detected in category hierarchy");
            }
        }
    }
}
