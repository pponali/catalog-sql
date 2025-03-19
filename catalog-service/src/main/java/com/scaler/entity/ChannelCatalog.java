package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "channel_catalog",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"channel_id", "catalog_id"})
    })
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"channel", "catalog"})
@EqualsAndHashCode(callSuper = true, exclude = {"channel", "catalog"})
public class ChannelCatalog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id", nullable = false)
    private Catalog catalog;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(name = "display_order")
    private Integer displayOrder;
}
