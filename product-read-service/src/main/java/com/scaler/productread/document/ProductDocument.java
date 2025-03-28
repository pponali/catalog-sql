package com.scaler.productread.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Elasticsearch document representation of a Product.
 * This is the main entity used for product search and querying.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "#{@indexNameProvider.getIndexName('elasticsearch.indices.product')}")
public class ProductDocument {
    
    @Id
    private UUID id;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String name;
    
    @Field(type = FieldType.Text)
    private String description;
    
    @Field(type = FieldType.Keyword)
    private String sku;
    
    @Field(type = FieldType.Keyword)
    private String brand;
    
    @Field(type = FieldType.Keyword)
    private Set<String> categories;
    
    @Field(type = FieldType.Nested)
    private List<CategoryInfo> categoryDetails;
    
    @Field(type = FieldType.Nested)
    private List<ProductFeature> features;
    
    @Field(type = FieldType.Nested)
    private List<SellerInfo> sellers;
    
    @Field(type = FieldType.Nested)
    private List<ChannelInfo> channels;
    
    @Field(type = FieldType.Object)
    private Map<String, Object> attributes;
    
    @Field(type = FieldType.Nested)
    private List<ImageInfo> images;
    
    @Field(type = FieldType.Boolean)
    private boolean active;
    
    @Field(type = FieldType.Double)
    private Double minPrice;
    
    @Field(type = FieldType.Double)
    private Double maxPrice;
    
    @Field(type = FieldType.Integer)
    private Integer totalStock;
    
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // Nested objects
    
    @Data
    @lombok.Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryInfo {
        @Field(type = FieldType.Keyword)
        private UUID id;
        
        @Field(type = FieldType.Text)
        private String name;
        
        @Field(type = FieldType.Keyword)
        private String path;
        
        @Field(type = FieldType.Integer)
        private Integer level;
    }
    
    @Data
    @lombok.Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductFeature {
        @Field(type = FieldType.Keyword)
        private UUID featureId;
        
        @Field(type = FieldType.Text)
        private String name;
        
        @Field(type = FieldType.Keyword)
        private String code;
        
        @Field(type = FieldType.Text)
        private String value;
        
        @Field(type = FieldType.Boolean)
        private boolean searchable;
        
        @Field(type = FieldType.Boolean)
        private boolean filterable;
    }
    
    @Data
    @lombok.Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SellerInfo {
        @Field(type = FieldType.Keyword)
        private UUID sellerId;
        
        @Field(type = FieldType.Text)
        private String name;
        
        @Field(type = FieldType.Double)
        private Double price;
        
        @Field(type = FieldType.Double)
        private Double salePrice;
        
        @Field(type = FieldType.Integer)
        private Integer inventory;
        
        @Field(type = FieldType.Keyword)
        private String currency;
        
        @Field(type = FieldType.Boolean)
        private boolean available;
    }
    
    @Data
    @lombok.Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChannelInfo {
        @Field(type = FieldType.Keyword)
        private UUID channelId;
        
        @Field(type = FieldType.Text)
        private String name;
        
        @Field(type = FieldType.Keyword)
        private String type;
        
        @Field(type = FieldType.Boolean)
        private boolean available;
    }
    
    @Data
    @lombok.Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageInfo {
        @Field(type = FieldType.Text)
        private String url;
        
        @Field(type = FieldType.Keyword)
        private String type;
        
        @Field(type = FieldType.Integer)
        private Integer sortOrder;
    }
}