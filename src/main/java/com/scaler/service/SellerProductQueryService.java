package com.scaler.service;

import com.scaler.entity.Product;
import com.scaler.entity.Seller;
import com.scaler.entity.SellerProduct;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SellerProductQueryService {
    
    List<SellerProduct> getAllSellerProducts();
    
    Optional<SellerProduct> getSellerProductById(UUID id);
    
    List<SellerProduct> getSellerProductsByProductId(UUID productId);
    
    List<SellerProduct> getSellerProductsBySellerId(UUID sellerId);
    
    List<SellerProduct> getSellerProductsByMerchantId(UUID merchantId);
    
    Optional<SellerProduct> getSellerProductByProductAndSeller(UUID productId, UUID sellerId);
    
    Optional<SellerProduct> getSellerProductByProductSellerAndMerchant(UUID productId, UUID sellerId, UUID merchantId);
    
    List<Seller> getSellersByProductId(UUID productId);
    
    List<Product> getProductsBySellerId(UUID sellerId);
    
    List<Product> getProductsBySellerIdAndMerchantId(UUID sellerId, UUID merchantId);
}