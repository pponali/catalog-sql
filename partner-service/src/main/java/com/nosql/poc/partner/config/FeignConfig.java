package com.nosql.poc.partner.config;

import feign.Logger;
import feign.Request;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableFeignClients(basePackages = "com.nosql.poc.partner.client")
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
    
    @Bean
    public ErrorDecoder errorDecoder() {
        return new PartnerServiceErrorDecoder();
    }
    
    @Bean
    public Request.Options options() {
        return new Request.Options(
                5, TimeUnit.SECONDS, // connection timeout
                15, TimeUnit.SECONDS, // read timeout
                true); // follow redirects
    }
}