package com.scaler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class BaseDTO {
    private UUID id;
    private String createdDate;
    private String lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;
}
