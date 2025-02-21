package com.nosql.poc.catalog.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Document(collection = "class_attribute")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ClassAttributeAssignment {
    private Long id;
    private ClassificationClass classificationClass;
    private ClassificationAttribute classificationAttribute;
    private String unit;
    private String attributeType;
    private boolean mandatory;
    private boolean multiValued;
    private Set<String> attributeValues = new HashSet<>();
    private Integer sequence;
}
