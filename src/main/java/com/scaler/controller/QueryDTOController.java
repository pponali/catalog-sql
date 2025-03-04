package com.scaler.controller;

import com.scaler.dto.*;
import com.scaler.entity.*;
import com.scaler.mapper.*;
import com.scaler.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/query/dashboard")
public class QueryDTOController {

    @Autowired
    private QueryService queryService;
    
    @Autowired
    private ProductCategoryQueryService productCategoryQueryService;
    
    @Autowired
    private ProductChannelQueryService productChannelQueryService;
    
    @Autowired
    private SellerProductQueryService sellerProductQueryService;
    
    @Autowired
    private ProductPlatformQueryService productPlatformQueryService;
    
    @Autowired
    private ChannelCatalogQueryService channelCatalogQueryService;
    
    @Autowired
    private ProductFeatureValueMappingQueryService productFeatureValueMappingQueryService;
    
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
    
    @Autowired
    private ProductCategoryMapper productCategoryMapper;
    
    @Autowired
    private ProductChannelMapper productChannelMapper;
    
    @Autowired
    private SellerProductMapper sellerProductMapper;
    
    // Original endpoints with DTO responses
    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = queryService.getAllProducts();
        List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = queryService.getAllCategories();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }

    @GetMapping("/channels")
    public ResponseEntity<List<ChannelDTO>> getAllChannels() {
        List<Channel> channels = queryService.getAllChannels();
        List<ChannelDTO> channelDTOs = channels.stream()
                .map(channelMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(channelDTOs);
    }

    @GetMapping("/merchants")
    public ResponseEntity<List<MerchantDTO>> getAllMerchants() {
        List<Merchant> merchants = queryService.getAllMerchants();
        List<MerchantDTO> merchantDTOs = merchants.stream()
                .map(merchantMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(merchantDTOs);
    }

    @GetMapping("/sellers")
    public ResponseEntity<List<SellerDTO>> getAllSellers() {
        List<Seller> sellers = queryService.getAllSellers();
        List<SellerDTO> sellerDTOs = sellers.stream()
                .map(sellerMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sellerDTOs);
    }

    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable UUID categoryId) {
        List<Product> products = queryService.getProductsByCategory(categoryId);
        List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/products/channel/{channelId}")
    public ResponseEntity<List<ProductDTO>> getProductsByChannel(@PathVariable UUID channelId) {
        List<Product> products = queryService.getProductsByChannel(channelId);
        List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/products/seller/{sellerId}")
    public ResponseEntity<List<ProductDTO>> getProductsBySeller(@PathVariable UUID sellerId) {
        List<Product> products = queryService.getProductsBySeller(sellerId);
        List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/products/merchant/{merchantId}")
    public ResponseEntity<List<ProductDTO>> getProductsByMerchant(@PathVariable UUID merchantId) {
        List<Product> products = queryService.getProductsByMerchant(merchantId);
        List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/categories/channel/{channelId}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByChannel(@PathVariable UUID channelId) {
        List<Category> categories = queryService.getCategoriesByChannel(channelId);
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }

    @GetMapping("/sellers/channel/{channelId}")
    public ResponseEntity<List<SellerDTO>> getSellersByChannel(@PathVariable UUID channelId) {
        List<Seller> sellers = queryService.getSellersByChannel(channelId);
        List<SellerDTO> sellerDTOs = sellers.stream()
                .map(sellerMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sellerDTOs);
    }

    @GetMapping("/merchants/channel/{channelId}")
    public ResponseEntity<List<MerchantDTO>> getMerchantsByChannel(@PathVariable UUID channelId) {
        List<Merchant> merchants = queryService.getMerchantsByChannel(channelId);
        List<MerchantDTO> merchantDTOs = merchants.stream()
                .map(merchantMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(merchantDTOs);
    }

    @GetMapping("/products/platform/{platformId}")
    public ResponseEntity<List<ProductDTO>> getProductsByPlatform(@PathVariable UUID platformId) {
        List<Product> products = queryService.getProductsByPlatform(platformId);
        List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/products/platform/code/{platformCode}")
    public ResponseEntity<List<ProductDTO>> getProductsByPlatformCode(@PathVariable UUID platformCode) {
        List<Product> products = queryService.getProductsByPlatformCode(platformCode);
        List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    // ProductCategory endpoints with DTO responses
    @GetMapping("/product-categories")
    public ResponseEntity<List<ProductCategoryDTO>> getAllProductCategories() {
        List<ProductCategory> productCategories = productCategoryQueryService.getAllProductCategories();
        List<ProductCategoryDTO> productCategoryDTOs = productCategories.stream()
                .map(productCategoryMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productCategoryDTOs);
    }
    
    @GetMapping("/product-categories/{id}")
    public ResponseEntity<ProductCategoryDTO> getProductCategoryById(@PathVariable UUID id) {
        return productCategoryQueryService.getProductCategoryById(id)
                .map(productCategoryMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product-categories/product/{productId}")
    public ResponseEntity<List<ProductCategoryDTO>> getProductCategoriesByProductId(@PathVariable UUID productId) {
        List<ProductCategory> productCategories = productCategoryQueryService.getProductCategoriesByProductId(productId);
        List<ProductCategoryDTO> productCategoryDTOs = productCategories.stream()
                .map(productCategoryMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productCategoryDTOs);
    }
    
    @GetMapping("/product-categories/category/{categoryId}")
    public ResponseEntity<List<ProductCategoryDTO>> getProductCategoriesByCategoryId(@PathVariable UUID categoryId) {
        List<ProductCategory> productCategories = productCategoryQueryService.getProductCategoriesByCategoryId(categoryId);
        List<ProductCategoryDTO> productCategoryDTOs = productCategories.stream()
                .map(productCategoryMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productCategoryDTOs);
    }
    
    @GetMapping("/product-categories/merchant/{merchantId}")
    public ResponseEntity<List<ProductCategoryDTO>> getProductCategoriesByMerchantId(@PathVariable UUID merchantId) {
        List<ProductCategory> productCategories = productCategoryQueryService.getProductCategoriesByMerchantId(merchantId);
        List<ProductCategoryDTO> productCategoryDTOs = productCategories.stream()
                .map(productCategoryMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productCategoryDTOs);
    }
    
    @GetMapping("/product-categories/product/{productId}/category/{categoryId}")
    public ResponseEntity<ProductCategoryDTO> getProductCategoryByProductAndCategory(
            @PathVariable UUID productId,
            @PathVariable UUID categoryId) {
        return productCategoryQueryService.getProductCategoryByProductAndCategory(productId, categoryId)
                .map(productCategoryMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product-categories/product/{productId}/category/{categoryId}/merchant/{merchantId}")
    public ResponseEntity<ProductCategoryDTO> getProductCategoryByProductCategoryAndMerchant(
            @PathVariable UUID productId,
            @PathVariable UUID categoryId,
            @PathVariable UUID merchantId) {
        return productCategoryQueryService.getProductCategoryByProductCategoryAndMerchant(productId, categoryId, merchantId)
                .map(productCategoryMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}/categories")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByProductId(@PathVariable UUID productId) {
        List<Category> categories = productCategoryQueryService.getCategoriesByProductId(productId);
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }
    
    @GetMapping("/category/{categoryId}/products/detailed")
    public ResponseEntity<List<ProductDTO>> getProductsByCategoryIdDetailed(@PathVariable UUID categoryId) {
        List<Product> products = productCategoryQueryService.getProductsByCategoryId(categoryId);
        List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }
    
    @GetMapping("/category/{categoryId}/merchant/{merchantId}/products")
    public ResponseEntity<List<ProductDTO>> getProductsByCategoryIdAndMerchantId(
            @PathVariable UUID categoryId,
            @PathVariable UUID merchantId) {
        List<Product> products = productCategoryQueryService.getProductsByCategoryIdAndMerchantId(categoryId, merchantId);
        List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }
    
    // ProductChannel endpoints with DTO responses
    @GetMapping("/product-channels")
    public ResponseEntity<List<ProductChannelDTO>> getAllProductChannels() {
        List<ProductChannel> productChannels = productChannelQueryService.getAllProductChannels();
        List<ProductChannelDTO> productChannelDTOs = productChannels.stream()
                .map(productChannelMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productChannelDTOs);
    }
    
    @GetMapping("/product-channels/{id}")
    public ResponseEntity<ProductChannelDTO> getProductChannelById(@PathVariable UUID id) {
        return productChannelQueryService.getProductChannelById(id)
                .map(productChannelMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product-channels/product/{productId}")
    public ResponseEntity<List<ProductChannelDTO>> getProductChannelsByProductId(@PathVariable UUID productId) {
        List<ProductChannel> productChannels = productChannelQueryService.getProductChannelsByProductId(productId);
        List<ProductChannelDTO> productChannelDTOs = productChannels.stream()
                .map(productChannelMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productChannelDTOs);
    }
    
    @GetMapping("/product-channels/channel/{channelId}")
    public ResponseEntity<List<ProductChannelDTO>> getProductChannelsByChannelId(@PathVariable UUID channelId) {
        List<ProductChannel> productChannels = productChannelQueryService.getProductChannelsByChannelId(channelId);
        List<ProductChannelDTO> productChannelDTOs = productChannels.stream()
                .map(productChannelMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productChannelDTOs);
    }
    
    // SellerProduct endpoints with DTO responses
    @GetMapping("/seller-products")
    public ResponseEntity<List<SellerProductDTO>> getAllSellerProducts() {
        List<SellerProduct> sellerProducts = sellerProductQueryService.getAllSellerProducts();
        List<SellerProductDTO> sellerProductDTOs = sellerProducts.stream()
                .map(sellerProductMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sellerProductDTOs);
    }
    
    @GetMapping("/seller-products/{id}")
    public ResponseEntity<SellerProductDTO> getSellerProductById(@PathVariable UUID id) {
        return sellerProductQueryService.getSellerProductById(id)
                .map(sellerProductMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/seller-products/product/{productId}")
    public ResponseEntity<List<SellerProductDTO>> getSellerProductsByProductId(@PathVariable UUID productId) {
        List<SellerProduct> sellerProducts = sellerProductQueryService.getSellerProductsByProductId(productId);
        List<SellerProductDTO> sellerProductDTOs = sellerProducts.stream()
                .map(sellerProductMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sellerProductDTOs);
    }
    
    @GetMapping("/seller-products/seller/{sellerId}")
    public ResponseEntity<List<SellerProductDTO>> getSellerProductsBySellerId(@PathVariable UUID sellerId) {
        List<SellerProduct> sellerProducts = sellerProductQueryService.getSellerProductsBySellerId(sellerId);
        List<SellerProductDTO> sellerProductDTOs = sellerProducts.stream()
                .map(sellerProductMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(sellerProductDTOs);
    }
}
