package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "store")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"merchant", "storeCatalogs"})
@EqualsAndHashCode(callSuper = true, exclude = {"merchant", "storeCatalogs"})
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

    @Column(name = "store_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private StoreType storeType;

    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Merchant merchant;

    @Column(name = "metadata", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> metadata;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private Set<StoreCatalog> storeCatalogs = new HashSet<>();

    public void addSiteCatalog(StoreCatalog storeCatalog) {
        storeCatalogs.add(storeCatalog);
        storeCatalog.setStore(this);
    }

    public void removeSiteCatalog(StoreCatalog storeCatalog) {
        storeCatalogs.remove(storeCatalog);
        storeCatalog.setStore(null);
    }
}
