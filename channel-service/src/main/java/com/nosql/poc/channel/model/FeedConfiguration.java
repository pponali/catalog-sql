package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

/**
 * Configuration for feed-based channel integration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedConfiguration {
    private String feedType; // XML, CSV, JSON
    private String feedFormat;
    private String feedDeliveryMethod; // FTP, API, S3
    private String feedSchedule;
    private Map<String, String> feedCredentials;
    private List<String> requiredFields;
}