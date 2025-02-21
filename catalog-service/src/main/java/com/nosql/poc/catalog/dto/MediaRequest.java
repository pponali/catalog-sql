package com.nosql.poc.catalog.dto;

import com.nosql.poc.catalog.model.ProductMedia;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.Map;

@Data
public class MediaRequest {
    @NotBlank(message = "Media type is required")
    private String type;

    @NotBlank(message = "Media URL is required")
    private String url;

    private String title;
    
    private String altText;
    
    private Integer displayOrder;
    
    private Map<String, Object> mediaAttributes;

    public ProductMedia toProductMedia() {
        ProductMedia media = new ProductMedia();
        media.setType(this.type);
        media.setUrl(this.url);
        media.setTitle(this.title);
        media.setAltText(this.altText);
        media.setDisplayOrder(this.displayOrder);
        media.setMediaAttributes(this.mediaAttributes);
        return media;
    }
}
