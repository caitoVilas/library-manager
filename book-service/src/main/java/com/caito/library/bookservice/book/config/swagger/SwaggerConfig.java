package com.caito.library.bookservice.book.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
@OpenAPIDefinition(

        info = @Info(
                title = "library - Book Services",
                version = "${application.version}",
                description = "Book crud  for Library Management application"
        )
)
@SecurityScheme(
        name = "security token",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "Bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
