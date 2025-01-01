package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "business")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"catalogs", "sites"})
@EqualsAndHashCode(callSuper = true, exclude = {"catalogs", "sites"})
public class Business extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private List<Catalog> catalogs = new ArrayList<>();

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
    private List<Site> sites = new ArrayList<>();

    public void addCatalog(Catalog catalog) {
        catalogs.add(catalog);
        catalog.setBusiness(this);
    }

    public void removeCatalog(Catalog catalog) {
        catalogs.remove(catalog);
        catalog.setBusiness(null);
    }

    public void addSite(Site site) {
        sites.add(site);
        site.setBusiness(this);
    }

    public void removeSite(Site site) {
        sites.remove(site);
        site.setBusiness(null);
    }
}
