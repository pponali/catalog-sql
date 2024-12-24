package com.scaler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationAttributeDTO {
    private Long id;
    private String code;
    private String name;
    private boolean required;
    private boolean multiValued;
}
