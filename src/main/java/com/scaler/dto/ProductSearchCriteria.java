package com.scaler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchCriteria {
    private UUID sellerId;
    private UUID merchantId;
    private UUID channelId;
    private UUID catalogId;
    private List<UUID> categoryIds;
    private String status;
    private String name;
    private String code;
    private String sku;
    private Double minPrice;
    private Double maxPrice;
    private Boolean isActive;
    private String productType;
    private List<String> tags;
    private String sortBy;
    private String sortDirection;
    private Integer page;
    private Integer size;
}
