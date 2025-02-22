package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"catalog", "categories", "features", "merchant", "unitOfMeasure", "channels", "lineOfBusiness", "sellers"})
@EqualsAndHashCode(callSuper = true, exclude = {"catalog", "categories", "features", "merchant", "unitOfMeasure", "channels", "lineOfBusiness", "sellers"})
public class Product extends BaseEntity {
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "product_type", nullable = false)
    private String productType;

    @Column(name = "status")
    private String status;

    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;

    @Column(name = "sku")
    private String sku;

    @Column(name = "price")
    private Double price;

    @ManyToMany
    @JoinTable(
        name = "product_categories",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductAttribute> attributes = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductFeature> features = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_of_measure_id")
    private UnitOfMeasure unitOfMeasure;
    
    @ManyToMany
    @JoinTable(
        name = "product_channels",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "channel_id")
    )
    private Set<Channel> channels = new HashSet<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_of_business_id", nullable = false)
    private LineOfBusiness lineOfBusiness;

    @ManyToMany
    @JoinTable(
        name = "product_sellers",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "seller_id")
    )
    private Set<Seller> sellers = new HashSet<>();

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

    public void addSeller(Seller seller) {
        sellers.add(seller);
        seller.getProducts().add(this);
    }

    public void removeSeller(Seller seller) {
        sellers.remove(seller);
        seller.getProducts().remove(this);
    }
    
    public void addChannel(Channel channel) {
        channels.add(channel);
        channel.getProducts().add(this);
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
        channel.getProducts().remove(this);
    }
}
