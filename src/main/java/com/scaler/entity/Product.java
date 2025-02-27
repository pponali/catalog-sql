package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "product")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"catalog", "productCategories", "features", "merchant", "channels", "sellerProducts"})
@EqualsAndHashCode(callSuper = true, exclude = {"catalog", "productCategories", "features", "merchant", "channels",  "sellerProducts"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product extends BaseEntity {
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "product_type", nullable = false)
    private ProductType productType;

    @Column(name = "status")
    private String status;

    @Column(name = "metadata", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String metadata;

    @Column(name = "sku")
    private String sku;

    @Column(name = "price")
    private Double price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIdentityReference(alwaysAsId = true)
    private Set<ProductCategory> productCategories = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductAttribute> attributes = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductFeatureMapping> featureMappings = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id")
    private Catalog catalog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_of_measure_id")
    private UnitOfMeasure unitOfMeasure;
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductChannel> productChannels = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<SellerProduct> sellerProducts = new HashSet<>();

    public void addProductCategory(ProductCategory productCategory) {
        productCategories.add(productCategory);
        productCategory.setProduct(this);
    }

    public void removeProductCategory(ProductCategory productCategory) {
        productCategories.remove(productCategory);
        productCategory.setProduct(null);
    }

    public void addFeatureMapping(ProductFeatureMapping mapping) {
        featureMappings.add(mapping);
        mapping.setProduct(this);
    }

    public void addProductChannel(Channel channel) {
        ProductChannel productChannel = ProductChannel.builder()
            .product(this)
            .channel(channel)
            .isEnabled(true)
            .isVisible(true)
            .effectiveFrom(LocalDateTime.now())
            .build();
        productChannels.add(productChannel);
    }

    public void removeProductChannel(Channel channel) {
        productChannels.removeIf(pc -> pc.getChannel().equals(channel));
    }

    public void disableProductChannel(Channel channel) {
        productChannels.stream()
            .filter(pc -> pc.getChannel().equals(channel))
            .findFirst()
            .ifPresent(pc -> pc.setIsEnabled(false));
    }

    public void enableProductChannel(Channel channel) {
        productChannels.stream()
            .filter(pc -> pc.getChannel().equals(channel))
            .findFirst()
            .ifPresent(pc -> pc.setIsEnabled(true));
    }

    public boolean isEnabledForChannel(Channel channel) {
        return productChannels.stream()
            .filter(pc -> pc.getChannel().equals(channel))
            .findFirst()
            .map(ProductChannel::getIsEnabled)
            .orElse(false);
    }

    public Set<Channel> getEnabledChannels() {
        return productChannels.stream()
            .filter(ProductChannel::getIsEnabled)
            .map(ProductChannel::getChannel)
            .collect(Collectors.toSet());
    }

    public void removeFeatureMapping(ProductFeatureMapping mapping) {
        featureMappings.remove(mapping);
        mapping.setProduct(null);
    }

    public void addSellerProduct(SellerProduct sellerProduct) {
        sellerProducts.add(sellerProduct);
        sellerProduct.setProduct(this);
    }

    public void removeSellerProduct(SellerProduct sellerProduct) {
        sellerProducts.remove(sellerProduct);
        sellerProduct.setProduct(null);
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
    

}
