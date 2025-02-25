package com.nosql.poc.catalog.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Document(collection = "classification_attribute")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClassificationAttribute extends BaseEntity {
    private String code;
    private String name;
    private String description;
    private String dataType;
    private boolean mandatory;
    private boolean multiValued;
    private Set<String> allowedValues = new HashSet<>();
}
