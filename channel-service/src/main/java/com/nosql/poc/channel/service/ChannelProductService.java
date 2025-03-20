package com.nosql.poc.channel.service;

import com.nosql.poc.channel.client.CatalogServiceClient;
import com.nosql.poc.channel.client.VendorServiceClient;
import com.nosql.poc.channel.dto.ChannelPrice;
import com.nosql.poc.channel.exception.ResourceNotFoundException;
import com.nosql.poc.channel.exception.ValidationException;
import com.nosql.poc.channel.model.*;
import com.nosql.poc.channel.repository.ChannelProductRepository;
import com.nosql.poc.channel.repository.ChannelRepository;
import com.nosql.poc.channel.validation.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for managing products in channels.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelProductService {

    private final CatalogServiceClient catalogServiceClient;
    private final VendorServiceClient vendorServiceClient;
    private final ChannelRepository channelRepository;
    private final ChannelProductRepository channelProductRepository;
    private final ChannelValidationService validationService;

    /**
     * Get a single product for a specific channel, transforming it as needed.
     * 
     * @param channelId the channel identifier
     * @param productId the product identifier
     * @return the channel-specific view of the product
     */
    public ChannelProduct getChannelProduct(String channelId, String productId) {
        // Get base product data
        Product baseProduct = catalogServiceClient.getProduct(productId);
        if (baseProduct == null) {
            throw new ResourceNotFoundException("Product not found: " + productId);
        }

        // Get channel configuration
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> new ResourceNotFoundException("Channel not found: " + channelId));
        
        // Extract channel catalog configuration
        ChannelCatalog channelConfig = channel.getChannelCatalog();
        if (channelConfig == null) {
            throw new ResourceNotFoundException("No catalog configuration found for channel: " + channelId);
        }

        // Get vendor information
        Vendor vendor = vendorServiceClient.getVendor(baseProduct.getVendorId());

        // Validate product for channel
        ValidationResult validation = validationService.validateProductForChannel(baseProduct, channelId);
        if (!validation.isValid()) {
            throw new ValidationException("Product validation failed for channel", validation.getErrors());
        }

        return transformToChannelProduct(baseProduct, channelConfig, vendor);
    }

    /**
     * Get multiple products for a channel with optional filtering and pagination.
     * 
     * @param channelId the channel identifier
     * @param category optional category filter
     * @param page page number for pagination
     * @param size page size for pagination
     * @return list of channel-specific products
     */
    public List<ChannelProduct> getChannelProducts(String channelId, String category, Integer page, Integer size) {
        // Set default pagination
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        // Get channel configuration
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> new ResourceNotFoundException("Channel not found: " + channelId));
        
        // Extract channel catalog configuration
        ChannelCatalog channelConfig = channel.getChannelCatalog();
        if (channelConfig == null) {
            throw new ResourceNotFoundException("No catalog configuration found for channel: " + channelId);
        }

        // Get products from catalog service
        List<Product> products = catalogServiceClient.getProducts(channelId);
        
        // Filter by category if specified
        if (category != null) {
            products = products.stream()
                .filter(p -> category.equals(p.getCategory()))
                .collect(Collectors.toList());
        }

        // Transform and validate products
        return products.stream()
            .map(product -> {
                try {
                    Vendor vendor = vendorServiceClient.getVendor(product.getVendorId());
                    return transformToChannelProduct(product, channelConfig, vendor);
                } catch (Exception e) {
                    // Log error and skip product
                    log.error("Error transforming product: " + product.getId(), e);
                    return null;
                }
            })
            .filter(p -> p != null)
            .collect(Collectors.toList());
    }

    /**
     * Activate a product in a channel.
     * 
     * @param channelId the channel identifier
     * @param productId the product identifier
     */
    public void activateProductInChannel(String channelId, String productId) {
        ChannelProduct channelProduct = getChannelProduct(channelId, productId);
        channelProduct.setActive(true);
        channelProductRepository.save(channelProduct);
    }

    /**
     * Deactivate a product in a channel.
     * 
     * @param channelId the channel identifier
     * @param productId the product identifier
     */
    public void deactivateProductInChannel(String channelId, String productId) {
        ChannelProduct channelProduct = getChannelProduct(channelId, productId);
        channelProduct.setActive(false);
        channelProductRepository.save(channelProduct);
    }

    /**
     * Update the price of a product in a channel.
     * 
     * @param channelId the channel identifier
     * @param productId the product identifier
     * @param price the new price information
     */
    public void updateChannelPrice(String channelId, String productId, ChannelPrice price) {
        ChannelProduct channelProduct = getChannelProduct(channelId, productId);
        // Validate price
        validationService.validateChannelPrice(price);
        // Update price
        channelProduct.setChannelPrice(price.getAmount());
        channelProduct.setChannelCurrency(price.getCurrency());
        channelProductRepository.save(channelProduct);
    }

    /**
     * Transform a base product into a channel-specific product.
     * 
     * @param baseProduct the original product
     * @param channelConfig the channel configuration
     * @param vendor the vendor information
     * @return a channel-specific view of the product
     */
    private ChannelProduct transformToChannelProduct(Product baseProduct, ChannelCatalog channelConfig, Vendor vendor) {
        ChannelProduct channelProduct = new ChannelProduct();
        channelProduct.setProductId(baseProduct.getId());
        channelProduct.setChannelId(channelConfig.getChannelId());
        channelProduct.setBaseProductName(baseProduct.getName());
        
        // Transform price
        BigDecimal channelPrice = calculateChannelPrice(baseProduct.getBasePrice(), channelConfig, vendor);
        channelProduct.setChannelPrice(channelPrice);
        channelProduct.setChannelCurrency(channelConfig.getDefaultCurrency());

        // Transform description
        String channelDescription = transformDescription(baseProduct.getDescription(), channelConfig);
        channelProduct.setChannelDescription(channelDescription);

        // Set media
        List<String> channelMedia = transformMedia(baseProduct.getMedia(), channelConfig);
        channelProduct.setChannelMedia(channelMedia);

        // Set availability
        String availabilityStatus = determineAvailability(baseProduct, vendor);
        channelProduct.setAvailabilityStatus(availabilityStatus);

        // Set fulfillment rules
        FulfillmentRules fulfillmentRules = buildFulfillmentRules(vendor, channelConfig);
        channelProduct.setFulfillmentRules(fulfillmentRules);

        // Set channel-specific attributes
        Map<String, Object> channelAttributes = transformAttributes(baseProduct.getAttributes(), channelConfig);
        channelProduct.setChannelAttributes(channelAttributes);

        // Set display priority
        channelProduct.setDisplayPriority(calculateDisplayPriority(baseProduct, channelConfig));

        return channelProduct;
    }

    /**
     * Calculate the channel-specific price.
     */
    private BigDecimal calculateChannelPrice(BigDecimal basePrice, ChannelCatalog channelConfig, Vendor vendor) {
        // Apply channel markup/markdown
        BigDecimal channelPrice = basePrice;
        
        if (channelConfig.getPriceMultiplier() != null) {
            channelPrice = basePrice.multiply(channelConfig.getPriceMultiplier());
        }
        
        // Apply vendor commission
        if (vendor.getCommissionRate() != null) {
            BigDecimal commission = vendor.getCommissionRate();
            channelPrice = channelPrice.multiply(BigDecimal.ONE.add(commission));
        }
        
        return channelPrice;
    }

    /**
     * Transform the product description for the channel.
     */
    private String transformDescription(String baseDescription, ChannelCatalog channelConfig) {
        // Apply channel-specific formatting rules
        if (baseDescription == null) {
            return "";
        }
        
        String description = baseDescription;
        
        // Add channel-specific content
        if (channelConfig.getDescriptionPrefix() != null) {
            description = channelConfig.getDescriptionPrefix() + description;
        }
        
        return description;
    }

    /**
     * Transform product media for the channel.
     */
    private List<String> transformMedia(List<ProductMedia> baseMedia, ChannelCatalog channelConfig) {
        // Filter and transform media based on channel requirements
        if (baseMedia == null) {
            return List.of();
        }
        
        return baseMedia.stream()
            .filter(media -> isMediaSupported(media, channelConfig))
            .map(ProductMedia::getUrl)
            .collect(Collectors.toList());
    }

    /**
     * Check if the given media is supported by the channel.
     */
    private boolean isMediaSupported(ProductMedia media, ChannelCatalog channelConfig) {
        // Default implementation assumes all media is supported
        return true;
    }

    /**
     * Determine product availability status.
     */
    private String determineAvailability(Product product, Vendor vendor) {
        if (product.getInventory() == null || product.getInventory().getQuantity() <= 0) {
            return "OUT_OF_STOCK";
        }
        
        if (!vendor.isActive()) {
            return "UNAVAILABLE";
        }
        
        return "IN_STOCK";
    }

    /**
     * Build fulfillment rules for a product in a channel.
     */
    private FulfillmentRules buildFulfillmentRules(Vendor vendor, ChannelCatalog channelConfig) {
        FulfillmentRules rules = new FulfillmentRules();
        rules.setDeliveryCapabilities(vendor.getDeliveryCapabilities());
        rules.setServiceableAreas(vendor.getServiceableAreas());
        
        if (channelConfig.getFulfillmentRestrictions() != null) {
            rules.setChannelRestrictions(channelConfig.getFulfillmentRestrictions());
        }
        
        return rules;
    }

    /**
     * Transform product attributes for a channel.
     */
    private Map<String, Object> transformAttributes(Map<String, Object> baseAttributes, ChannelCatalog channelConfig) {
        Map<String, Object> channelAttributes = new HashMap<>();
        
        if (baseAttributes == null || channelConfig.getAttributeMappings() == null) {
            return channelAttributes;
        }
        
        // Apply attribute mappings
        channelConfig.getAttributeMappings().forEach(mapping -> {
            Object value = baseAttributes.get(mapping.getSourceAttribute());
            if (value != null) {
                channelAttributes.put(mapping.getTargetAttribute(), transformAttributeValue(value, mapping));
            }
        });
        
        return channelAttributes;
    }

    /**
     * Transform an individual attribute value.
     */
    private Object transformAttributeValue(Object value, AttributeMapping mapping) {
        // Default implementation just returns the original value
        return value;
    }

    /**
     * Calculate the display priority for a product.
     */
    private Integer calculateDisplayPriority(Product product, ChannelCatalog channelConfig) {
        // Calculate priority based on various factors
        int priority = 0;
        
        // Factor in product quality score
        if (product.getQualityScore() != null) {
            priority += product.getQualityScore().getScore() * 10;
        }
        
        // Factor in inventory levels
        if (product.getInventory() != null && product.getInventory().getQuantity() > 0) {
            priority += 5;
        }
        
        return priority;
    }
}
