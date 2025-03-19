package com.scaler.service;

import com.scaler.dto.ProductDTO;
import com.scaler.dto.SellerDTO;
import com.scaler.entity.Seller;
import com.scaler.entity.SellerProduct;
import com.scaler.exception.ResourceNotFoundException;
import com.scaler.mapper.ProductMapper;
import com.scaler.mapper.SellerMapper;
import com.scaler.repository.ProductRepository;
import com.scaler.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerService {
    
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final SellerMapper sellerMapper;
    private final ProductMapper productMapper;

    @Transactional
    public SellerDTO createSeller(SellerDTO sellerDTO) {
        Seller seller = sellerMapper.toEntity(sellerDTO);
        seller.setCreatedDate(LocalDateTime.now());
        seller.setLastModifiedDate(LocalDateTime.now());
        return sellerMapper.toDto(sellerRepository.save(seller));
    }

    @Transactional
    public SellerDTO updateSeller(UUID id, SellerDTO sellerDTO) {
        Seller existingSeller = sellerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + id));
        
        Seller seller = sellerMapper.toEntity(sellerDTO);
        seller.setId(existingSeller.getId());
        seller.setCreatedDate(existingSeller.getCreatedDate());
        seller.setLastModifiedDate(LocalDateTime.now());
        
        return sellerMapper.toDto(sellerRepository.save(seller));
    }

    public SellerDTO getSeller(UUID id) {
        return sellerRepository.findById(id)
            .map(sellerMapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + id));
    }

    public List<SellerDTO> getAllSellers() {
        return sellerRepository.findAll().stream()
            .map(sellerMapper::toDto)
            .toList();
    }

    public List<SellerDTO> getSellersByMerchant(UUID merchantId) {
        return sellerRepository.findByMerchantId(merchantId).stream()
            .map(sellerMapper::toDto)
            .toList();
    }

    @Transactional
    public void deleteSeller(UUID id) {
        if (!sellerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Seller not found with id: " + id);
        }
        sellerRepository.deleteById(id);
    }

    public List<ProductDTO> getSellerProducts(UUID id) {
        Seller seller = sellerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + id));
        return seller.getSellerProducts().stream()
            .map(sellerProduct -> productMapper.toDTO(sellerProduct.getProduct()))
            .toList();
    }

    @Transactional
    public void addProductToSeller(UUID sellerId, UUID productId) {
        Seller seller = sellerRepository.findById(sellerId)
            .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + sellerId));
        
        var product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        
        SellerProduct sellerProduct = SellerProduct.builder()
            .seller(seller)
            .product(product)
            .status("ACTIVE")
            .build();
            
        seller.addSellerProduct(sellerProduct);
        sellerRepository.save(seller);
    }

    @Transactional
    public void removeProductFromSeller(UUID sellerId, UUID productId) {
        Seller seller = sellerRepository.findById(sellerId)
            .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + sellerId));
        
        seller.getSellerProducts().removeIf(sellerProduct -> sellerProduct.getProduct().getId().equals(productId));
        sellerRepository.save(seller);
    }
}
