package com.imepac.appointment.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Agendamentos",
                version = "v1",
                description = "API para agendamento de consultas, retornos e prontuário",
                contact = @Contact(name = "IMEPAC", email = "contato@imepac.edu.br")
        )
)
public class SwaggerConfig {
}

