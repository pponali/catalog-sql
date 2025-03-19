package com.scaler.entity;

public enum ProductType {
    SIMPLE,           // Basic product
    CONFIGURABLE,     // Product with variants (e.g., T-shirt with different sizes/colors)
    BUNDLE,          // Bundle of products sold together
    VIRTUAL,         // Downloadable or service products
    VARIANT          // Child product of a configurable product
}
