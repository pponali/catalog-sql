package com.nosql.poc.partner.dto;

import lombok.Data;

/**
 * Standard API response wrapper
 *
 * @param <T> Type of data being returned
 */
@Data
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private String errorCode;
    private T data;
}