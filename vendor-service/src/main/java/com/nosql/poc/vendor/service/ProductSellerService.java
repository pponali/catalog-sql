package com.nosql.poc.vendor.service;

import com.nosql.poc.vendor.model.ProductSeller;
import com.nosql.poc.vendor.model.SellerType;
import com.nosql.poc.vendor.repository.ProductSellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductSellerService {

    private final ProductSellerRepository productSellerRepository;

    @Transactional
    public ProductSeller addSellerToProduct(ProductSeller productSeller) {
        // Validate if a primary seller already exists when adding a new primary seller
        if (productSeller.getSellerType() == SellerType.PRIMARY) {
            List<ProductSeller> existingPrimarySellers = productSellerRepository
                .findByProductIdAndSellerType(productSeller.getProductId(), SellerType.PRIMARY);
            
            if (!existingPrimarySellers.isEmpty()) {
                throw new ValidationException("A primary seller already exists for this product");
            }
        }

        productSeller.setListingDate(LocalDateTime.now());
        productSeller.setLastUpdated(LocalDateTime.now());
        productSeller.setIsActive(true);
        
        return productSellerRepository.save(productSeller);
    }

    public List<ProductSeller> getProductSellers(String productId) {
        return productSellerRepository.findByProductId(productId);
    }

    public List<ProductSeller> getActiveProductSellers(String productId) {
        return productSellerRepository.findActiveSellersByProductId(productId);
    }

    public Optional<ProductSeller> getPrimarySeller(String productId) {
        List<ProductSeller> primarySellers = productSellerRepository
            .findActiveSellersByProductIdAndType(productId, SellerType.PRIMARY);
        return primarySellers.isEmpty() ? Optional.empty() : Optional.of(primarySellers.get(0));
    }

    public List<ProductSeller> getSecondarySellers(String productId) {
        return productSellerRepository
            .findActiveSellersByProductIdAndType(productId, SellerType.SECONDARY);
    }

    @Transactional
    public void deactivateSeller(String productId, String vendorId) {
        Optional<ProductSeller> productSeller = productSellerRepository
            .findByProductIdAndVendorId(productId, vendorId);
            
        productSeller.ifPresent(seller -> {
            seller.setIsActive(false);
            seller.setLastUpdated(LocalDateTime.now());
            productSellerRepository.save(seller);
        });
    }

    @Transactional
    public ProductSeller updateSellerInfo(String productId, String vendorId, ProductSeller updatedInfo) {
        return productSellerRepository
            .findByProductIdAndVendorId(productId, vendorId)
            .map(existing -> {
                existing.setSellingPrice(updatedInfo.getSellingPrice());
                existing.setStockQuantity(updatedInfo.getStockQuantity());
                existing.setFulfillmentType(updatedInfo.getFulfillmentType());
                existing.setProcessingTime(updatedInfo.getProcessingTime());
                existing.setShippingCharge(updatedInfo.getShippingCharge());
                existing.setMinOrderQuantity(updatedInfo.getMinOrderQuantity());
                existing.setMaxOrderQuantity(updatedInfo.getMaxOrderQuantity());
                existing.setAllowPartialFulfillment(updatedInfo.getAllowPartialFulfillment());
                existing.setLastUpdated(LocalDateTime.now());
                return productSellerRepository.save(existing);
            })
            .orElseThrow(() -> new ValidationException("Seller not found for this product"));
    }
}
