package com.scaler.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeDTO extends BaseDTO {
    private UUID productId;
    private String attributeName;
    private String attributeValue;
    private String unitOfMeasure;
}
