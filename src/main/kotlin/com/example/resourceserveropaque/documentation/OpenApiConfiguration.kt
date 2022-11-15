package com.example.resourceserveropaque.documentation

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfiguration {

    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI()
            .addSecurityItem(
                SecurityRequirement().addList("bearer-key")
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        "bearer-key",
                        SecurityScheme().name("bearer-key").type(SecurityScheme.Type.HTTP).scheme("bearer")
                    )
            )
    }
}
