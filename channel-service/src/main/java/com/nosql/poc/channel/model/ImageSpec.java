package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Specification for an image type in a channel.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageSpec {
    private String type; // PRIMARY, SECONDARY, THUMBNAIL
    private Integer width;
    private Integer height;
    private String format;
    private Integer quality;
    private Boolean required;
}