package com.nosql.poc.vendor.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class CommissionStructure {
    private String structureId;
    
    private String structureType; // FIXED, TIERED, CATEGORY_BASED
    
    private LocalDateTime effectiveFrom;
    
    private LocalDateTime effectiveTo;
    
    private List<CommissionTier> commissionTiers;
    
    private Map<String, CategoryCommission> categoryCommissions;
    
    private List<CommissionDeduction> deductions;
    
    private Map<String, Object> additionalTerms;
}

@Data
public class CommissionTier {
    private BigDecimal fromAmount;
    private BigDecimal toAmount;
    private BigDecimal commissionPercentage;
    private String applicability; // PER_ORDER, MONTHLY_GMV
    private Map<String, Object> conditions;
}

@Data
public class CategoryCommission {
    private String categoryId;
    private String categoryName;
    private BigDecimal baseCommission;
    private List<CommissionTier> categoryTiers;
    private Map<String, Object> categoryRules;
}

@Data
public class CommissionDeduction {
    private String deductionType;
    private String calculationBasis;
    private BigDecimal value;
    private String applicability;
    private Map<String, Object> conditions;
}
