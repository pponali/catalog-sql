package com.nosql.poc.channel.service;

import com.nosql.poc.channel.client.CatalogServiceClient;
import com.nosql.poc.channel.client.VendorServiceClient;
import com.nosql.poc.channel.model.*;
import com.nosql.poc.channel.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelProductService {

    private final CatalogServiceClient catalogServiceClient;
    private final VendorServiceClient vendorServiceClient;
    private final ChannelRepository channelRepository;
    private final ChannelValidationService validationService;

    public ChannelProduct getChannelProduct(String channelId, String productId) {
        // Get base product data
        Product baseProduct = catalogServiceClient.getProduct(productId);
        if (baseProduct == null) {
            throw new ResourceNotFoundException("Product not found: " + productId);
        }

        // Get channel configuration
        ChannelCatalog channelConfig = channelRepository.findByChannelId(channelId)
            .orElseThrow(() -> new ResourceNotFoundException("Channel not found: " + channelId));

        // Get vendor information
        Vendor vendor = vendorServiceClient.getVendor(baseProduct.getVendorId());

        // Validate product for channel
        ValidationResult validation = validationService.validateProductForChannel(baseProduct, channelId);
        if (!validation.isValid()) {
            throw new ValidationException("Product validation failed for channel", validation.getErrors());
        }

        return transformToChannelProduct(baseProduct, channelConfig, vendor);
    }

    public List<ChannelProduct> getChannelProducts(String channelId, String category, Integer page, Integer size) {
        // Set default pagination
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        // Get channel configuration
        ChannelCatalog channelConfig = channelRepository.findByChannelId(channelId)
            .orElseThrow(() -> new ResourceNotFoundException("Channel not found: " + channelId));

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

    public void activateProductInChannel(String channelId, String productId) {
        ChannelProduct channelProduct = getChannelProduct(channelId, productId);
        channelProduct.setActive(true);
        // Additional activation logic
    }

    public void deactivateProductInChannel(String channelId, String productId) {
        ChannelProduct channelProduct = getChannelProduct(channelId, productId);
        channelProduct.setActive(false);
        // Additional deactivation logic
    }

    public void updateChannelPrice(String channelId, String productId, ChannelPrice price) {
        ChannelProduct channelProduct = getChannelProduct(channelId, productId);
        // Validate price
        validationService.validateChannelPrice(price);
        // Update price
        channelProduct.setChannelPrice(price.getAmount());
        channelProduct.setChannelCurrency(price.getCurrency());
        // Additional price update logic
    }

    private ChannelProduct transformToChannelProduct(Product baseProduct, ChannelCatalog channelConfig, Vendor vendor) {
        ChannelProduct channelProduct = new ChannelProduct();
        channelProduct.setChannelId(channelConfig.getChannelId());
        
        // Transform price
        BigDecimal channelPrice = calculateChannelPrice(baseProduct.getBasePrice(), channelConfig, vendor);
        channelProduct.setChannelPrice(channelPrice);
        channelProduct.setChannelCurrency(channelConfig.getDefaultCurrency());

        // Transform description
        String channelDescription = transformDescription(baseProduct.getDescription(), channelConfig);
        channelProduct.setChannelDescription(channelDescription);

        // Set media
        List<ProductMedia> channelMedia = transformMedia(baseProduct.getMedia(), channelConfig);
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

    private BigDecimal calculateChannelPrice(BigDecimal basePrice, ChannelCatalog channelConfig, Vendor vendor) {
        // Apply channel markup/markdown
        BigDecimal channelPrice = basePrice.multiply(channelConfig.getPriceMultiplier());
        
        // Apply vendor commission
        BigDecimal commission = vendor.getCommissionRate();
        channelPrice = channelPrice.multiply(BigDecimal.ONE.add(commission));
        
        return channelPrice;
    }

    private String transformDescription(String baseDescription, ChannelCatalog channelConfig) {
        // Apply channel-specific formatting rules
        String description = baseDescription;
        
        // Add channel-specific content
        if (channelConfig.getDescriptionPrefix() != null) {
            description = channelConfig.getDescriptionPrefix() + description;
        }
        
        return description;
    }

    private List<ProductMedia> transformMedia(List<ProductMedia> baseMedia, ChannelCatalog channelConfig) {
        // Filter and transform media based on channel requirements
        return baseMedia.stream()
            .filter(media -> isMediaSupported(media, channelConfig))
            .map(media -> transformMediaFormat(media, channelConfig))
            .collect(Collectors.toList());
    }

    private String determineAvailability(Product product, Vendor vendor) {
        if (product.getInventory() == null || product.getInventory().getQuantity() <= 0) {
            return "OUT_OF_STOCK";
        }
        
        if (!vendor.isActive()) {
            return "UNAVAILABLE";
        }
        
        return "IN_STOCK";
    }

    private FulfillmentRules buildFulfillmentRules(Vendor vendor, ChannelCatalog channelConfig) {
        FulfillmentRules rules = new FulfillmentRules();
        rules.setDeliveryCapabilities(vendor.getDeliveryCapabilities());
        rules.setServiceableAreas(vendor.getServiceableAreas());
        rules.setChannelRestrictions(channelConfig.getFulfillmentRestrictions());
        return rules;
    }

    private Map<String, Object> transformAttributes(Map<String, Object> baseAttributes, ChannelCatalog channelConfig) {
        Map<String, Object> channelAttributes = new HashMap<>();
        
        // Apply attribute mappings
        channelConfig.getAttributeMappings().forEach(mapping -> {
            Object value = baseAttributes.get(mapping.getSourceAttribute());
            if (value != null) {
                channelAttributes.put(mapping.getTargetAttribute(), transformAttributeValue(value, mapping));
            }
        });
        
        return channelAttributes;
    }

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
