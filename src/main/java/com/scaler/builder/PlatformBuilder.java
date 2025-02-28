package com.scaler.builder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.scaler.entity.Channel;
import com.scaler.entity.Platform;
import com.scaler.entity.enums.PlatformType;

public class PlatformBuilder {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private PlatformBuilder() {
        // Private constructor to prevent instantiation
    }

    public static Platform createMobileAppAndroid(Channel channel, String code) {
        ObjectNode metadata = objectMapper.createObjectNode();
        metadata.put("os", "Android");
        metadata.put("minSdkVersion", "24");
        metadata.put("targetSdkVersion", "33");

        return Platform.builder()
                .code(code)
                .name("Android Mobile App")
                .type(PlatformType.MOBILE_APP_ANDROID)
                .description("Native Android application")
                .version("1.0.0")
                .minVersion("1.0.0")
                .isEnabled(true)
                .metadata(metadata)
                .channel(channel)
                .createdBy("SYSTEM")
                .build();
    }

    public static Platform createMobileAppIOS(Channel channel, String code) {
        ObjectNode metadata = objectMapper.createObjectNode();
        metadata.put("os", "iOS");
        metadata.put("minOsVersion", "13.0");
        metadata.put("deviceTypes", "iPhone,iPad");

        return Platform.builder()
                .code(code)
                .name("iOS Mobile App")
                .type(PlatformType.MOBILE_APP_IOS)
                .description("Native iOS application")
                .version("1.0.0")
                .minVersion("1.0.0")
                .isEnabled(true)
                .metadata(metadata)
                .channel(channel)
                .createdBy("SYSTEM")
                .build();
    }

    public static Platform createDesktopWeb(Channel channel, String code) {
        ObjectNode metadata = objectMapper.createObjectNode();
        metadata.put("supportedBrowsers", "Chrome,Firefox,Safari,Edge");
        metadata.put("responsive", true);

        return Platform.builder()
                .code(code)
                .name("Desktop Website")
                .type(PlatformType.DESKTOP_WEB)
                .description("Desktop web application")
                .version("1.0.0")
                .minVersion("1.0.0")
                .isEnabled(true)
                .metadata(metadata)
                .channel(channel)
                .createdBy("SYSTEM")
                .build();
    }

    public static Platform createMobileWeb(Channel channel, String code) {
        ObjectNode metadata = objectMapper.createObjectNode();
        metadata.put("supportedBrowsers", "Chrome Mobile,Safari Mobile");
        metadata.put("responsive", true);
        metadata.put("pwa", true);

        return Platform.builder()
                .code(code)
                .name("Mobile Website")
                .type(PlatformType.MOBILE_WEB)
                .description("Mobile web application")
                .version("1.0.0")
                .minVersion("1.0.0")
                .isEnabled(true)
                .metadata(metadata)
                .channel(channel)
                .createdBy("SYSTEM")
                .build();
    }

    public static Platform createKiosk(Channel channel, String code) {
        ObjectNode metadata = objectMapper.createObjectNode();
        metadata.put("deviceType", "Kiosk");
        metadata.put("touchEnabled", true);
        metadata.put("printerEnabled", true);

        return Platform.builder()
                .code(code)
                .name("Store Kiosk")
                .type(PlatformType.KIOSK)
                .description("In-store kiosk application")
                .version("1.0.0")
                .minVersion("1.0.0")
                .isEnabled(true)
                .metadata(metadata)
                .channel(channel)
                .createdBy("SYSTEM")
                .build();
    }
}
