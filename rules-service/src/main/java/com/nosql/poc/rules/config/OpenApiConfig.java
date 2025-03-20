package com.nosql.poc.rules.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path:/api}")
    private String contextPath;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(getApiInfo())
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8087" + contextPath)
                                .description("Local development server")
                ));
    }

    private Info getApiInfo() {
        return new Info()
                .title("Rules Service API")
                .description("API endpoints for managing validation and business rules")
                .version("1.0.0")
                .contact(new Contact()
                        .name("Rules Service Team")
                        .email("rules-service@example.com"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0.html"));
    }
}