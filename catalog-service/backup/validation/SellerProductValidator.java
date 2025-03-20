package com.scaler.validation;

import com.scaler.dto.SellerProductDTO;
import com.scaler.entity.SellerProduct;
import com.scaler.exception.ValidationException;
import com.scaler.repository.SellerProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SellerProductValidator {
    
    private final SellerProductRepository sellerProductRepository;
    
    public void validateCreate(SellerProductDTO dto) {
        List<String> errors = new ArrayList<>();
        
        // Validate price
        if (dto.getPrice() != null && dto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            errors.add("Price cannot be negative");
        }
        
        // Validate stock
        if (dto.getStock() != null && dto.getStock() < 0) {
            errors.add("Stock cannot be negative");
        }
        
        // Validate commission rate
        if (dto.getCommissionRate() != null) {
            if (dto.getCommissionRate().compareTo(BigDecimal.ZERO) < 0) {
                errors.add("Commission rate cannot be negative");
            }
            if (dto.getCommissionRate().compareTo(new BigDecimal("100")) > 0) {
                errors.add("Commission rate cannot be greater than 100%");
            }
        }
        
        // Check if relationship already exists
        Optional<SellerProduct> existing = sellerProductRepository.findByProductIdAndSellerIdAndMerchantId(
            dto.getProductId(),
            dto.getSellerId(),
            dto.getMerchantId()
        );
        if (existing.isPresent()) {
            errors.add("This product is already associated with the seller for this merchant");
        }
        
        // If seller is manufacturer, check if product already has a manufacturer
        if (Boolean.TRUE.equals(dto.getIsManufacturer())) {
            boolean hasManufacturer = sellerProductRepository.existsByProductIdAndSellerIdAndIsManufacturerTrue(
                dto.getProductId(),
                dto.getSellerId()
            );
            if (hasManufacturer) {
                errors.add("Product already has a manufacturer");
            }
        }
        
        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed");
        }
    }
    
    public void validateUpdate(SellerProductDTO dto, SellerProduct existing) {
        List<String> errors = new ArrayList<>();
        
        // Cannot change manufacturer status
        if (dto.getIsManufacturer() != null && !dto.getIsManufacturer().equals(existing.getIsManufacturer())) {
            errors.add("Cannot change manufacturer status of an existing relationship");
        }
        
        // Validate price changes
        if (dto.getPrice() != null) {
            if (dto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                errors.add("Price cannot be negative");
            }
            
            // Optional: Add business rule for maximum price change
            BigDecimal maxChange = existing.getPrice().multiply(new BigDecimal("0.50")); // 50% change limit
            if (dto.getPrice().subtract(existing.getPrice()).abs().compareTo(maxChange) > 0) {
                errors.add("Price change cannot exceed 50% of current price");
            }
        }
        
        // Validate stock changes
        if (dto.getStock() != null && dto.getStock() < 0) {
            errors.add("Stock cannot be negative");
        }
        
        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed", errors);
        }
    }
}
