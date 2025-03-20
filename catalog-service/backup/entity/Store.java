package com.scaler.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "store")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true, exclude = {"merchant", "storeCatalogs"})
@EqualsAndHashCode(callSuper = true, exclude = {"merchant", "storeCatalogs"})
public class Store extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

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
    private final Set<StoreCatalog> storeCatalogs = new HashSet<>();

    // Channel relationships removed as part of microservice separation

    public Store() {
        super();
    }

    public void addSiteCatalog(StoreCatalog storeCatalog) {
        storeCatalogs.add(storeCatalog);
        storeCatalog.setStore(this);
    }

    public void removeSiteCatalog(StoreCatalog storeCatalog) {
        storeCatalogs.remove(storeCatalog);
        storeCatalog.setStore(null);
    }

    public void addChannel(Channel channel) {
        channels.add(channel);
        channel.setStore(this);
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
        channel.setStore(null);
    }
}
