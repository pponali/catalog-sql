package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Entity
@Table(name = "enum_value")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(exclude = {"translations"})
public class EnumValue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "enum_type", nullable = false)
    private String enumType;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "enumValue", cascade = CascadeType.ALL)
    private Set<EnumValueTranslation> translations = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnumValue that)) return false;
        return code != null && code.equals(that.getCode());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
