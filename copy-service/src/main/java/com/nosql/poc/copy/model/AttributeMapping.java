package com.nosql.poc.copy.model;

import lombok.Data;

@Data
public class AttributeMapping {
    private String sourceAttribute;
    private String targetAttribute;
    private String defaultValue;
    private boolean required;
    private String format;
    private String transformationRule;
}
