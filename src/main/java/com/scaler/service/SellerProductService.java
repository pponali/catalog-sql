package com.scaler.service;

import com.scaler.dto.SellerProductDTO;
import com.scaler.entity.Merchant;
import com.scaler.entity.Product;
import com.scaler.entity.Seller;
import com.scaler.entity.SellerProduct;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.mapper.SellerProductMapper;
import com.scaler.repository.MerchantRepository;
import com.scaler.repository.ProductRepository;
import com.scaler.repository.SellerProductRepository;
import com.scaler.repository.SellerRepository;
import com.scaler.validation.SellerProductValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerProductService {
    
    private final SellerProductRepository sellerProductRepository;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final MerchantRepository merchantRepository;
    private final SellerProductMapper sellerProductMapper;
    private final SellerProductValidator validator;
    
    @Transactional
    public SellerProductDTO createSellerProduct(SellerProductDTO dto) {
        // Validate
        validator.validateCreate(dto);
        
        // Get related entities
        Product product = productRepository.findById(dto.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            
        Seller seller = sellerRepository.findById(dto.getSellerId())
            .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
            
        Merchant merchant = merchantRepository.findById(dto.getMerchantId())
            .orElseThrow(() -> new ResourceNotFoundException("Merchant not found"));
        
        // Create relationship
        SellerProduct sellerProduct = sellerProductMapper.toEntity(dto);
        sellerProduct.setProduct(product);
        sellerProduct.setSeller(seller);
        sellerProduct.setMerchant(merchant);
        
        // Save and return
        SellerProduct saved = sellerProductRepository.save(sellerProduct);
        return sellerProductMapper.toDTO(saved);
    }
    
    @Transactional
    public SellerProductDTO updateSellerProduct(UUID id, SellerProductDTO dto) {
        SellerProduct existing = sellerProductRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("SellerProduct relationship not found"));
        
        // Validate
        validator.validateUpdate(dto, existing);
        
        // Update entity
        sellerProductMapper.updateEntity(dto, existing);
        
        // Save and return
        SellerProduct updated = sellerProductRepository.save(existing);
        return sellerProductMapper.toDTO(updated);
    }
    
    @Transactional(readOnly = true)
    public List<SellerProductDTO> findSellersByProduct(UUID productId, UUID merchantId) {
        return sellerProductRepository.findByProductAndMerchant(productId, merchantId)
            .stream()
            .map(sellerProductMapper::toDTO)
            .toList();
    }
    
    @Transactional(readOnly = true)
    public List<SellerProductDTO> findProductsBySeller(UUID sellerId, UUID merchantId) {
        return sellerProductRepository.findBySellerIdAndMerchantIdOrderByCreatedDateDesc(sellerId, merchantId)
            .stream()
            .map(sellerProductMapper::toDTO)
            .toList();
    }
    
    @Transactional(readOnly = true)
    public List<SellerProductDTO> findProductsByPriceRange(UUID merchantId, BigDecimal minPrice, BigDecimal maxPrice) {
        return sellerProductRepository.findByPriceRange(merchantId, minPrice, maxPrice)
            .stream()
            .map(sellerProductMapper::toDTO)
            .toList();
    }
    
    @Transactional(readOnly = true)
    public List<SellerProductDTO> findLowStockProducts(UUID merchantId, Integer threshold) {
        return sellerProductRepository.findLowStockProducts(merchantId, threshold)
            .stream()
            .map(sellerProductMapper::toDTO)
            .toList();
    }
    
    @Transactional
    public void deleteSellerProduct(UUID id) {
        if (!sellerProductRepository.existsById(id)) {
            throw new ResourceNotFoundException("SellerProduct relationship not found");
        }
        sellerProductRepository.deleteById(id);
    }
    

    
    @Transactional
    public void updateStock(UUID id, Integer quantity) {
        SellerProduct sellerProduct = sellerProductRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("SellerProduct relationship not found"));
            
        if (sellerProduct.getStock() + quantity < 0) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        
        sellerProduct.setStock(sellerProduct.getStock() + quantity);
        sellerProductRepository.save(sellerProduct);
    }
}
