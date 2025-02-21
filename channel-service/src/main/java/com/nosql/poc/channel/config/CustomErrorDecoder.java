package com.nosql.poc.channel.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nosql.poc.channel.exception.ServiceException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String errorMessage = new String(response.body().asInputStream().readAllBytes());
            return new ServiceException(response.status(), errorMessage);
        } catch (Exception e) {
            log.error("Error decoding response", e);
            return new ServiceException(response.status(), "Unknown error occurred");
        }
    }
}
