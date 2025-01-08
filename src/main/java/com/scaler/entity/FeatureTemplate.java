package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "feature_template")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "template_type")
@EqualsAndHashCode(callSuper = true)
public class FeatureTemplate extends BaseEntity {

    @Column(name = "template_type", insertable = false, updatable = false)
    private String templateType;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "feature_type", nullable = false)
    private String featureType;

    @Column(name = "data_type", nullable = false)
    private String dataType;

    @Column(name = "input_type", nullable = false)
    private String inputType;

    @Column(name = "validation_pattern")
    private String validationPattern;

    @Column(name = "min_value")
    private String minValue;

    @Column(name = "max_value")
    private String maxValue;

    @Column(name = "allowed_values")
    private String allowedValues;

    @Column(name = "default_value")
    private String defaultValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private UnitOfMeasure unit;

    @Column(name = "required")
    private Boolean required = false;

    @Column(name = "filterable")
    private Boolean filterable = true;

    @Column(name = "hidden")
    private Boolean hidden = false;

    @Column(name = "multi_valued")
    private Boolean multiValued = false;

    @Column(name = "searchable")
    private Boolean searchable = true;

    @Column(name = "comparable")
    private Boolean comparable = true;

    @Column(name = "visible")
    private Boolean visible = true;

    @Column(name = "editable")
    private Boolean editable = true;
}
