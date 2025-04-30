package org.example.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info =
@Info(
        title = "LibraryAccountingService",
        version = "1.0.0",
        description = "Library accounting service API"
))
public class SwaggerConfig {
}
