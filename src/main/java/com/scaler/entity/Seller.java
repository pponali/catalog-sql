package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sellers")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
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

    @ManyToMany
    @JoinTable(
        name = "seller_products",
        joinColumns = @JoinColumn(name = "seller_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "seller_catalogs",
        joinColumns = @JoinColumn(name = "seller_id"),
        inverseJoinColumns = @JoinColumn(name = "catalog_id")
    )
    @Builder.Default
    private List<Catalog> catalogs = new ArrayList<>();
}
