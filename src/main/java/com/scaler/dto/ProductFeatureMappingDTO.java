package com.scaler.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProductFeatureMappingDTO extends BaseDTO {

    @NotNull(message = "Product ID is required")
    private UUID productId;

    @NotNull(message = "Feature ID is required")
    private UUID featureId;

    private Integer displayOrder;
    private Boolean visible;
    private Boolean enabled;
    private String metadata;

    @ToString.Exclude
    private ProductDTO product;

    @ToString.Exclude
    private ProductFeatureDTO feature;

    @ToString.Exclude
    private Set<ProductFeatureValueDTO> featureValueMappings = new HashSet<>();
}
