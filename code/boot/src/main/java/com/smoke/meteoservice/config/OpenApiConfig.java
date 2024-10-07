package com.smoke.meteoservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "MeteoService API", version = "v1", description = "Temperature API"))
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi temperatureApi() {
        return GroupedOpenApi.builder()
                .group("temperature-api")
                .packagesToScan("com.smoke.meteoservice.adapter.in.controller") 
                .build();
    }
}
