package com.scaler.constants;

/**
 * Constants for feature-related configurations and common values.
 * Centralizes feature-related constants for better maintainability.
 */
public final class FeatureConstants {
    private FeatureConstants() {
        // Private constructor to prevent instantiation
    }

    // Feature Types
    public static final String SPECIFICATION = "SPECIFICATION";
    public static final String ENUM = "ENUM";
    public static final String STRING = "STRING";
    public static final String DROPDOWN = "DROPDOWN";

    // Feature Groups
    public static final String TECH_SPECS = "Technical Specifications";
    
    // Feature Codes (Tata CLiQ Electronics - Laptop)
    public static final String LAPTOP_PROCESSOR = "TCLQ-ELEC-LAP-PROC";
    public static final String LAPTOP_RAM = "TCLQ-ELEC-LAP-RAM";
    public static final String LAPTOP_STORAGE = "TCLQ-ELEC-LAP-STO";

    // Validation Patterns
    public static final String RAM_PATTERN = "^\\d+\\s*GB$";
    public static final String STORAGE_PATTERN = "^\\d+\\s*(?:GB|TB)$";

    // Default Values
    public static final String DEFAULT_RAM = "8 GB";
    public static final String DEFAULT_STORAGE = "512 GB";

    // Allowed Values
    public static final String PROCESSOR_VALUES = "Apple M1,Apple M2,Apple M2 Pro,Apple M2 Max," +
            "Intel Core i3,Intel Core i5,Intel Core i7,Intel Core i9," +
            "AMD Ryzen 5,AMD Ryzen 7,AMD Ryzen 9";
    
    public static final String RAM_VALUES = "4 GB,8 GB,16 GB,32 GB,64 GB,128 GB";
    public static final String STORAGE_VALUES = "256 GB,512 GB,1 TB,2 TB,4 TB";

    // System User
    public static final String SYSTEM_USER = "system";
}
