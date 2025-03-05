package com.scaler.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFeatureValueMappingDTO {
    private UUID id;
    private UUID productId;
    private String productName;
    private UUID featureId;
    private String featureName;
    private String featureCode;
    private UUID featureValueId;
    private String featureValueDisplayValue;
    private UUID templateId;
    private String templateName;
    private Boolean isActive;
    private Integer displayOrder;
    private String status;
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}