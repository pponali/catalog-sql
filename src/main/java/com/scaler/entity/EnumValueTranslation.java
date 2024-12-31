package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "enum_value_translations")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EnumValueTranslation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enum_value_id", nullable = false)
    private EnumValue enumValue;

    @Column(name = "languageCode", nullable = false)
    private String languageCode;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;
}
