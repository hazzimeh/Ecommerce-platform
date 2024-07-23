package com.example.apigateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gateway Documentation")
                        .version("1.0")
                        .description("API Gateway for Microservices")
                        .termsOfService("http://swagger.io/terms/")
                        .contact(new Contact().name("API Support").url("http://www.example.com/support").email("support@example.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8080").description("Local server"),
                        new Server().url("https://api.example.com").description("Production server")
                ))
                .tags(Arrays.asList(
                        new Tag().name("User Service").description("Operations related to users"),
                        new Tag().name("Product Service").description("Operations related to products"),
                        new Tag().name("Order Service").description("Operations related to orders"),
                        new Tag().name("Payment Service").description("Operations related to payments")
                ));
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("users")
                .pathsToMatch("/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi productApi() {
        return GroupedOpenApi.builder()
                .group("products")
                .pathsToMatch("/products/**")
                .build();
    }

    @Bean
    public GroupedOpenApi orderApi() {
        return GroupedOpenApi.builder()
                .group("orders")
                .pathsToMatch("/orders/**")
                .build();
    }

    @Bean
    public GroupedOpenApi paymentApi() {
        return GroupedOpenApi.builder()
                .group("payments")
                .pathsToMatch("/payments/**")
                .build();
    }
}
