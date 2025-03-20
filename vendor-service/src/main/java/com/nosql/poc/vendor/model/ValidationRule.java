package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.Map;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationRule {
    private String ruleId;
    private String ruleName;
    private String ruleType;
    private String entityType;
    private String expression;
    private String errorMessage;
    private Integer severity;
    private Boolean active;
    private List<String> targetFields;
    private Map<String, Object> parameters;
}