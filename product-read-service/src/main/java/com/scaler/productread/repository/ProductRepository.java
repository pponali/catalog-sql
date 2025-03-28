package com.scaler.productread.repository;

import com.scaler.productread.document.ProductDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Repository for product data in Elasticsearch.
 */
@Repository
public interface ProductRepository extends ElasticsearchRepository<ProductDocument, UUID> {
    
    /**
     * Find products by text search across name and description fields.
     *
     * @param query    The search query
     * @param pageable Pagination information
     * @return Page of matching products
     */
    @Query("{\"bool\": {\"should\": [{\"match\": {\"name\": \"?0\"}}, {\"match\": {\"description\": \"?0\"}}]}}")
    Page<ProductDocument> searchByNameOrDescription(String query, Pageable pageable);
    
    /**
     * Find products by categories.
     *
     * @param categories Set of category IDs
     * @param pageable   Pagination information
     * @return Page of matching products
     */
    Page<ProductDocument> findByCategoriesIn(Set<String> categories, Pageable pageable);
    
    /**
     * Find products by brand.
     *
     * @param brand    Brand name
     * @param pageable Pagination information
     * @return Page of matching products
     */
    Page<ProductDocument> findByBrand(String brand, Pageable pageable);
    
    /**
     * Find products by price range.
     *
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @param pageable Pagination information
     * @return Page of matching products
     */
    @Query("{\"bool\": {\"must\": [{\"range\": {\"minPrice\": {\"lte\": \"?1\"}}}, {\"range\": {\"maxPrice\": {\"gte\": \"?0\"}}}]}}")
    Page<ProductDocument> findByPriceRange(Double minPrice, Double maxPrice, Pageable pageable);
    
    /**
     * Find active products with available inventory.
     *
     * @param pageable Pagination information
     * @return Page of available products
     */
    @Query("{\"bool\": {\"must\": [{\"term\": {\"active\": true}}, {\"range\": {\"totalStock\": {\"gt\": 0}}}]}}")
    Page<ProductDocument> findAvailableProducts(Pageable pageable);
    
    /**
     * Find products by seller.
     *
     * @param sellerId Seller ID
     * @param pageable Pagination information
     * @return Page of products from the seller
     */
    @Query("{\"nested\": {\"path\": \"sellers\", \"query\": {\"term\": {\"sellers.sellerId\": \"?0\"}}}}")
    Page<ProductDocument> findBySellerId(UUID sellerId, Pageable pageable);
    
    /**
     * Find products by channel.
     *
     * @param channelId Channel ID
     * @param pageable  Pagination information
     * @return Page of products available in the channel
     */
    @Query("{\"nested\": {\"path\": \"channels\", \"query\": {\"bool\": {\"must\": [{\"term\": {\"channels.channelId\": \"?0\"}}, {\"term\": {\"channels.available\": true}}]}}}}")
    Page<ProductDocument> findByChannelId(UUID channelId, Pageable pageable);
    
    /**
     * Find products by feature value.
     *
     * @param featureCode Feature code
     * @param value       Feature value
     * @param pageable    Pagination information
     * @return Page of products with the specified feature value
     */
    @Query("{\"nested\": {\"path\": \"features\", \"query\": {\"bool\": {\"must\": [{\"term\": {\"features.code\": \"?0\"}}, {\"term\": {\"features.value\": \"?1\"}}]}}}}")
    Page<ProductDocument> findByFeatureValue(String featureCode, String value, Pageable pageable);
    
    /**
     * Find recently updated products.
     *
     * @param pageable Pagination information
     * @return Page of recently updated products
     */
    Page<ProductDocument> findAllByOrderByUpdatedAtDesc(Pageable pageable);
    
    /**
     * Find products with the specified SKUs.
     *
     * @param skus List of SKUs
     * @return List of products matching the SKUs
     */
    List<ProductDocument> findBySkuIn(List<String> skus);
}