package com.nosql.poc.catalog.dto;

import com.nosql.poc.catalog.model.ProductMedia;
import lombok.Data;
import java.util.Map;

@Data
public class MediaResponse {
    private String id;
    private String type;
    private String url;
    private String title;
    private String altText;
    private Integer displayOrder;
    private Map<String, Object> mediaAttributes;

    public static MediaResponse fromProductMedia(ProductMedia media) {
        if (media == null) return null;
        
        MediaResponse response = new MediaResponse();
        response.setId(media.getId());
        response.setType(media.getType());
        response.setUrl(media.getUrl());
        response.setTitle(media.getTitle());
        response.setAltText(media.getAltText());
        response.setDisplayOrder(media.getDisplayOrder());
        response.setMediaAttributes(media.getMediaAttributes());
        return response;
    }
}
