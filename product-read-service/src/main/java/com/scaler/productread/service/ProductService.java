package com.scaler.productread.service;

import com.scaler.productread.document.ProductDocument;
import com.scaler.productread.dto.ProductFilterDTO;
import com.scaler.productread.dto.ProductResultDTO;
import com.scaler.productread.dto.ProductSortDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for product operations.
 */
public interface ProductService {
    
    /**
     * Find a product by its ID.
     *
     * @param id Product ID
     * @return Found product or null
     */
    ProductDocument findById(UUID id);
    
    /**
     * Find products with the provided filter criteria.
     *
     * @param filter Filter criteria
     * @param page   Page number
     * @param size   Page size
     * @param sorts  Sorting options
     * @return Paginated result with products and facets
     */
    ProductResultDTO findProducts(ProductFilterDTO filter, int page, int size, List<ProductSortDTO> sorts);
    
    /**
     * Search products by text query.
     *
     * @param query Search query
     * @param page  Page number
     * @param size  Page size
     * @return Paginated result with matching products
     */
    ProductResultDTO searchProducts(String query, int page, int size);
    
    /**
     * Find products by category.
     *
     * @param categoryId Category ID
     * @param page       Page number
     * @param size       Page size
     * @return Paginated result with products in the category
     */
    ProductResultDTO findByCategory(UUID categoryId, int page, int size);
    
    /**
     * Find products by brand.
     *
     * @param brand Brand name
     * @param page  Page number
     * @param size  Page size
     * @return Paginated result with products of the brand
     */
    ProductResultDTO findByBrand(String brand, int page, int size);
    
    /**
     * Find products available in a channel.
     *
     * @param channelId Channel ID
     * @param page      Page number
     * @param size      Page size
     * @return Paginated result with products available in the channel
     */
    ProductResultDTO findByChannel(UUID channelId, int page, int size);
    
    /**
     * Find products by seller.
     *
     * @param sellerId Seller ID
     * @param page     Page number
     * @param size     Page size
     * @return Paginated result with products from the seller
     */
    ProductResultDTO findBySeller(UUID sellerId, int page, int size);
    
    /**
     * Index a product in Elasticsearch.
     *
     * @param product Product to index
     * @return Indexed product
     */
    ProductDocument indexProduct(ProductDocument product);
    
    /**
     * Delete a product from the index.
     *
     * @param id Product ID to delete
     */
    void deleteProduct(UUID id);
    
    /**
     * Refresh product data from the catalog service.
     *
     * @param id Product ID to refresh
     * @return Updated product
     */
    ProductDocument refreshProduct(UUID id);
}