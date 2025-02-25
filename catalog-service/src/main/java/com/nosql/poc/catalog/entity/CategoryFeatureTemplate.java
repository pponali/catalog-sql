package com.nosql.poc.catalog.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Document(collection = "category_feature_template")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"category", "features"})
@EqualsAndHashCode(callSuper = true, exclude = {"category", "features"})
public class CategoryFeatureTemplate extends FeatureTemplate {
    private String code;
    private String name;
    private String description;
    private String attributeType;
    private String validationPattern = "";
    private String minValue = "";
    private String maxValue = "";
    private String allowedValues = "";
    private String defaultValue = "";
    private String featureType = "STRING";
}
