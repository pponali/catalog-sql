package com.nosql.poc.vendor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCommission {
    private String categoryId;
    private String categoryName;
    private BigDecimal baseCommission;
    private List<CommissionTier> categoryTiers;
    private Map<String, Object> categoryRules;
}