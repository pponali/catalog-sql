package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enum_value")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EnumValue extends BaseEntity {

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "enum_type", nullable = false)
    private String enumType;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "enumValue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnumValueTranslation> translations = new ArrayList<>();
}
