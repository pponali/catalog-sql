package com.example.entity;

import com.example.validation.ProductFeatureValueValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "product_feature_value")
@Data
@EqualsAndHashCode(exclude = "feature")
@ToString(exclude = "feature")
@ProductFeatureValueValidator
public class ProductFeatureValue {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id")
    @NotNull
    private ProductFeature feature;

    @Column(name = "string_value")
    private String stringValue;

    @Column(name = "numeric_value")
    private Double numericValue;

    @Column(name = "boolean_value")
    private Boolean booleanValue;

    @Column(name = "attribute_value", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode attributeValue;

    private String unit;

    public void setFeature(ProductFeature feature) {
        this.feature = feature;
    }

    public String getValue() {
        if (stringValue != null) return stringValue;
        if (numericValue != null) return numericValue.toString();
        if (booleanValue != null) return booleanValue.toString();
        return attributeValue != null ? attributeValue.toString() : null;
    }

    public void setValue(String value) {
        if (feature == null || feature.getTemplate() == null) {
            this.stringValue = value;
            return;
        }

        String type = feature.getTemplate().getAttributeType().toLowerCase();
        try {
            switch (type) {
                case "string":
                    this.stringValue = value;
                    break;
                case "numeric":
                    this.numericValue = Double.parseDouble(value);
                    break;
                case "boolean":
                    this.booleanValue = Boolean.parseBoolean(value);
                    break;
                case "json":
                    this.attributeValue = OBJECT_MAPPER.readTree(value);
                    break;
                default:
                    this.stringValue = value;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid value for type " + type + ": " + value, e);
        }
    }

    public void setAttributeValue(String jsonValue) {
        try {
            this.attributeValue = OBJECT_MAPPER.readTree(jsonValue);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON value: " + jsonValue, e);
        }
    }

    public String getAttributeValueAsString() {
        return attributeValue != null ? attributeValue.toString() : null;
    }
}
