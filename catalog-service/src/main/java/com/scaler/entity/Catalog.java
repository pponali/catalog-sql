package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.Builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "catalog")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"products", "merchants", "business", "categories"})
@EqualsAndHashCode(callSuper = true, exclude = {"products", "merchants", "business", "categories"})
public class Catalog extends BaseEntity {
    
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "merchant_id")
    private java.util.UUID merchantId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Merchant business;

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL)
    @lombok.Builder.Default
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL)
    @lombok.Builder.Default
    private List<Category> categories = new ArrayList<>();

    // StoreCatalog relationship removed as part of microservice separation

    @ManyToMany
    @JoinTable(
        name = "catalog_merchants",
        joinColumns = @JoinColumn(name = "catalog_id"),
        inverseJoinColumns = @JoinColumn(name = "merchant_id")
    )
    @lombok.Builder.Default
    private Set<Merchant> merchants = new HashSet<>();
    
    

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

    // StoreCatalog methods removed as part of microservice separation
    
    public void addMerchant(Merchant merchant) {
        merchants.add(merchant);
        merchant.getCatalogs().add(this);
    }
    
    public void removeMerchant(Merchant merchant) {
        merchants.remove(merchant);
        merchant.getCatalogs().remove(this);
    }
    
    public void setMerchant(Merchant merchant) {
        this.addMerchant(merchant);
        this.merchantId = merchant.getId();
    }
    
    public void setMerchantId(java.util.UUID merchantId) {
        this.merchantId = merchantId;
    }
}
