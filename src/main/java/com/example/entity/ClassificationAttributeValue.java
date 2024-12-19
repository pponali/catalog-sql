package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "classification_attribute_values")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationAttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classification_attribute_id")
    private ClassificationAttribute classificationAttribute;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "display_value")
    private String displayValue;

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "is_active")
    private Boolean isActive;
}
