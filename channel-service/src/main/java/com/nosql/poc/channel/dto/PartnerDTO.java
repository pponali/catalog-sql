package com.nosql.poc.channel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

/**
 * Data Transfer Object for partner entities.
 * Renamed from MerchantDTO to align with microservice architecture.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDTO {
    private String id;
    private String name;
    private String code;
    private String type;
    private String status;
    private String contactEmail;
    private String contactPhone;
    private Map<String, Object> additionalDetails;
}