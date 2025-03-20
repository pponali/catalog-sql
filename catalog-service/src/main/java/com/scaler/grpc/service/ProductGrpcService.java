package com.scaler.grpc.service;

import com.scaler.dto.ProductDTO;
import com.scaler.entity.Product;
import com.scaler.entity.ProductCategory;
import com.scaler.entity.ProductFeature;
import com.scaler.entity.ProductFeatureValue;
import com.scaler.grpc.product.*;
import com.scaler.service.ProductService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * gRPC service implementation for the Product service
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductGrpcService extends ProductServiceGrpc.ProductServiceImplBase {

    private final ProductService productService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void getProduct(GetProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        try {
            log.info("gRPC request received to get product with ID: {}", request.getId());
            
            Product product = productService.getProductById(UUID.fromString(request.getId()));
            ProductMessage productMessage = mapProductToProductMessage(product);
            
            ProductResponse response = ProductResponse.newBuilder()
                    .setProduct(productMessage)
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            log.info("gRPC response sent for product with ID: {}", request.getId());
        } catch (Exception e) {
            log.error("Error processing gRPC getProduct request", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void getProductsByCategory(GetProductsByCategoryRequest request, StreamObserver<ProductListResponse> responseObserver) {
        try {
            log.info("gRPC request received to get products by category ID: {}", request.getCategoryId());
            
            List<Product> products = productService.getProductsByCategory(UUID.fromString(request.getCategoryId()));
            List<ProductMessage> productMessages = products.stream()
                    .map(this::mapProductToProductMessage)
                    .collect(Collectors.toList());
            
            ProductListResponse response = ProductListResponse.newBuilder()
                    .addAllProducts(productMessages)
                    .setTotalCount(productMessages.size())
                    .setPage(1)
                    .setSize(productMessages.size())
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            log.info("gRPC response sent for products by category: {} (total: {})", 
                    request.getCategoryId(), productMessages.size());
        } catch (Exception e) {
            log.error("Error processing gRPC getProductsByCategory request", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void createProduct(CreateProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        try {
            log.info("gRPC request received to create a new product");
            
            ProductMessage productMessage = request.getProduct();
            ProductDTO productDTO = mapProductMessageToProductDTO(productMessage);
            
            Product createdProduct = productService.createProduct(productDTO);
            ProductMessage createdProductMessage = mapProductToProductMessage(createdProduct);
            
            ProductResponse response = ProductResponse.newBuilder()
                    .setProduct(createdProductMessage)
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            log.info("gRPC response sent for created product with ID: {}", createdProduct.getId());
        } catch (Exception e) {
            log.error("Error processing gRPC createProduct request", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void updateProduct(UpdateProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        try {
            log.info("gRPC request received to update product with ID: {}", request.getProduct().getId());
            
            ProductMessage productMessage = request.getProduct();
            ProductDTO productDTO = mapProductMessageToProductDTO(productMessage);
            
            Product updatedProduct = productService.updateProduct(
                    UUID.fromString(productMessage.getId()), productDTO);
            ProductMessage updatedProductMessage = mapProductToProductMessage(updatedProduct);
            
            ProductResponse response = ProductResponse.newBuilder()
                    .setProduct(updatedProductMessage)
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            log.info("gRPC response sent for updated product with ID: {}", updatedProduct.getId());
        } catch (Exception e) {
            log.error("Error processing gRPC updateProduct request", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void deleteProduct(DeleteProductRequest request, StreamObserver<DeleteProductResponse> responseObserver) {
        try {
            log.info("gRPC request received to delete product with ID: {}", request.getId());
            
            boolean deleted = productService.deleteProduct(UUID.fromString(request.getId()));
            
            DeleteProductResponse response = DeleteProductResponse.newBuilder()
                    .setSuccess(deleted)
                    .setMessage(deleted ? "Product deleted successfully" : "Product deletion failed")
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            log.info("gRPC response sent for product deletion: {}", deleted ? "successful" : "failed");
        } catch (Exception e) {
            log.error("Error processing gRPC deleteProduct request", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void searchProducts(SearchProductsRequest request, StreamObserver<ProductListResponse> responseObserver) {
        try {
            log.info("gRPC request received to search products with query: {}", request.getQuery());
            
            // Convert category strings to UUIDs
            List<UUID> categoryIds = request.getCategoriesList().stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toList());
            
            List<Product> products = productService.searchProducts(
                    request.getQuery(), categoryIds, request.getFiltersList(), 
                    request.getPage(), request.getSize());
            
            List<ProductMessage> productMessages = products.stream()
                    .map(this::mapProductToProductMessage)
                    .collect(Collectors.toList());
            
            ProductListResponse response = ProductListResponse.newBuilder()
                    .addAllProducts(productMessages)
                    .setTotalCount(productMessages.size())
                    .setPage(request.getPage())
                    .setSize(request.getSize())
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            log.info("gRPC response sent for product search: {} (total: {})", 
                    request.getQuery(), productMessages.size());
        } catch (Exception e) {
            log.error("Error processing gRPC searchProducts request", e);
            responseObserver.onError(e);
        }
    }

    /**
     * Maps a Product entity to a ProductMessage gRPC message
     */
    private ProductMessage mapProductToProductMessage(Product product) {
        if (product == null) {
            return ProductMessage.getDefaultInstance();
        }
        
        List<String> categories = product.getProductCategories().stream()
                .map(pc -> pc.getCategory().getId())
                .map(UUID::toString)
                .collect(Collectors.toList());
        
        List<ProductFeatureMessage> features = new ArrayList<>();
        // We'll leave features empty for now since the mapping is complex
        // and would require additional implementation
        
        ProductMessage.Builder builder = ProductMessage.newBuilder()
                .setId(product.getId().toString())
                .setName(product.getName())
                .setSku(product.getSku());
        
        if (product.getDescription() != null) {
            builder.setDescription(product.getDescription());
        }
        
        // Brand may be stored in metadata
        if (product.getMetadata() != null && product.getMetadata().has("brand")) {
            builder.setBrand(product.getMetadata().get("brand").asText());
        }
        
        // Set price from the product entity
        if (product.getPrice() != null) {
            builder.setPrice(product.getPrice());
        }
        
        // Inventory might be stored in metadata or related entities
        if (product.getMetadata() != null && product.getMetadata().has("inventory")) {
            builder.setInventory(product.getMetadata().get("inventory").asInt());
        }
        
        builder.addAllCategories(categories);
        builder.addAllFeatures(features);
        
        if (product.getMerchant() != null) {
            builder.setMerchantId(product.getMerchant().getId().toString());
        }
        
        if (product.getCatalog() != null) {
            builder.setCatalogId(product.getCatalog().getId().toString());
        }
        
        // No need to set dates in the message
        
        return builder.build();
    }

    /**
     * Maps a ProductFeature entity to a ProductFeatureMessage gRPC message
     */
    private ProductFeatureMessage mapProductFeatureToFeatureMessage(ProductFeature feature) {
        ProductFeatureMessage.Builder builder = ProductFeatureMessage.newBuilder()
                .setFeatureId(feature.getId().toString())
                .setFeatureName(feature.getName());
        
        // We can't directly access feature values here, so we'll leave it with basic info
        // In a real implementation, you might want to query for the value or structure differently
        
        return builder.build();
    }

    /**
     * Maps a ProductMessage gRPC message to a ProductDTO
     */
    private ProductDTO mapProductMessageToProductDTO(ProductMessage productMessage) {
        ProductDTO productDTO = new ProductDTO();
        
        if (productMessage.getId() != null && !productMessage.getId().isEmpty()) {
            productDTO.setId(UUID.fromString(productMessage.getId()));
        }
        
        productDTO.setName(productMessage.getName());
        productDTO.setSku(productMessage.getSku());
        productDTO.setDescription(productMessage.getDescription());
        // Handle brand as metadata
        if (!productMessage.getBrand().isEmpty()) {
            // We'll need to handle this in the service when creating/updating the product
            // by adding it to metadata
        }
        
        // Set categories
        if (!productMessage.getCategoriesList().isEmpty()) {
            List<UUID> categoryIds = new ArrayList<>();
            for (String categoryId : productMessage.getCategoriesList()) {
                try {
                    categoryIds.add(UUID.fromString(categoryId));
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid category ID format: {}", categoryId);
                }
            }
            // Convert list to set
            HashSet<UUID> categoryIdSet = new HashSet<>();
            categoryIdSet.addAll(categoryIds);
            productDTO.setCategoryIds(categoryIdSet);
        }
        
        // Set merchant ID
        if (productMessage.getMerchantId() != null && !productMessage.getMerchantId().isEmpty()) {
            productDTO.setMerchantId(UUID.fromString(productMessage.getMerchantId()));
        }
        
        // Set catalog ID
        if (productMessage.getCatalogId() != null && !productMessage.getCatalogId().isEmpty()) {
            productDTO.setCatalogId(UUID.fromString(productMessage.getCatalogId()));
        }
        
        // Created/updated dates will be set by the service, not from the message
        
        return productDTO;
    }
}