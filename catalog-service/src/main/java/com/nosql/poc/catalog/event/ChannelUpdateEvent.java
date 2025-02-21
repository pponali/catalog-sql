package com.nosql.poc.catalog.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelUpdateEvent {
    private String channelId;
    private String updateType; // CONFIG_UPDATE, STATUS_UPDATE, MAPPING_UPDATE
    private String userId;
    private long timestamp;
}
