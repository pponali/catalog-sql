package com.scaler.productread.sync;

import com.scaler.productread.document.ProductDocument;
import com.scaler.productread.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Maps product data from catalog service format to Elasticsearch document format.
 */
@Slf4j
@Component
public class ProductMapper {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    
    /**
     * Maps product data from catalog service to ProductDocument.
     *
     * @param productDto Product data from catalog service
     * @return ProductDocument for Elasticsearch
     */
    public ProductDocument mapToProductDocument(ProductDTO productDto) {
        try {
            ProductDocument document = new ProductDocument();
            
            // Basic fields
            document.setId(productDto.getId());
            document.setName(productDto.getName());
            document.setDescription(productDto.getDescription());
            document.setSku(productDto.getSku());
            document.setBrand(productDto.getBrand());
            document.setActive(productDto.isActive());
            
            // Dates
            document.setCreatedAt(productDto.getCreatedAt());
            document.setUpdatedAt(productDto.getUpdatedAt());
            
            // Map categories, features, sellers, channels, images using existing methods
            if (productDto.getCategoryDetails() != null) {
                mapCategories(document, productDto);
            }
            
            if (productDto.getFeatures() != null) {
                mapFeatures(document, productDto);
            }
            
            if (productDto.getSellers() != null) {
                mapSellers(document, productDto);
            }
            
            if (productDto.getChannels() != null) {
                mapChannels(document, productDto);
            }
            
            if (productDto.getImages() != null) {
                mapImages(document, productDto);
            }
            
            // Price and inventory from DTO directly if available
            document.setMinPrice(productDto.getMinPrice());
            document.setMaxPrice(productDto.getMaxPrice());
            document.setTotalStock(productDto.getTotalStock());
            
            return document;
        } catch (Exception e) {
            log.error("Error mapping product data to document: {}", e.getMessage(), e);
            throw new RuntimeException("Error mapping product data", e);
        }
    }
    
    /**
     * Legacy method for backward compatibility
     */
    public ProductDocument mapToProductDocument(Map<String, Object> productData) {
        try {
            ProductDocument document = new ProductDocument();
            
            // Basic fields
            document.setId(UUID.fromString(productData.get("id").toString()));
            document.setName((String) productData.get("name"));
            document.setDescription((String) productData.get("description"));
            document.setSku((String) productData.get("sku"));
            document.setBrand((String) productData.get("brand"));
            document.setActive((Boolean) productData.getOrDefault("active", true));
            
            // Dates
            if (productData.containsKey("createdAt") && productData.get("createdAt") != null) {
                document.setCreatedAt(parseDateTime(productData.get("createdAt").toString()));
            }
            
            if (productData.containsKey("updatedAt") && productData.get("updatedAt") != null) {
                document.setUpdatedAt(parseDateTime(productData.get("updatedAt").toString()));
            }
            
            // Map categories
            mapCategories(document, productData);
            
            // Map features
            mapFeatures(document, productData);
            
            // Map sellers
            mapSellers(document, productData);
            
            // Map channels
            mapChannels(document, productData);
            
            // Map images
            mapImages(document, productData);
            
            return document;
        } catch (Exception e) {
            log.error("Error mapping product data to document: {}", e.getMessage(), e);
            throw new RuntimeException("Error mapping product data", e);
        }
    }
    
    /**
     * Maps category data to the document from ProductDTO.
     */
    private void mapCategories(ProductDocument document, ProductDTO productDto) {
        try {
            Set<String> categoryIds = new HashSet<>();
            List<ProductDocument.CategoryInfo> categoryDetails = new ArrayList<>();
            
            // Convert categories if available
            if (productDto.getCategories() != null) {
                productDto.getCategories().forEach(id -> categoryIds.add(id.toString()));
            }
            
            // Convert category details
            if (productDto.getCategoryDetails() != null) {
                for (Map<String, Object> detail : productDto.getCategoryDetails()) {
                    ProductDocument.CategoryInfo categoryInfo = ProductDocument.CategoryInfo.builder()
                            .id(UUID.fromString(detail.get("id").toString()))
                            .name((String) detail.get("name"))
                            .path((String) detail.getOrDefault("path", ""))
                            .level(detail.containsKey("level") ? Integer.parseInt(detail.get("level").toString()) : 0)
                            .build();
                    
                    categoryDetails.add(categoryInfo);
                    categoryIds.add(detail.get("id").toString());
                }
            }
            
            document.setCategories(categoryIds);
            document.setCategoryDetails(categoryDetails);
        } catch (Exception e) {
            log.error("Error mapping category data from ProductDTO: {}", e.getMessage());
        }
    }
    
