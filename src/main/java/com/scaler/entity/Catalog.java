package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "catalog")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"business", "products", "categories", "siteCatalogs"})
@EqualsAndHashCode(callSuper = true, exclude = {"business", "products", "categories", "siteCatalogs"})
public class Catalog extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Business business;

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL)
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL)
    private Set<SiteCatalog> siteCatalogs = new HashSet<>();

    public void addProduct(Product product) {
        products.add(product);
        product.setCatalog(this);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        product.setCatalog(null);
    }

    public void addCategory(Category category) {
        categories.add(category);
        category.setCatalog(this);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
        category.setCatalog(null);
    }

    public void addSiteCatalog(SiteCatalog siteCatalog) {
        siteCatalogs.add(siteCatalog);
        siteCatalog.setCatalog(this);
    }

    public void removeSiteCatalog(SiteCatalog siteCatalog) {
        siteCatalogs.remove(siteCatalog);
        siteCatalog.setCatalog(null);
    }
}
