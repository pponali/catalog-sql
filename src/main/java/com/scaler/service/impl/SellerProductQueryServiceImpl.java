package com.scaler.service.impl;

import com.scaler.entity.Product;
import com.scaler.entity.Seller;
import com.scaler.entity.SellerProduct;
import com.scaler.repository.SellerProductRepository;
import com.scaler.service.SellerProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerProductQueryServiceImpl implements SellerProductQueryService {

    private final SellerProductRepository sellerProductRepository;

    @Override
    public List<SellerProduct> getAllSellerProducts() {
        return sellerProductRepository.findAll();
    }

    @Override
    public Optional<SellerProduct> getSellerProductById(UUID id) {
        return sellerProductRepository.findById(id);
    }

    @Override
    public List<SellerProduct> getSellerProductsByProductId(UUID productId) {
        return sellerProductRepository.findByProductId(productId);
    }

    @Override
    public List<SellerProduct> getSellerProductsBySellerId(UUID sellerId) {
        return sellerProductRepository.findBySellerId(sellerId);
    }

    @Override
    public List<SellerProduct> getSellerProductsByMerchantId(UUID merchantId) {
        return sellerProductRepository.findByMerchantId(merchantId);
    }

    @Override
    public Optional<SellerProduct> getSellerProductByProductAndSeller(UUID productId, UUID sellerId) {
        return sellerProductRepository.findByProductIdAndSellerId(productId, sellerId);
    }

    @Override
    public Optional<SellerProduct> getSellerProductByProductSellerAndMerchant(UUID productId, UUID sellerId, UUID merchantId) {
        return sellerProductRepository.findByProductIdAndSellerIdAndMerchantId(productId, sellerId, merchantId);
    }

    @Override
    public List<Seller> getSellersByProductId(UUID productId) {
        return sellerProductRepository.findByProductId(productId).stream()
                .map(SellerProduct::getSeller)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsBySellerId(UUID sellerId) {
        return sellerProductRepository.findBySellerId(sellerId).stream()
                .map(SellerProduct::getProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProductsBySellerIdAndMerchantId(UUID sellerId, UUID merchantId) {
        return sellerProductRepository.findBySellerIdAndMerchantId(sellerId, merchantId).stream()
                .map(SellerProduct::getProduct)
                .collect(Collectors.toList());
    }
}