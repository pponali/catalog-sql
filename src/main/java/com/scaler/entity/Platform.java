package com.scaler.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.scaler.entity.enums.PlatformType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "platform")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Platform extends BaseEntity {

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PlatformType type;

    @Column(name = "description")
    private String description;

    @Column(name = "version")
    private String version;

    @Column(name = "min_version")
    private String minVersion;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(name = "metadata", columnDefinition = "jsonb")
    private JsonNode metadata;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;
}
