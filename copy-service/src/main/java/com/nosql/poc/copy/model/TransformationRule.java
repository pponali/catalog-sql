package com.nosql.poc.copy.model;

import lombok.Data;

@Data
public class TransformationRule {
    private String type; // CONCAT, SPLIT, FORMAT, CUSTOM
    private String expression;
    private Map<String, Object> parameters;
    private String customTransformerClass;
}
