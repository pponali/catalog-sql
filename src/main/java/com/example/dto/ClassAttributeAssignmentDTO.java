package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassAttributeAssignmentDTO {
    private Long id;
    private String classificationAttribute;  // code of the attribute
    private String classificationClass;      // code of the class
    private String unit;
    private String attributeType;
    private boolean mandatory;
    private boolean multiValued;
    private Set<String> attributeValues = new HashSet<>();
}
