package com.scaler.productread.service.impl;

import com.scaler.productread.client.CatalogServiceClient;
import com.scaler.productread.document.ProductDocument;
import com.scaler.productread.dto.ProductFilterDTO;
import com.scaler.productread.dto.ProductResultDTO;
import com.scaler.productread.dto.ProductSortDTO;
import com.scaler.productread.repository.ProductRepository;
import com.scaler.productread.service.ProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the ProductService interface.
 * Provides methods for retrieving and manipulating product data.
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final CatalogServiceClient catalogServiceClient;
    
    @Autowired
    public ProductServiceImpl(
            ProductRepository productRepository,
            ElasticsearchOperations elasticsearchOperations,
            CatalogServiceClient catalogServiceClient) {
        this.productRepository = productRepository;
        this.elasticsearchOperations = elasticsearchOperations;
        this.catalogServiceClient = catalogServiceClient;
    }
    
    @Override
    @Cacheable(value = "products", key = "#id")
    public ProductDocument findById(UUID id) {
        log.debug("Finding product by ID: {}", id);
        return productRepository.findById(id).orElseGet(() -> refreshProduct(id));
    }
    
    @Override
    @Cacheable(value = "productQueries", key = "'filter:' + #filter + ':page:' + #page + ':size:' + #size + ':sort:' + #sorts")
    public ProductResultDTO findProducts(ProductFilterDTO filter, int page, int size, List<ProductSortDTO> sorts) {
        log.debug("Finding products with filter: {}, page: {}, size: {}", filter, page, size);
        
        // Create a criteria query using the new API
        Pageable pageable = createPageable(page, size, sorts);
        
        // Build query with criteria
        Query query = Query.findAll().setPageable(pageable);
        
        // Apply filters
        if (filter != null) {
            query = applyFilters(query, filter);
        }
        
        SearchHits<ProductDocument> searchHits = elasticsearchOperations.search(
                query, ProductDocument.class);
        
        return createProductResult(searchHits, page, size);
    }
    
    @Override
    @Cacheable(value = "searchQueries", key = "'query:' + #query + ':page:' + #page + ':size:' + #size")
    public ProductResultDTO searchProducts(String query, int page, int size) {
        log.debug("Searching products with query: {}, page: {}, size: {}", query, page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDocument> productPage = productRepository.searchByNameOrDescription(query, pageable);
        
        return createProductResult(productPage, page, size);
    }
    
    @Override
    @Cacheable(value = "categoryQueries", key = "'category:' + #categoryId + ':page:' + #page + ':size:' + #size")
    public ProductResultDTO findByCategory(UUID categoryId, int page, int size) {
        log.debug("Finding products by category: {}, page: {}, size: {}", categoryId, page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Set<String> categoryIds = new HashSet<>();
        categoryIds.add(categoryId.toString());
        
        Page<ProductDocument> productPage = productRepository.findByCategoriesIn(categoryIds, pageable);
        
        return createProductResult(productPage, page, size);
    }
    
    @Override
    @Cacheable(value = "brandQueries", key = "'brand:' + #brand + ':page:' + #page + ':size:' + #size")
    public ProductResultDTO findByBrand(String brand, int page, int size) {
        log.debug("Finding products by brand: {}, page: {}, size: {}", brand, page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDocument> productPage = productRepository.findByBrand(brand, pageable);
        
        return createProductResult(productPage, page, size);
    }
    
    @Override
    @Cacheable(value = "channelQueries", key = "'channel:' + #channelId + ':page:' + #page + ':size:' + #size")
    public ProductResultDTO findByChannel(UUID channelId, int page, int size) {
        log.debug("Finding products by channel: {}, page: {}, size: {}", channelId, page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDocument> productPage = productRepository.findByChannelId(channelId, pageable);
        
        return createProductResult(productPage, page, size);
    }
    
    @Override
    @Cacheable(value = "sellerQueries", key = "'seller:' + #sellerId + ':page:' + #page + ':size:' + #size")
    public ProductResultDTO findBySeller(UUID sellerId, int page, int size) {
        log.debug("Finding products by seller: {}, page: {}, size: {}", sellerId, page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDocument> productPage = productRepository.findBySellerId(sellerId, pageable);
        
        return createProductResult(productPage, page, size);
    }
    
    @Override
    public ProductDocument indexProduct(ProductDocument product) {
        log.debug("Indexing product: {}", product.getId());
        return productRepository.save(product);
    }
    
    @Override
    @CacheEvict(value = {"products", "productQueries", "searchQueries", "categoryQueries", "brandQueries", "channelQueries", "sellerQueries"}, 
                key = "#id", allEntries = true)
    public void deleteProduct(UUID id) {
        log.debug("Deleting product: {}", id);
        productRepository.deleteById(id);
    }
    
    @Override
    @CircuitBreaker(name = "catalogService", fallbackMethod = "getProductFallback")
    @Retry(name = "catalogService")
    public ProductDocument refreshProduct(UUID id) {
        log.debug("Refreshing product data from catalog service: {}", id);
        
        // Fetch product data from catalog service
        Map<String, Object> productData = catalogServiceClient.getProduct(id);
        if (productData.isEmpty()) {
            log.warn("Product not found in catalog service: {}", id);
            return null;
        }
        
        // Convert to ProductDocument and save to Elasticsearch
        ProductDocument product = convertToProductDocument(productData);
        return productRepository.save(product);
    }
    
    /**
     * Fallback method for refreshing product data.
     */
    private ProductDocument getProductFallback(UUID id, Exception e) {
        log.warn("Fallback for refreshProduct called. ID: {}, Error: {}", id, e.getMessage());
        return null;
    }
    
    /**
     * Convert product data from catalog service to ProductDocument.
     */
    private ProductDocument convertToProductDocument(Map<String, Object> productData) {
        // Implement conversion logic here
        // This is a simplified example
        ProductDocument product = new ProductDocument();
        product.setId(UUID.fromString(productData.get("id").toString()));
        product.setName((String) productData.get("name"));
        product.setDescription((String) productData.get("description"));
        product.setSku((String) productData.get("sku"));
        product.setBrand((String) productData.get("brand"));
        
        // Set other fields based on the data
        
        return product;
    }
    
    /**
     * Create a PageRequest with sorting.
     */
    private Pageable createPageable(int page, int size, List<ProductSortDTO> sorts) {
        if (sorts == null || sorts.isEmpty()) {
            return PageRequest.of(page, size);
        }
        
        List<Sort.Order> orders = sorts.stream()
                .map(sort -> {
                    String fieldName = mapSortField(sort.getField());
                    return sort.getDirection() == ProductSortDTO.SortDirection.ASC
                            ? Sort.Order.asc(fieldName)
                            : Sort.Order.desc(fieldName);
                })
                .collect(Collectors.toList());
        
        return PageRequest.of(page, size, Sort.by(orders));
    }
    
    /**
     * Map sort field enum to actual field name.
     */
    private String mapSortField(ProductSortDTO.ProductSortField field) {
        switch (field) {
            case NAME: return "name";
            case PRICE: return "minPrice";
            case CREATED_AT: return "createdAt";
            case UPDATED_AT: return "updatedAt";
            default: return "name";
        }
    }
    
    /**
     * Apply filters to the query.
     * @return The updated query with filters applied
     */
    private Query applyFilters(Query query, ProductFilterDTO filter) {
        // Create a criteria query from scratch
        CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria());
        criteriaQuery.setPageable(query.getPageable());
        
        List<Criteria> criteriaList = new ArrayList<>();
        
        // Apply different filters based on the filter object
        if (filter.getCategoryIds() != null && !filter.getCategoryIds().isEmpty()) {
            criteriaList.add(Criteria.where("categories").in(filter.getCategoryIds()));
        }
        
        if (filter.getBrands() != null && !filter.getBrands().isEmpty()) {
            criteriaList.add(Criteria.where("brand").in(filter.getBrands()));
        }
        
        // Apply price range filter
        if (filter.getPriceRange() != null) {
            ProductFilterDTO.PriceRangeDTO priceRange = filter.getPriceRange();
            if (priceRange.getMin() != null && priceRange.getMax() != null) {
                Criteria priceCriteria = new Criteria();
                priceCriteria.and(Criteria.where("minPrice").lessThanEqual(priceRange.getMax()))
                           .and(Criteria.where("maxPrice").greaterThanEqual(priceRange.getMin()));
                criteriaList.add(priceCriteria);
            } else if (priceRange.getMin() != null) {
                criteriaList.add(Criteria.where("maxPrice").greaterThanEqual(priceRange.getMin()));
            } else if (priceRange.getMax() != null) {
                criteriaList.add(Criteria.where("minPrice").lessThanEqual(priceRange.getMax()));
            }
        }
        
        if (filter.getActive() != null) {
            criteriaList.add(Criteria.where("active").is(filter.getActive()));
        }
        
        if (filter.getInStock() != null && filter.getInStock()) {
            criteriaList.add(Criteria.where("totalStock").greaterThan(0));
        }
        
        // Combine all criteria with AND
        if (!criteriaList.isEmpty()) {
            Criteria combinedCriteria = new Criteria().and(criteriaList.toArray(new Criteria[0]));
            criteriaQuery = new CriteriaQuery(combinedCriteria);
            criteriaQuery.setPageable(query.getPageable());
        }
        
        return criteriaQuery;
    }
    
    /**
     * Create product result from search hits.
     */
    private ProductResultDTO createProductResult(SearchHits<ProductDocument> searchHits, int page, int size) {
        List<ProductDocument> products = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
        
        long totalElements = searchHits.getTotalHits();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        
        return ProductResultDTO.builder()
                .content(products)
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .facets(Collections.emptyList()) // Add facet extraction logic if needed
                .build();
    }
    
    /**
     * Create product result from page.
     */
    private ProductResultDTO createProductResult(Page<ProductDocument> productPage, int page, int size) {
        return ProductResultDTO.builder()
                .content(productPage.getContent())
                .page(page)
                .size(size)
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .facets(Collections.emptyList()) // Add facet extraction logic if needed
                .build();
    }
}