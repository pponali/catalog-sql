package com.scaler.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_feature_value")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"product", "feature"})
@EqualsAndHashCode(callSuper = true, exclude = {"product", "feature"})
public class ProductFeatureValue extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", referencedColumnName = "id")
    private ProductFeature feature;

    @Column(name = "template_id")
    private UUID templateId;

    @Column(name = "type")
    private String type;

    @Column(name = "unit")
    private String unit;

    @Column(name = "unit_of_measure")
    private String unitOfMeasure;

    @Column(name = "status")
    private String status;

    @Column(name = "validation_status")
    private String validationStatus;

    @Column(name = "validation_pattern")
    private String validationPattern;

    @Column(name = "validation_message")
    private String validationMessage;

    @Column(name = "string_value")
    private String stringValue;

    @Column(name = "numeric_value")
    private Double numericValue;

    @Column(name = "boolean_value")
    private Boolean booleanValue;

    @Column(name = "attribute_values", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode attributeValues;

    @Column(name = "metadata", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode metadata;

    @PrePersist
    protected void onCreate() {
        setCreatedDate(LocalDateTime.now());
        setLastModifiedDate(LocalDateTime.now());
    }

    @PreUpdate
    protected void onUpdate() {
        setLastModifiedDate(LocalDateTime.now());
    }

    public String getValueAsString() {
        if (attributeValues != null) {
            return attributeValues.toString();
        }
        return null;
    }

    public void setAttributeValue(JsonNode value) {
        this.attributeValues = value;
        if (value != null) {
            if (value.isTextual()) {
                this.stringValue = value.asText();
            } else if (value.isNumber()) {
                this.numericValue = value.asDouble();
            } else if (value.isBoolean()) {
                this.booleanValue = value.asBoolean();
            }
        }
    }

    public void setValidationStatus(String status) {
        this.validationStatus = status;
    }

    public void setValidationMessage(String message) {
        this.validationMessage = message;
    }
}
