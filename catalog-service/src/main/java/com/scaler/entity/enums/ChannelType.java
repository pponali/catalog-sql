package com.scaler.entity.enums;

public enum ChannelType {
    QUICK_COMMERCE("Quick Commerce"),
    POS("Point of Sale"),
    MARKETPLACE("Marketplace"),
    MOBILE_APP("Mobile Apps"),
    SOCIAL_COMMERCE("Social Commerce"),
    PHYSICAL_STORE("Physical Stores"),
    ECOMMERCE("E-commerce");

    private final String displayName;

    ChannelType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
