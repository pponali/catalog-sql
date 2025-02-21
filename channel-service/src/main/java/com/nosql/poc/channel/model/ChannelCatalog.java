package com.nosql.poc.channel.model;

import com.nosql.poc.channel.validation.constraints.ChannelType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "channel_catalogs")
public class ChannelCatalog {
    @Id
    private String id;
    
    @NotBlank(message = "Channel ID is required")
    private String channelId;
    
    @NotBlank(message = "Channel name is required")
    private String channelName;
    
    @NotNull(message = "Channel type is required")
    @ChannelType
    private String channelType;
    
    @Valid
    @NotNull(message = "Channel configuration is required")
    private CatalogConfiguration configuration;
    
    @Valid
    private List<CategoryMapping> categoryMappings;
    
    @Valid
    private List<AttributeMapping> attributeMappings;
    
    @Valid
    private ValidationRules validationRules;
    
    @Valid
    private TransformationRules transformationRules;
    
    @NotNull
    private List<String> supportedProductTypes;
    
    private Map<String, Object> channelAttributes;
}
