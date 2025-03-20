package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Image requirements for product listing in a channel.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequirements {
    @NotNull(message = "Supported formats are required")
    private List<String> supportedFormats;
    
    @NotNull(message = "Minimum width is required")
    private Integer minWidth;
    
    @NotNull(message = "Minimum height is required")
    private Integer minHeight;
    
    @NotNull(message = "Maximum file size is required")
    private Integer maxFileSize;
    
    @NotNull(message = "Minimum number of images is required")
    private Integer minImages;
    
    private Integer maxImages;
    private List<String> requiredAngles;
    
    @Valid
    private Map<String, ImageSpec> imageSpecs;
}