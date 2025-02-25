package com.nosql.poc.catalog.entity;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "product_feature_value")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true, exclude = {"product", "feature"})
@EqualsAndHashCode(callSuper = true, exclude = {"product", "feature"})
public class ProductFeatureValue extends BaseEntity {

    private Product product;
    private ProductFeature feature;
    private UUID templateId;
    private String type;
    private String unit;
    private String unitOfMeasure;
    private String status;
    private String validationStatus;
    private String validationPattern;
    private JsonNode value;
    private LocalDateTime lastUpdated;
}
