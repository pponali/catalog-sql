package com.nosql.poc.channel.model;

import lombok.Data;

@Data
public class ValidationRule {
    private String name;
    private String type; // REQUIRED_FIELD, FORMAT, RANGE, CUSTOM
    private String field;
    private String condition;
    private String errorMessage;
    private String severity; // ERROR, WARNING
    private int priority;
    private Map<String, Object> parameters;
}
