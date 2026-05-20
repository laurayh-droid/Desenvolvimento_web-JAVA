package com.imepac.administrative.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Administração",
                version = "v1",
                description = "CRUD de funcionários, usuários, especialidades, médicos e convênios",
                contact = @Contact(name = "IMEPAC", email = "contato@imepac.edu.br")
        )
)
public class SwaggerConfig {
}
