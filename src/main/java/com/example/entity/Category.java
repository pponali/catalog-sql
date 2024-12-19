package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "category")
@Data
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CategoryFeatureTemplate> templates = new HashSet<>();

    @ManyToMany(mappedBy = "categories")
    @ToString.Exclude
    private Set<Product> products = new HashSet<>();

    public void addTemplate(CategoryFeatureTemplate template) {
        templates.add(template);
        template.setCategory(this);
    }

    public void removeTemplate(CategoryFeatureTemplate template) {
        templates.remove(template);
        template.setCategory(null);
    }

    public void addChild(Category child) {
        children.add(child);
        child.setParent(this);
    }

    public void removeChild(Category child) {
        children.remove(child);
        child.setParent(null);
    }
}
