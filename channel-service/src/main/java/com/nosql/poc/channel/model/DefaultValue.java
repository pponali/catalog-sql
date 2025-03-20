package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

/**
 * Represents default value configuration for an attribute in a channel mapping.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultValue {
    private String value;
    private String condition;
    private String applicability;
    private Map<String, Object> rules;
}