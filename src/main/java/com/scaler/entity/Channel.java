package com.scaler.entity;

import com.scaler.entity.enums.ChannelType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.ArrayList;
import java.util.List;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "channel")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"store", "platforms"})
@EqualsAndHashCode(callSuper = true, exclude = {"store", "platforms"})
public class Channel extends BaseEntity {
    
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "status")
    private String status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ChannelType type;

    @Column(name = "metadata", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String metadata;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "visibility")
    private Boolean visibility;
    


    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Platform> platforms = new HashSet<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "channel_config", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String channelConfig;

    @PrePersist
    protected void onCreate() {
        if (enabled == null) enabled = true;
        if (visibility == null) visibility = true;
        if (displayOrder == null) displayOrder = 0;
    }
}
