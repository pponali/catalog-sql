package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.Builder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Builder;

@Entity
@Table(name = "product")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true, exclude = {"catalog", "productCategories", "merchant", "channels", "sellerProducts", "productPlatforms", "featureValueMappings"})
@EqualsAndHashCode(callSuper = true, exclude = {"catalog", "productCategories", "merchant", "channels", "sellerProducts", "productPlatforms", "featureValueMappings"})
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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference("product-features")
    @JsonIgnoreProperties({"product"})
    private Set<ProductFeatureValueMapping> featureValueMappings = new HashSet<>();

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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductPlatform> productPlatforms = new HashSet<>();

    public void addProductCategory(ProductCategory productCategory) {
        if (productCategories == null) {
            productCategories = new HashSet<>();
        }
        productCategories.add(productCategory);
        productCategory.setProduct(this);
    }

    public void removeProductCategory(ProductCategory productCategory) {
        if (productCategories != null) {
            productCategories.remove(productCategory);
            productCategory.setProduct(null);
        }
    }

    public void addFeatureValueMapping(ProductFeatureValueMapping mapping) {
        if (featureValueMappings == null) {
            featureValueMappings = new HashSet<>();
        }
        featureValueMappings.add(mapping);
        mapping.setProduct(this);
    }

    public void addProductChannel(Channel channel) {
        ProductChannel productChannel = ProductChannel.builder()
                .product(this)
                .channel(channel)
                .isEnabled(true)
                .isVisible(true)
                .createdBy("SYSTEM")
                .effectiveFrom(LocalDateTime.now())
                .build();
        if (productChannels == null) {
            productChannels = new HashSet<>();
        }
        productChannels.add(productChannel);
    }

    public void removeProductChannel(Channel channel) {
        if (productChannels != null) {
            productChannels.removeIf(pc -> pc.getChannel().equals(channel));
        }
    }

    public void disableProductChannel(Channel channel) {
        if (productChannels != null) {
            productChannels.stream()
                    .filter(pc -> pc.getChannel().equals(channel))
                    .findFirst()
                    .ifPresent(pc -> pc.setIsEnabled(false));
        }
    }

    public void enableProductChannel(Channel channel) {
        if (productChannels != null) {
            productChannels.stream()
                    .filter(pc -> pc.getChannel().equals(channel))
                    .findFirst()
                    .ifPresent(pc -> pc.setIsEnabled(true));
        }
    }

    public boolean isEnabledForChannel(Channel channel) {
        if (productChannels == null) {
            return false;
        }
        return productChannels.stream()
                .filter(pc -> pc.getChannel().equals(channel))
                .findFirst()
                .map(ProductChannel::getIsEnabled)
                .orElse(false);
    }

    /*public Set<Channel> getEnabledChannels() {
        return productChannels.stream()
                .filter(ProductChannel::getIsEnabled)
                .map(ProductChannel::getChannel)
                .collect(Collectors.toSet());
    }*/

    public void removeFeatureValueMapping(ProductFeatureValueMapping mapping) {
        if (featureValueMappings != null) {
            featureValueMappings.remove(mapping);
            mapping.setProduct(null);
        }
    }

    public void addSellerProduct(SellerProduct sellerProduct) {
        if (sellerProducts == null) {
            sellerProducts = new HashSet<>();
        }
        sellerProducts.add(sellerProduct);
        sellerProduct.setProduct(this);
    }

    public Product() {
        super();
    }

    public void removeSellerProduct(SellerProduct sellerProduct) {
        if (sellerProducts != null) {
            sellerProducts.remove(sellerProduct);
            sellerProduct.setProduct(null);
        }
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public void addProductPlatform(Platform platform) {
        if (productPlatforms == null) {
            productPlatforms = new HashSet<>();
        }
        ProductPlatform productPlatform = ProductPlatform.builder()
                .product(this)
                .platform(platform)
                .isActive(true)
                .displayOrder(productPlatforms.size() + 1)
                .status("ACTIVE")
                .build();
        productPlatforms.add(productPlatform);
    }

    public void removeProductPlatform(Platform platform) {
        if (productPlatforms != null) {
            productPlatforms.removeIf(pp -> pp.getPlatform().equals(platform));
        }
    }

    public void deactivateProductPlatform(Platform platform) {
        if (productPlatforms != null) {
            productPlatforms.stream()
                    .filter(pp -> pp.getPlatform().equals(platform))
                    .findFirst()
                    .ifPresent(pp -> {
                        pp.setIsActive(false);
                        pp.setStatus("INACTIVE");
                    });
        }
    }

    public void activateProductPlatform(Platform platform) {
        if (productPlatforms != null) {
            productPlatforms.stream()
                    .filter(pp -> pp.getPlatform().equals(platform))
                    .findFirst()
                    .ifPresent(pp -> {
                        pp.setIsActive(true);
                        pp.setStatus("ACTIVE");
                    });
        }
    }


}
