package com.nosql.poc.vendor.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelConfiguration {
    private String integrationMode;
    private Map<String, String> apiCredentials;
    private Map<String, String> endpoints;
    private Map<String, Object> channelSettings;
    private List<String> supportedFeatures;
    private Map<String, ValidationRule> validationRules;
}