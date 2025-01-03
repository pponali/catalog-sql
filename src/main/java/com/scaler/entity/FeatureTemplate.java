package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "feature_template")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class FeatureTemplate extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "feature_type", nullable = false)
    private String featureType;

    @Column(name = "data_type", nullable = false)
    private String dataType;

    @Column(name = "validation_pattern")
    private String validationPattern;

    @Column(name = "min_value")
    private String minValue;

    @Column(name = "max_value")
    private String maxValue;

    @Column(name = "step")
    private String step;

    @Column(name = "allowed_values")
    private String allowedValues;

    @Column(name = "input_type")
    private String inputType;

    @Column(name = "required")
    private Boolean required = false;

    @Column(name = "searchable")
    private Boolean searchable = false;

    @Column(name = "filterable")
    private Boolean filterable = false;

    @Column(name = "comparable")
    private Boolean comparable = false;

    @Column(name = "hidden")
    private Boolean hidden = false;

    @Column(name = "default_value")
    private String defaultValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CategoryFeatureTemplate> categoryFeatureTemplates = new HashSet<>();
}
