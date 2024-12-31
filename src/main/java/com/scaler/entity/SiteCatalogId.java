package com.scaler.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SiteCatalogId implements Serializable {
    @Column(name = "site_id")
    private UUID siteId;

    @Column(name = "catalog_id")
    private UUID catalogId;
}
