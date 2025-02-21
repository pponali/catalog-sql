package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "store")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"business", "siteCatalogs"})
@EqualsAndHashCode(callSuper = true, exclude = {"business", "siteCatalogs"})
public class Store extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "domain", nullable = false)
    private String domain;

    @Column(name = "locale", nullable = false)
    private String locale;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "description")
    private String description;

    @Column(name = "active")
    private boolean active;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Merchant merchant;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private Set<SiteCatalog> siteCatalogs = new HashSet<>();

    public void addSiteCatalog(SiteCatalog siteCatalog) {
        siteCatalogs.add(siteCatalog);
        siteCatalog.setStore(this);
    }

    public void removeSiteCatalog(SiteCatalog siteCatalog) {
        siteCatalogs.remove(siteCatalog);
        siteCatalog.setStore(null);
    }
}
