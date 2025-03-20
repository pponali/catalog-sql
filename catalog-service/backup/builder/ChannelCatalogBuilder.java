package com.scaler.builder;

import com.scaler.entity.Channel;
import com.scaler.entity.ChannelCatalog;
import com.scaler.entity.Catalog;

public class ChannelCatalogBuilder {
    
    private ChannelCatalogBuilder() {
        // Private constructor to prevent instantiation
    }

    public static ChannelCatalog createChannelCatalog(Channel channel, Catalog catalog, boolean isDefault) {
        return ChannelCatalog.builder()
                .channel(channel)
                .catalog(catalog)
                .isDefault(isDefault)
                .isEnabled(true)
                .displayOrder(1)
                .createdBy("SYSTEM")
                .build();
    }
}
