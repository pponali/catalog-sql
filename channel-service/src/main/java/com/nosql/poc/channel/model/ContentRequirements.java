package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Content requirements for product listing in a channel.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentRequirements {
    @NotNull(message = "Minimum title length is required")
    private Integer minTitleLength;
    
    @NotNull(message = "Maximum title length is required")
    private Integer maxTitleLength;
    
    @NotNull(message = "Minimum description length is required")
    private Integer minDescriptionLength;
    
    @NotNull(message = "Maximum description length is required")
    private Integer maxDescriptionLength;
    
    @NotNull(message = "Mandatory attributes list is required")
    private List<String> mandatoryAttributes;
    
    private List<String> optionalAttributes;
    private Map<String, String> formatRequirements;
}