package com.scaler.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class UnitOfMeasureDTO {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private String baseUnit;
    private Double conversionFactor;
}