    /**
     * Maps feature data to the document from ProductDTO.
     */
    private void mapFeatures(ProductDocument document, ProductDTO productDto) {
        try {
            List<Map<String, Object>> features = productDto.getFeatures();
            List<ProductDocument.ProductFeature> productFeatures = features.stream()
                    .map(feature -> ProductDocument.ProductFeature.builder()
                            .featureId(UUID.fromString(feature.get("featureId").toString()))
                            .name((String) feature.get("name"))
                            .code((String) feature.get("code"))
                            .value((String) feature.get("value"))
                            .searchable((Boolean) feature.getOrDefault("searchable", false))
                            .filterable((Boolean) feature.getOrDefault("filterable", false))
                            .build())
                    .collect(Collectors.toList());
            
            document.setFeatures(productFeatures);
        } catch (Exception e) {
            log.error("Error mapping feature data from ProductDTO: {}", e.getMessage());
        }
    }
    
    /**
     * Maps seller data to the document from ProductDTO.
     */
    private void mapSellers(ProductDocument document, ProductDTO productDto) {
        try {
            List<Map<String, Object>> sellers = productDto.getSellers();
            List<ProductDocument.SellerInfo> sellerInfos = sellers.stream()
                    .map(seller -> {
                        Double price = seller.containsKey("price") ? 
                                Double.parseDouble(seller.get("price").toString()) : null;
                                
                        Double salePrice = seller.containsKey("salePrice") ? 
                                Double.parseDouble(seller.get("salePrice").toString()) : null;
                                
                        Integer inventory = seller.containsKey("inventory") ? 
                                Integer.parseInt(seller.get("inventory").toString()) : 0;
                        
                        return ProductDocument.SellerInfo.builder()
                                .sellerId(UUID.fromString(seller.get("sellerId").toString()))
                                .name((String) seller.get("name"))
                                .price(price)
                                .salePrice(salePrice)
                                .inventory(inventory)
                                .currency((String) seller.getOrDefault("currency", "INR"))
                                .available((Boolean) seller.getOrDefault("available", true))
                                .build();
                    })
                    .collect(Collectors.toList());
            
            document.setSellers(sellerInfos);
            
            // Calculate min and max prices if not provided
            if (document.getMinPrice() == null && document.getMaxPrice() == null && !sellerInfos.isEmpty()) {
                double minPrice = sellerInfos.stream()
                        .filter(seller -> seller.getPrice() != null)
                        .mapToDouble(ProductDocument.SellerInfo::getPrice)
                        .min()
                        .orElse(0.0);
                
                double maxPrice = sellerInfos.stream()
                        .filter(seller -> seller.getPrice() != null)
                        .mapToDouble(ProductDocument.SellerInfo::getPrice)
                        .max()
                        .orElse(0.0);
                
                document.setMinPrice(minPrice);
                document.setMaxPrice(maxPrice);
            }
            
            // Calculate total inventory if not provided
            if (document.getTotalStock() == null && !sellerInfos.isEmpty()) {
                int totalStock = sellerInfos.stream()
                        .mapToInt(ProductDocument.SellerInfo::getInventory)
                        .sum();
                
                document.setTotalStock(totalStock);
            }
        } catch (Exception e) {
            log.error("Error mapping seller data from ProductDTO: {}", e.getMessage());
        }
    }
    
    /**
     * Maps channel data to the document from ProductDTO.
     */
    private void mapChannels(ProductDocument document, ProductDTO productDto) {
        try {
            List<Map<String, Object>> channels = productDto.getChannels();
            List<ProductDocument.ChannelInfo> channelInfos = channels.stream()
                    .map(channel -> ProductDocument.ChannelInfo.builder()
                            .channelId(UUID.fromString(channel.get("channelId").toString()))
                            .name((String) channel.get("name"))
                            .type((String) channel.getOrDefault("type", ""))
                            .available((Boolean) channel.getOrDefault("available", true))
                            .build())
                    .collect(Collectors.toList());
            
            document.setChannels(channelInfos);
        } catch (Exception e) {
            log.error("Error mapping channel data from ProductDTO: {}", e.getMessage());
        }
    }
    
