package com.nosql.poc.channel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Represents media associated with a product (images, videos, documents).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductMedia {
    
    /**
     * Unique identifier for the media.
     */
    private String id;
    
    /**
     * Type of media (image, video, document, etc).
     */
    private MediaType type;
    
    /**
     * URL or path to the media resource.
     */
    private String url;
    
    /**
     * Content type/MIME type of the media.
     */
    private String contentType;
    
    /**
     * Order/sequence number for display.
     */
    private Integer sequence;
    
    /**
     * Alt text or description of the media.
     */
    private String altText;
    
    /**
     * Purpose or context of the media (main, thumbnail, lifestyle, etc).
     */
    private String purpose;
    
    /**
     * Width of the media in pixels (for images/videos).
     */
    private Integer width;
    
    /**
     * Height of the media in pixels (for images/videos).
     */
    private Integer height;
    
    /**
     * Additional attributes as key-value pairs.
     */
    private Map<String, Object> attributes;
    
    /**
     * Types of product media.
     */
    public enum MediaType {
        /**
         * Image media.
         */
        IMAGE,
        
        /**
         * Video media.
         */
        VIDEO,
        
        /**
         * Document (PDF, specification, etc).
         */
        DOCUMENT,
        
        /**
         * 3D model.
         */
        MODEL_3D,
        
        /**
         * Audio file.
         */
        AUDIO
    }
    
    /**
     * Check if the media is an image.
     * 
     * @return true if the media is an image
     */
    public boolean isImage() {
        return type == MediaType.IMAGE;
    }
    
    /**
     * Check if the media is a video.
     * 
     * @return true if the media is a video
     */
    public boolean isVideo() {
        return type == MediaType.VIDEO;
    }
}