package com.smoke.meteoservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "MeteoService API", version = "v1", description = "Weather API"))
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi weatherApi() {
        return GroupedOpenApi.builder()
                .group("weather-api")
                .packagesToScan("com.smoke.meteoservice.adapter.in.controller") 
                .build();
    }
}
