package com.scaler.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "feature_template")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class FeatureTemplate extends BaseEntity {

    @Column(name = "unit_id")
    private UUID unitId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "feature_type", nullable = false)
    private String featuretype;

    @Column(name = "data_type", nullable = false)
    private String datatype;

    @Column(name = "input_type", nullable = false)
    private String inputtype;

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

    @Column(name = "required")
    private Boolean required = false;

    @Column(name = "filterable")
    private Boolean filterable;

    @Column(name = "hidden")
    private Boolean hidden;

    @Column(name = "multi_valued")
    private Boolean multiValued = false;

    @Column(name = "searchable")
    private Boolean searchable = false;

    @Column(name = "comparable")
    private Boolean comparable = false;

    @Column(name = "visible")
    private Boolean visible = true;

    @Column(name = "editable")
    private Boolean editable = true;
}
