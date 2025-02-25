package com.nosql.poc.catalog.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "product_feature")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"product", "template", "featureValues"})
@ToString(callSuper = true, exclude = {"product", "template", "featureValues"})
public class ProductFeature extends BaseEntity {
    private String code;
    private String name;
    private String description;
    private String attributeType;
    private String validationPattern;
    private String minValue;
    private String maxValue;
    private String allowedValues;
    private String defaultValue;
    private String featureType = "STRING";
    private UnitOfMeasure unitOfMeasure;
}
