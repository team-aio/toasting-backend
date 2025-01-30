package io.toasting.global.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

private const val JWT = "JWT"
private const val BEARER = "bearer"

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI =
        OpenAPI()
            .components(Components())
            .info(apiInfo())
            .addSecurityItem(SecurityRequirement().addList(JWT))
            .components(jwtComponents())

    private fun apiInfo(): Info =
        Info()
            .title("Toasting API")
            .description("Toasting API Swagger")
            .version("0.0.1")

    private fun jwtComponents(): Components =
        Components().addSecuritySchemes(
            JWT,
            SecurityScheme()
                .name(JWT)
                .type(SecurityScheme.Type.HTTP)
                .scheme(
                    BEARER,
                ).bearerFormat(JWT),
        )
}
