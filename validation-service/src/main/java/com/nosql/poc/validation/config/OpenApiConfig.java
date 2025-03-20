package com.nosql.poc.validation.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Configuration for OpenAPI / Swagger documentation.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures the OpenAPI documentation.
     *
     * @return The OpenAPI configuration
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Enhanced Validation Service API")
                        .description("REST API for the Enhanced Validation Service, providing validation of product " +
                                "data with advanced features like cross-field validation, conditional rules, " +
                                "calculated fields, and rule dependencies.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Validation Team")
                                .email("validation@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Service Documentation")
                        .url("https://github.com/pponali/catalog-sql/validation-service"))
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8085").description("Local Development Server"),
                        new Server().url("https://validation-dev.example.com").description("Development Server"),
                        new Server().url("https://validation.example.com").description("Production Server")
                ));
    }
}