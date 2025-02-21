package com.scaler.service;

import com.scaler.entity.*;
import com.scaler.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryService {
    
    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductInventoryRepository productInventoryRepository;
    private final CatalogRepository catalogRepository;
    
    public List<Product> getProductsByMerchantAndChannel(Long merchantId, Long channelId) {
        return productRepository.findByMerchantAndChannel(merchantId, channelId);
    }
    
    public List<ProductPrice> getProductPricing(Long productId, Long channelId, Long lineOfBusinessId) {
        return null;
        //return productPriceRepository.findActiveProductPrices(productId, channelId, lineOfBusinessId);
    }
    
    public Optional<ProductInventory> getProductInventory(Long productId, Long merchantId, Long channelId) {
        return productInventoryRepository.findByProductMerchantAndChannel(productId, merchantId, channelId);
    }
    
    public Optional<Catalog> getCatalogByMerchantChannelAndLob(Long merchantId, Long channelId, Long lineOfBusinessId) {
        return catalogRepository.findByMerchantChannelAndLob(merchantId, channelId, lineOfBusinessId);
    }
}
