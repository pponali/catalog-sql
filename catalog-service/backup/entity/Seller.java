package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Entity
@Table(name = "sellers")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"sellerProducts"})
@EqualsAndHashCode(callSuper = true, exclude = {"sellerProducts"})
public class Seller extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String status;

    @Column(name = "merchant_id", nullable = false)
    private UUID merchantId;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(length = 500)
    private String address;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SellerProduct> sellerProducts = new HashSet<>();

    public void addSellerProduct(SellerProduct sellerProduct) {
        sellerProducts.add(sellerProduct);
        sellerProduct.setSeller(this);
    }

    public void removeSellerProduct(SellerProduct sellerProduct) {
        sellerProducts.remove(sellerProduct);
        sellerProduct.setSeller(null);
    }

    @ManyToMany
    @JoinTable(
        name = "seller_catalogs",
        joinColumns = @JoinColumn(name = "seller_id"),
        inverseJoinColumns = @JoinColumn(name = "catalog_id")
    )
    private List<Catalog> catalogs = new ArrayList<>();
}
