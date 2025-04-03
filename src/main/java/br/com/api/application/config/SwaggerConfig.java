package br.com.api.application.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("financial-control-api")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info().title("Financial Control API").version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList("x-api-key"))
                .components(
                        new Components()
                                .addSecuritySchemes("x-api-key", new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("x-api-key")
                                )
                );
    }
}
