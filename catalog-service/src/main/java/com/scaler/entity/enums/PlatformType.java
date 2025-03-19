package com.scaler.entity.enums;

public enum PlatformType {
    MOBILE_APP_ANDROID("Android App"),
    MOBILE_APP_IOS("iOS App"),
    DESKTOP_WEB("Desktop Web"),
    MOBILE_WEB("Mobile Web"),
    KIOSK("Kiosk"),
    POS("Point of Sale"),
    TABLET("Tablet");

    private final String displayName;

    PlatformType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
