package com.imepac.attendance.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name:attendance-service}")
    private String appName;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(appName)
                        .version("1.0.0")
                        .description("Módulo Atendimento"))
                .servers(List.of(new Server().url("/")));
    }
}

