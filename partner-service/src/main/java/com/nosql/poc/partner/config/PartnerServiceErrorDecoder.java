package com.nosql.poc.partner.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public class PartnerServiceErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("Error in Feign client call: {} - Status: {}", methodKey, response.status());
        
        if (response.status() >= 500) {
            return new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE, 
                    "Remote service is unavailable: " + response.reason());
        }
        
        if (response.status() == 404) {
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND, 
                    "Resource not found in remote service: " + response.reason());
        }
        
        if (response.status() == 400) {
            return new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, 
                    "Invalid request parameters: " + response.reason());
        }
        
        if (response.status() == 401 || response.status() == 403) {
            return new ResponseStatusException(
                    HttpStatus.FORBIDDEN, 
                    "Access denied to remote service: " + response.reason());
        }
        
        return defaultErrorDecoder.decode(methodKey, response);
    }
}