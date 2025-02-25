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
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"products", "merchants", "channels", "business", "categories", "siteCatalogs"})
@EqualsAndHashCode(callSuper = true, exclude = {"products", "merchants", "channels",  "business", "categories", "siteCatalogs"})
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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id")
    private Merchant business;

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL)
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL)
    private Set<StoreCatalog> siteCatalogs = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "catalog_merchants",
        joinColumns = @JoinColumn(name = "catalog_id"),
        inverseJoinColumns = @JoinColumn(name = "merchant_id")
    )
    private Set<Merchant> merchants = new HashSet<>();
    
    @ManyToMany
    @JoinTable(
        name = "catalog_channels",
        joinColumns = @JoinColumn(name = "catalog_id"),
        inverseJoinColumns = @JoinColumn(name = "channel_id")
    )
    private Set<Channel> channels = new HashSet<>();
    

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

    public void addSiteCatalog(StoreCatalog siteCatalog) {
        siteCatalogs.add(siteCatalog);
        siteCatalog.setCatalog(this);
    }

    public void removeSiteCatalog(StoreCatalog siteCatalog) {
        siteCatalogs.remove(siteCatalog);
        siteCatalog.setCatalog(null);
    }
    
    public void addMerchant(Merchant merchant) {
        merchants.add(merchant);
        merchant.getCatalogs().add(this);
    }
    
    public void removeMerchant(Merchant merchant) {
        merchants.remove(merchant);
        merchant.getCatalogs().remove(this);
    }
    
    public void addChannel(Channel channel) {
        channels.add(channel);
        channel.getCatalogs().add(this);
    }
    
    public void removeChannel(Channel channel) {
        channels.remove(channel);
        channel.getCatalogs().remove(this);
    }
    

}
