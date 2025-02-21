package com.nosql.poc.channel.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorUpdateEvent {
    private String vendorId;
    private String channelId;
    private String updateType; // ONBOARD, UPDATE, SUSPEND, TERMINATE
    private String userId;
    private long timestamp;
}
