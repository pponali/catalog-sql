package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

import com.example.converter.JsonConverter;

@Entity
@Table(name = "category_feature_templates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class CategoryFeatureTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "attribute_type", nullable = false)
    private String attributeType;

    @Column(name = "validation_pattern")
    private String validationPattern;

    @Column(name = "min_value")
    private Double minValue;

    @Column(name = "max_value")
    private Double maxValue;

    @Column(name = "allowed_values")
    private String allowedValues;

    @Column(name = "unit")
    private String unit;

    @Column(name = "visible")
    private boolean visible = true;

    @Column(name = "editable")
    private boolean editable = true;

    @Column(name = "searchable")
    private boolean searchable = true;

    @Column(name = "comparable")
    private boolean comparable = true;

    @Column(name = "mandatory")
    private boolean mandatory = false;

    @Column(name = "multi_valued")
    private boolean multiValued = false;

    @Column(name = "metadata")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> metadata;
}
