package com.example.shiftplanner.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Shiftplanner API",
                version = "1.0",
                description = "Backend API for internal shift planning application"
        )
)
public class OpenApiConfig {
}