    /**
     * Maps image data to the document from ProductDTO.
     */
    private void mapImages(ProductDocument document, ProductDTO productDto) {
        try {
            List<Map<String, Object>> images = productDto.getImages();
            List<ProductDocument.ImageInfo> imageInfos = images.stream()
                    .map(image -> {
                        Integer sortOrder = image.containsKey("sortOrder") ? 
                                Integer.parseInt(image.get("sortOrder").toString()) : 0;
                                
                        return ProductDocument.ImageInfo.builder()
                                .url((String) image.get("url"))
                                .type((String) image.getOrDefault("type", "PRIMARY"))
                                .sortOrder(sortOrder)
                                .build();
                    })
                    .collect(Collectors.toList());
            
            document.setImages(imageInfos);
        } catch (Exception e) {
            log.error("Error mapping image data from ProductDTO: {}", e.getMessage());
        }
    }
    
    /**
     * Maps category data to the document.
     */
    @SuppressWarnings("unchecked")
    private void mapCategories(ProductDocument document, Map<String, Object> productData) {
        try {
            Set<String> categoryIds = new HashSet<>();
            List<ProductDocument.CategoryInfo> categoryDetails = new ArrayList<>();
            
            // Direct category mappings
            if (productData.containsKey("categories")) {
                List<Map<String, Object>> categories = (List<Map<String, Object>>) productData.get("categories");
                for (Map<String, Object> category : categories) {
                    String categoryId = category.get("categoryId").toString();
                    categoryIds.add(categoryId);
                }
            }
            
            // Detailed category information
            if (productData.containsKey("categoryDetails")) {
                List<Map<String, Object>> details = (List<Map<String, Object>>) productData.get("categoryDetails");
                
                for (Map<String, Object> detail : details) {
                    ProductDocument.CategoryInfo categoryInfo = ProductDocument.CategoryInfo.builder()
                            .id(UUID.fromString(detail.get("id").toString()))
                            .name((String) detail.get("name"))
                            .path((String) detail.getOrDefault("path", ""))
                            .level(detail.containsKey("level") ? Integer.parseInt(detail.get("level").toString()) : 0)
                            .build();
                    
                    categoryDetails.add(categoryInfo);
                    categoryIds.add(detail.get("id").toString());
                }
            }
            
            document.setCategories(categoryIds);
            document.setCategoryDetails(categoryDetails);
        } catch (Exception e) {
            log.error("Error mapping category data: {}", e.getMessage());
        }
    }
    
    /**
     * Maps feature data to the document.
     */
    @SuppressWarnings("unchecked")
    private void mapFeatures(ProductDocument document, Map<String, Object> productData) {
        try {
            if (!productData.containsKey("features")) {
                return;
            }
            
            List<Map<String, Object>> features = (List<Map<String, Object>>) productData.get("features");
            List<ProductDocument.ProductFeature> productFeatures = features.stream()
                    .map(feature -> ProductDocument.ProductFeature.builder()
                            .featureId(UUID.fromString(feature.get("featureId").toString()))
                            .name((String) feature.get("name"))
                            .code((String) feature.get("code"))
                            .value((String) feature.get("value"))
                            .searchable((Boolean) feature.getOrDefault("searchable", false))
                            .filterable((Boolean) feature.getOrDefault("filterable", false))
                            .build())
                    .collect(Collectors.toList());
            
            document.setFeatures(productFeatures);
        } catch (Exception e) {
            log.error("Error mapping feature data: {}", e.getMessage());
        }
    }
    
