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
public class FeatureValueDTO {
    private UUID id;
    private UUID featureId;
    private String featureName;
    private String featureCode;
    private UUID templateId;
    private String templateName;
    private String value;
    private String displayValue;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}