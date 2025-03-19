package com.scaler.enums;

public enum BusinessUnit {
    TATA_CLIQ_FASHION("Tata CLiQ Fashion", "fashion"),
    TATA_DIGITAL("Tata Digital", "digital"),
    BIGBASKET("BigBasket", "grocery"),
    TATA_1MG("Tata 1mg", "pharmacy"),
    TANISHQ("Tanishq", "jewelry");

    private final String displayName;
    private final String domain;

    BusinessUnit(String displayName, String domain) {
        this.displayName = displayName;
        this.domain = domain;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDomain() {
        return domain;
    }
}
