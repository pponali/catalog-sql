package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

import com.scaler.builder.MerchantBuilder;

@Entity
@Table(name = "merchant")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"catalogs"})
@EqualsAndHashCode(callSuper = true, exclude = {"catalogs"})
public class Merchant extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true)
    private String code;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "status")
    private String status;

    @Column(name = "contact_email")
    private String contactEmail;
    
    @ManyToMany(mappedBy = "merchants")
    private Set<Catalog> catalogs = new HashSet<>();

    public Set<Catalog> getCatalogs() {
        return catalogs;
    }
}
