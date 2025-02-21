package com.nosql.poc.catalog.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Document(collection = "classification_class")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClassificationClass extends BaseEntity {
    private String code;
    private String name;
    private String description;
    private Set<ClassAttributeAssignment> attributeAssignments = new HashSet<>();
}
