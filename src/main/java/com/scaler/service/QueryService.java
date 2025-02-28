package com.scaler.service;

import com.scaler.dto.*;
import com.scaler.entity.*;
import com.scaler.mapper.*;
import com.scaler.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class QueryService {

    @Autowired
    private QueryRepository queryRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ChannelRepository channelRepository;
    
    @Autowired
    private MerchantRepository merchantRepository;
    
    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    @Autowired
    private ChannelMapper channelMapper;
    
    @Autowired
    private MerchantMapper merchantMapper;
    
    @Autowired
    private SellerMapper sellerMapper;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    public List<Merchant> getAllMerchants() {
        return merchantRepository.findAll();
    }

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public List<Product> getProductsByCategory(UUID categoryId) {
        return queryRepository.findProductsByCategory(categoryId);
    }

    public List<Product> getProductsByChannel(UUID channelId) {
        return queryRepository.findProductsByChannel(channelId);
    }

    public List<Product> getProductsBySeller(UUID sellerId) {
        return queryRepository.findProductsBySeller(sellerId);
    }

    public List<Product> getProductsByMerchant(UUID merchantId) {
        return queryRepository.findProductsByMerchant(merchantId);
    }

    public List<Category> getCategoriesByChannel(UUID channelId) {
        return queryRepository.findCategoriesByChannel(channelId);
    }

    public List<Seller> getSellersByChannel(UUID channelId) {
        return queryRepository.findSellersByChannel(channelId);
    }

    public List<Merchant> getMerchantsByChannel(UUID channelId) {
        return queryRepository.findMerchantsByChannel(channelId);
    }
}