    /**
     * Maps seller data to the document.
     */
    @SuppressWarnings("unchecked")
    private void mapSellers(ProductDocument document, Map<String, Object> productData) {
        try {
            if (!productData.containsKey("sellers")) {
                return;
            }
            
            List<Map<String, Object>> sellers = (List<Map<String, Object>>) productData.get("sellers");
            List<ProductDocument.SellerInfo> sellerInfos = sellers.stream()
                    .map(seller -> {
                        Double price = seller.containsKey("price") ? 
                                Double.parseDouble(seller.get("price").toString()) : null;
                                
                        Double salePrice = seller.containsKey("salePrice") ? 
                                Double.parseDouble(seller.get("salePrice").toString()) : null;
                                
                        Integer inventory = seller.containsKey("inventory") ? 
                                Integer.parseInt(seller.get("inventory").toString()) : 0;
                        
                        return ProductDocument.SellerInfo.builder()
                                .sellerId(UUID.fromString(seller.get("sellerId").toString()))
                                .name((String) seller.get("name"))
                                .price(price)
                                .salePrice(salePrice)
                                .inventory(inventory)
                                .currency((String) seller.getOrDefault("currency", "INR"))
                                .available((Boolean) seller.getOrDefault("available", true))
                                .build();
                    })
                    .collect(Collectors.toList());
            
            document.setSellers(sellerInfos);
            
            // Calculate min and max prices
            if (!sellerInfos.isEmpty()) {
                double minPrice = sellerInfos.stream()
                        .filter(seller -> seller.getPrice() != null)
                        .mapToDouble(ProductDocument.SellerInfo::getPrice)
                        .min()
                        .orElse(0.0);
                
                double maxPrice = sellerInfos.stream()
                        .filter(seller -> seller.getPrice() != null)
                        .mapToDouble(ProductDocument.SellerInfo::getPrice)
                        .max()
                        .orElse(0.0);
                
                document.setMinPrice(minPrice);
                document.setMaxPrice(maxPrice);
                
                // Calculate total inventory
                int totalStock = sellerInfos.stream()
                        .mapToInt(ProductDocument.SellerInfo::getInventory)
                        .sum();
                
                document.setTotalStock(totalStock);
            }
        } catch (Exception e) {
            log.error("Error mapping seller data: {}", e.getMessage());
        }
    }
    
    /**
     * Maps channel data to the document.
     */
    @SuppressWarnings("unchecked")
    private void mapChannels(ProductDocument document, Map<String, Object> productData) {
        try {
            if (!productData.containsKey("channels")) {
                return;
            }
            
            List<Map<String, Object>> channels = (List<Map<String, Object>>) productData.get("channels");
            List<ProductDocument.ChannelInfo> channelInfos = channels.stream()
                    .map(channel -> ProductDocument.ChannelInfo.builder()
                            .channelId(UUID.fromString(channel.get("channelId").toString()))
                            .name((String) channel.get("name"))
                            .type((String) channel.getOrDefault("type", ""))
                            .available((Boolean) channel.getOrDefault("available", true))
                            .build())
                    .collect(Collectors.toList());
            
            document.setChannels(channelInfos);
        } catch (Exception e) {
            log.error("Error mapping channel data: {}", e.getMessage());
        }
    }
    
    /**
     * Maps image data to the document.
     */
    @SuppressWarnings("unchecked")
    private void mapImages(ProductDocument document, Map<String, Object> productData) {
        try {
            if (!productData.containsKey("images")) {
                return;
            }
            
            List<Map<String, Object>> images = (List<Map<String, Object>>) productData.get("images");
            List<ProductDocument.ImageInfo> imageInfos = images.stream()
                    .map(image -> {
                        Integer sortOrder = image.containsKey("sortOrder") ? 
                                Integer.parseInt(image.get("sortOrder").toString()) : 0;
                                
                        return ProductDocument.ImageInfo.builder()
                                .url((String) image.get("url"))
                                .type((String) image.getOrDefault("type", "PRIMARY"))
                                .sortOrder(sortOrder)
                                .build();
                    })
                    .collect(Collectors.toList());
            
            document.setImages(imageInfos);
        } catch (Exception e) {
            log.error("Error mapping image data: {}", e.getMessage());
        }
    }
    
    /**
     * Parses date-time string to LocalDateTime.
     */
    private LocalDateTime parseDateTime(String dateTimeStr) {
        try {
            return LocalDateTime.parse(dateTimeStr, DATE_FORMATTER);
        } catch (Exception e) {
            log.warn("Error parsing date-time: {}", dateTimeStr);
            return LocalDateTime.now();
        }
    }
}