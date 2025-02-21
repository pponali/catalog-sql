package com.nosql.poc.copy.model;

public enum CopyStatus {
    PENDING,
    VALIDATING,
    TRANSFORMING,
    COPYING,
    COMPLETED,
    FAILED,
    PARTIALLY_COMPLETED
}
