package com.scaler.controller;

import com.scaler.dto.*;
import com.scaler.entity.*;
import com.scaler.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/query")
public class QueryController {

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

    // Original endpoints
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(queryService.getAllProducts());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(queryService.getAllCategories());
    }

    @GetMapping("/channels")
    public ResponseEntity<List<Channel>> getAllChannels() {
        return ResponseEntity.ok(queryService.getAllChannels());
    }

    @GetMapping("/merchants")
    public ResponseEntity<List<Merchant>> getAllMerchants() {
        return ResponseEntity.ok(queryService.getAllMerchants());
    }

    @GetMapping("/sellers")
    public ResponseEntity<List<Seller>> getAllSellers() {
        return ResponseEntity.ok(queryService.getAllSellers());
    }

    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(queryService.getProductsByCategory(categoryId));
    }

    @GetMapping("/products/channel/{channelId}")
    public ResponseEntity<List<Product>> getProductsByChannel(@PathVariable UUID channelId) {
        return ResponseEntity.ok(queryService.getProductsByChannel(channelId));
    }

    @GetMapping("/products/seller/{sellerId}")
    public ResponseEntity<List<Product>> getProductsBySeller(@PathVariable UUID sellerId) {
        return ResponseEntity.ok(queryService.getProductsBySeller(sellerId));
    }

    @GetMapping("/products/merchant/{merchantId}")
    public ResponseEntity<List<Product>> getProductsByMerchant(@PathVariable UUID merchantId) {
        return ResponseEntity.ok(queryService.getProductsByMerchant(merchantId));
    }

    @GetMapping("/categories/channel/{channelId}")
    public ResponseEntity<List<Category>> getCategoriesByChannel(@PathVariable UUID channelId) {
        return ResponseEntity.ok(queryService.getCategoriesByChannel(channelId));
    }

    @GetMapping("/sellers/channel/{channelId}")
    public ResponseEntity<List<Seller>> getSellersByChannel(@PathVariable UUID channelId) {
        return ResponseEntity.ok(queryService.getSellersByChannel(channelId));

    }

    @GetMapping("/merchants/channel/{channelId}")
    public ResponseEntity<List<Merchant>> getMerchantsByChannel(@PathVariable UUID channelId) {
        return ResponseEntity.ok(queryService.getMerchantsByChannel(channelId));
    }

    @GetMapping("/products/platform/{platformId}")
    public ResponseEntity<List<Product>> getProductsByPlatform(@PathVariable UUID platformId) {
        return ResponseEntity.ok(queryService.getProductsByPlatform(platformId));
    }

    @GetMapping("/products/platform/code/{platformCode}")
    public ResponseEntity<List<Product>> getProductsByPlatformCode(@PathVariable UUID platformCode) {
        return ResponseEntity.ok(queryService.getProductsByPlatformCode(platformCode));
    }

    @GetMapping("/platforms/product/{productId}")
    public ResponseEntity<List<Platform>> getPlatformsByProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(queryService.getPlatformsByProduct(productId));
    }
    
    // ProductCategory endpoints
    @GetMapping("/product-categories")
    public ResponseEntity<List<ProductCategory>> getAllProductCategories() {
        return ResponseEntity.ok(productCategoryQueryService.getAllProductCategories());
    }
    
    @GetMapping("/product-categories/{id}")
    public ResponseEntity<ProductCategory> getProductCategoryById(@PathVariable UUID id) {
        return productCategoryQueryService.getProductCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product-categories/product/{productId}")
    public ResponseEntity<List<ProductCategory>> getProductCategoriesByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(productCategoryQueryService.getProductCategoriesByProductId(productId));
    }
    
    @GetMapping("/product-categories/category/{categoryId}")
    public ResponseEntity<List<ProductCategory>> getProductCategoriesByCategoryId(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(productCategoryQueryService.getProductCategoriesByCategoryId(categoryId));
    }
    
    @GetMapping("/product-categories/merchant/{merchantId}")
    public ResponseEntity<List<ProductCategory>> getProductCategoriesByMerchantId(@PathVariable UUID merchantId) {
        return ResponseEntity.ok(productCategoryQueryService.getProductCategoriesByMerchantId(merchantId));
    }
    
    @GetMapping("/product-categories/product/{productId}/category/{categoryId}")
    public ResponseEntity<ProductCategory> getProductCategoryByProductAndCategory(
            @PathVariable UUID productId,
            @PathVariable UUID categoryId) {
        return productCategoryQueryService.getProductCategoryByProductAndCategory(productId, categoryId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product-categories/product/{productId}/category/{categoryId}/merchant/{merchantId}")
    public ResponseEntity<ProductCategory> getProductCategoryByProductCategoryAndMerchant(
            @PathVariable UUID productId,
            @PathVariable UUID categoryId,
            @PathVariable UUID merchantId) {
        return productCategoryQueryService.getProductCategoryByProductCategoryAndMerchant(productId, categoryId, merchantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}/categories")
    public ResponseEntity<List<Category>> getCategoriesByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(productCategoryQueryService.getCategoriesByProductId(productId));
    }
    
    @GetMapping("/category/{categoryId}/products/detailed")
    public ResponseEntity<List<Product>> getProductsByCategoryIdDetailed(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(productCategoryQueryService.getProductsByCategoryId(categoryId));
    }
    
    @GetMapping("/category/{categoryId}/merchant/{merchantId}/products")
    public ResponseEntity<List<Product>> getProductsByCategoryIdAndMerchantId(
            @PathVariable UUID categoryId,
            @PathVariable UUID merchantId) {
        return ResponseEntity.ok(productCategoryQueryService.getProductsByCategoryIdAndMerchantId(categoryId, merchantId));
    }
    
    // ProductChannel endpoints
    @GetMapping("/product-channels")
    public ResponseEntity<List<ProductChannel>> getAllProductChannels() {
        return ResponseEntity.ok(productChannelQueryService.getAllProductChannels());
    }
    
    @GetMapping("/product-channels/{id}")
    public ResponseEntity<ProductChannel> getProductChannelById(@PathVariable UUID id) {
        return productChannelQueryService.getProductChannelById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product-channels/product/{productId}")
    public ResponseEntity<List<ProductChannel>> getProductChannelsByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(productChannelQueryService.getProductChannelsByProductId(productId));
    }
    
    @GetMapping("/product-channels/channel/{channelId}")
    public ResponseEntity<List<ProductChannel>> getProductChannelsByChannelId(@PathVariable UUID channelId) {
        return ResponseEntity.ok(productChannelQueryService.getProductChannelsByChannelId(channelId));
    }
    
    @GetMapping("/product-channels/product/{productId}/channel/{channelId}")
    public ResponseEntity<ProductChannel> getProductChannelByProductAndChannel(
            @PathVariable UUID productId,
            @PathVariable UUID channelId) {
        return productChannelQueryService.getProductChannelByProductAndChannel(productId, channelId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}/channels")
    public ResponseEntity<List<Channel>> getChannelsByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(productChannelQueryService.getChannelsByProductId(productId));
    }
    
    @GetMapping("/channel/{channelId}/products/detailed")
    public ResponseEntity<List<Product>> getProductsByChannelIdDetailed(@PathVariable UUID channelId) {
        return ResponseEntity.ok(productChannelQueryService.getProductsByChannelId(channelId));
    }
    
    @GetMapping("/channel/{channelId}/active/{isActive}/products")
    public ResponseEntity<List<Product>> getProductsByChannelIdAndActive(
            @PathVariable UUID channelId,
            @PathVariable boolean isActive) {
        return ResponseEntity.ok(productChannelQueryService.getProductsByChannelIdAndActive(channelId, isActive));
    }
    
    // SellerProduct endpoints
    @GetMapping("/seller-products")
    public ResponseEntity<List<SellerProduct>> getAllSellerProducts() {
        return ResponseEntity.ok(sellerProductQueryService.getAllSellerProducts());
    }
    
    @GetMapping("/seller-products/{id}")
    public ResponseEntity<SellerProduct> getSellerProductById(@PathVariable UUID id) {
        return sellerProductQueryService.getSellerProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/seller-products/product/{productId}")
    public ResponseEntity<List<SellerProduct>> getSellerProductsByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(sellerProductQueryService.getSellerProductsByProductId(productId));
    }
    
    @GetMapping("/seller-products/seller/{sellerId}")
    public ResponseEntity<List<SellerProduct>> getSellerProductsBySellerId(@PathVariable UUID sellerId) {
        return ResponseEntity.ok(sellerProductQueryService.getSellerProductsBySellerId(sellerId));
    }
    
    @GetMapping("/seller-products/merchant/{merchantId}")
    public ResponseEntity<List<SellerProduct>> getSellerProductsByMerchantId(@PathVariable UUID merchantId) {
        return ResponseEntity.ok(sellerProductQueryService.getSellerProductsByMerchantId(merchantId));
    }
    
    @GetMapping("/seller-products/product/{productId}/seller/{sellerId}")
    public ResponseEntity<SellerProduct> getSellerProductByProductAndSeller(
            @PathVariable UUID productId,
            @PathVariable UUID sellerId) {
        return sellerProductQueryService.getSellerProductByProductAndSeller(productId, sellerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/seller-products/product/{productId}/seller/{sellerId}/merchant/{merchantId}")
    public ResponseEntity<SellerProduct> getSellerProductByProductSellerAndMerchant(
            @PathVariable UUID productId,
            @PathVariable UUID sellerId,
            @PathVariable UUID merchantId) {
        return sellerProductQueryService.getSellerProductByProductSellerAndMerchant(productId, sellerId, merchantId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}/sellers")
    public ResponseEntity<List<Seller>> getSellersByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(sellerProductQueryService.getSellersByProductId(productId));
    }
    
    @GetMapping("/seller/{sellerId}/products/detailed")
    public ResponseEntity<List<Product>> getProductsBySellerIdDetailed(@PathVariable UUID sellerId) {
        return ResponseEntity.ok(sellerProductQueryService.getProductsBySellerId(sellerId));
    }
    
    @GetMapping("/seller/{sellerId}/merchant/{merchantId}/products")
    public ResponseEntity<List<Product>> getProductsBySellerIdAndMerchantId(
            @PathVariable UUID sellerId,
            @PathVariable UUID merchantId) {
        return ResponseEntity.ok(sellerProductQueryService.getProductsBySellerIdAndMerchantId(sellerId, merchantId));
    }
    
    // ProductPlatform endpoints
    @GetMapping("/product-platforms")
    public ResponseEntity<List<ProductPlatform>> getAllProductPlatforms() {
        return ResponseEntity.ok(productPlatformQueryService.getAllProductPlatforms());
    }
    
    @GetMapping("/product-platforms/{id}")
    public ResponseEntity<ProductPlatform> getProductPlatformById(@PathVariable UUID id) {
        return productPlatformQueryService.getProductPlatformById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product-platforms/product/{productId}")
    public ResponseEntity<List<ProductPlatform>> getProductPlatformsByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(productPlatformQueryService.getProductPlatformsByProductId(productId));
    }
    
    @GetMapping("/product-platforms/platform/{platformId}")
    public ResponseEntity<List<ProductPlatform>> getProductPlatformsByPlatformId(@PathVariable UUID platformId) {
        return ResponseEntity.ok(productPlatformQueryService.getProductPlatformsByPlatformId(platformId));
    }
    
    @GetMapping("/product-platforms/product/{productId}/platform/{platformId}")
    public ResponseEntity<ProductPlatform> getProductPlatformByProductAndPlatform(
            @PathVariable UUID productId,
            @PathVariable UUID platformId) {
        return productPlatformQueryService.getProductPlatformByProductAndPlatform(productId, platformId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product/{productId}/platforms/detailed")
    public ResponseEntity<List<Platform>> getPlatformsByProductIdDetailed(@PathVariable UUID productId) {
        return ResponseEntity.ok(productPlatformQueryService.getPlatformsByProductId(productId));
    }
    
    @GetMapping("/platform/{platformId}/products/detailed")
    public ResponseEntity<List<Product>> getProductsByPlatformIdDetailed(@PathVariable UUID platformId) {
        return ResponseEntity.ok(productPlatformQueryService.getProductsByPlatformId(platformId));
    }
    
    @GetMapping("/platform/code/{platformCode}/products/detailed")
    public ResponseEntity<List<Product>> getProductsByPlatformCodeDetailed(@PathVariable String platformCode) {
        return ResponseEntity.ok(productPlatformQueryService.getProductsByPlatformCode(platformCode));
    }
    
    @GetMapping("/platform/{platformId}/active/{isActive}/products")
    public ResponseEntity<List<Product>> getProductsByPlatformIdAndActive(
            @PathVariable UUID platformId,
            @PathVariable boolean isActive) {
        return ResponseEntity.ok(productPlatformQueryService.getProductsByPlatformIdAndActive(platformId, isActive));
    }
    
    // ChannelCatalog endpoints
    @GetMapping("/channel-catalogs")
    public ResponseEntity<List<ChannelCatalog>> getAllChannelCatalogs() {
        return ResponseEntity.ok(channelCatalogQueryService.getAllChannelCatalogs());
    }
    
    @GetMapping("/channel-catalogs/{id}")
    public ResponseEntity<ChannelCatalog> getChannelCatalogById(@PathVariable UUID id) {
        return channelCatalogQueryService.getChannelCatalogById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/channel-catalogs/channel/{channelId}")
    public ResponseEntity<List<ChannelCatalog>> getChannelCatalogsByChannelId(@PathVariable UUID channelId) {
        return ResponseEntity.ok(channelCatalogQueryService.getChannelCatalogsByChannelId(channelId));
    }
    
    @GetMapping("/channel-catalogs/catalog/{catalogId}")
    public ResponseEntity<List<ChannelCatalog>> getChannelCatalogsByCatalogId(@PathVariable UUID catalogId) {
        return ResponseEntity.ok(channelCatalogQueryService.getChannelCatalogsByCatalogId(catalogId));
    }
    
    @GetMapping("/channel-catalogs/channel/{channelId}/catalog/{catalogId}")
    public ResponseEntity<ChannelCatalog> getChannelCatalogByChannelAndCatalog(
            @PathVariable UUID channelId,
            @PathVariable UUID catalogId) {
        return channelCatalogQueryService.getChannelCatalogByChannelAndCatalog(channelId, catalogId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/channel/{channelId}/catalogs")
    public ResponseEntity<List<Catalog>> getCatalogsByChannelId(@PathVariable UUID channelId) {
        return ResponseEntity.ok(channelCatalogQueryService.getCatalogsByChannelId(channelId));
    }
    
    @GetMapping("/catalog/{catalogId}/channels")
    public ResponseEntity<List<Channel>> getChannelsByCatalogId(@PathVariable UUID catalogId) {
        return ResponseEntity.ok(channelCatalogQueryService.getChannelsByCatalogId(catalogId));
    }
    
    @GetMapping("/channel-catalogs/active")
    public ResponseEntity<List<ChannelCatalog>> getActiveChannelCatalogs() {
        return ResponseEntity.ok(channelCatalogQueryService.getActiveChannelCatalogs());
    }
    
    @GetMapping("/channel/{channelId}/catalogs/active")
    public ResponseEntity<List<ChannelCatalog>> getActiveChannelCatalogsByChannelId(@PathVariable UUID channelId) {
        return ResponseEntity.ok(channelCatalogQueryService.getActiveChannelCatalogsByChannelId(channelId));
    }
    
    @GetMapping("/catalog/{catalogId}/channels/active")
    public ResponseEntity<List<ChannelCatalog>> getActiveChannelCatalogsByCatalogId(@PathVariable UUID catalogId) {
        return ResponseEntity.ok(channelCatalogQueryService.getActiveChannelCatalogsByCatalogId(catalogId));
    }
    
    // ProductFeatureValueMapping endpoints
    @GetMapping("/product-feature-value-mappings")
    public ResponseEntity<List<ProductFeatureValueMapping>> getAllProductFeatureValueMappings() {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getAllProductFeatureValueMappings());
    }
    
    @GetMapping("/product-feature-value-mappings/{id}")
    public ResponseEntity<ProductFeatureValueMapping> getProductFeatureValueMappingById(@PathVariable UUID id) {
        return productFeatureValueMappingQueryService.getProductFeatureValueMappingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product-feature-value-mappings/product/{productId}")
    public ResponseEntity<List<ProductFeatureValueMapping>> getProductFeatureValueMappingsByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getProductFeatureValueMappingsByProductId(productId));
    }
    
    @GetMapping("/product-feature-value-mappings/feature/{featureId}")
    public ResponseEntity<List<ProductFeatureValueMapping>> getProductFeatureValueMappingsByFeatureId(@PathVariable UUID featureId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getProductFeatureValueMappingsByFeatureId(featureId));
    }
    
    @GetMapping("/product-feature-value-mappings/feature-value/{featureValueId}")
    public ResponseEntity<List<ProductFeatureValueMapping>> getProductFeatureValueMappingsByFeatureValueId(@PathVariable UUID featureValueId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getProductFeatureValueMappingsByFeatureValueId(featureValueId));
    }
    
    @GetMapping("/product-feature-value-mappings/template/{templateId}")
    public ResponseEntity<List<ProductFeatureValueMapping>> getProductFeatureValueMappingsByTemplateId(@PathVariable UUID templateId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getProductFeatureValueMappingsByTemplateId(templateId));
    }
    
    @GetMapping("/product-feature-value-mappings/product/{productId}/feature/{featureId}")
    public ResponseEntity<ProductFeatureValueMapping> getProductFeatureValueMappingByProductAndFeature(
            @PathVariable UUID productId,
            @PathVariable UUID featureId) {
        return productFeatureValueMappingQueryService.getProductFeatureValueMappingByProductAndFeature(productId, featureId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/product-feature-value-mappings/product/{productId}/template/{templateId}")
    public ResponseEntity<List<ProductFeatureValueMapping>> getProductFeatureValueMappingsByProductAndTemplate(
            @PathVariable UUID productId,
            @PathVariable UUID templateId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getProductFeatureValueMappingsByProductAndTemplate(productId, templateId));
    }
    
    @GetMapping("/product/{productId}/features")
    public ResponseEntity<List<ProductFeature>> getFeaturesByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getFeaturesByProductId(productId));
    }
    
    @GetMapping("/product/{productId}/feature-values")
    public ResponseEntity<List<ProductFeatureValue>> getFeatureValuesByProductId(@PathVariable UUID productId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getFeatureValuesByProductId(productId));
    }
    
    @GetMapping("/product/{productId}/feature/{featureId}/feature-values")
    public ResponseEntity<List<ProductFeatureValue>> getFeatureValuesByProductAndFeature(
            @PathVariable UUID productId,
            @PathVariable UUID featureId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getFeatureValuesByProductAndFeature(productId, featureId));
    }
    
    @GetMapping("/feature/{featureId}/products")
    public ResponseEntity<List<Product>> getProductsByFeatureId(@PathVariable UUID featureId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getProductsByFeatureId(featureId));
    }
    
    @GetMapping("/feature-value/{featureValueId}/products")
    public ResponseEntity<List<Product>> getProductsByFeatureValueId(@PathVariable UUID featureValueId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getProductsByFeatureValueId(featureValueId));
    }
    
    @GetMapping("/template/{templateId}/products")
    public ResponseEntity<List<Product>> getProductsByTemplateId(@PathVariable UUID templateId) {
        return ResponseEntity.ok(productFeatureValueMappingQueryService.getProductsByTemplateId(templateId));
    }
}
