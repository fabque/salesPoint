package com.ar.sales.point.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${openapi30-modulename:sales.point}") String moduleName,
                                 @Value("${openapi30-apiversion:1.0}") String apiVersion) {
        return new OpenAPI()
                .info(new Info().title(moduleName).version(apiVersion));
    }
}

