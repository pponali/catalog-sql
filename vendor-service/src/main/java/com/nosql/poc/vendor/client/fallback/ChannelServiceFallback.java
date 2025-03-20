package com.nosql.poc.vendor.client.fallback;

import com.nosql.poc.vendor.client.ChannelServiceClient;
import com.nosql.poc.vendor.model.Channel;
import com.nosql.poc.vendor.model.VendorChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Fallback implementation for ChannelServiceClient to handle service unavailability.
 */
@Component
public class ChannelServiceFallback implements ChannelServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(ChannelServiceFallback.class);
    
    @Override
    public Channel getChannel(String channelId) {
        logger.warn("Falling back to stub response for getChannel({})", channelId);
        Channel fallbackChannel = new Channel();
        fallbackChannel.setId(channelId);
        fallbackChannel.setName("Fallback Channel");
        fallbackChannel.setCode("FALLBACK");
        fallbackChannel.setType("FALLBACK");
        fallbackChannel.setActive(false);
        return fallbackChannel;
    }
    
    @Override
    public List<Channel> getChannels(String vendorId) {
        logger.warn("Falling back to stub response for getChannels({})", vendorId);
        return new ArrayList<>(); // Return empty list as fallback
    }
    
    @Override
    public VendorChannel addVendorToChannel(String channelId, String vendorId, VendorChannel vendorChannel) {
        logger.warn("Falling back for addVendorToChannel({}, {})", channelId, vendorId);
        VendorChannel fallback = new VendorChannel();
        fallback.setChannelId(channelId);
        fallback.setChannelName("Fallback Channel");
        fallback.setChannelType("MARKETPLACE");
        fallback.setIsActive(false);
        return fallback;
    }
    
    @Override
    public VendorChannel updateVendorInChannel(String channelId, String vendorId, VendorChannel vendorChannel) {
        logger.warn("Falling back for updateVendorInChannel({}, {})", channelId, vendorId);
        // Return the same object since we can't make actual update
        return vendorChannel;
    }
}