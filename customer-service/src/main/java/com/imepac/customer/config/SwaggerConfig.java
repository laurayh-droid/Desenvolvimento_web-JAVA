package com.imepac.customer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Customer Service API")
                        .description("REST API for customer management")
                        .version("v1")
                        .contact(new Contact()
                                .name("IMEPAC")
                                .email("contato@imepac.edu.br")));
    }
}
