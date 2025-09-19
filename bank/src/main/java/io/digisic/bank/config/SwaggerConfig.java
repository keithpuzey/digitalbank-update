package io.digisic.bank.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.digisic.bank.util.Constants;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static io.digisic.bank.util.Constants.API_VERSION;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    // JWT token reference used in Swagger
    private static final String JWT_TOKEN_REF = "Bearer %token";

    // Updated API path for version 2.1
    private static final String API_REGEX_PATH = Constants.API_BASE + ".*";

    @Bean
    public Docket api() {
        // Define security scheme (JWT header)
        List<SecurityScheme> schemeList = new ArrayList<>();
        schemeList.add(new ApiKey(JWT_TOKEN_REF, Constants.API_AUTH_HEADER, "Header"));

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.digisic.bank.controller"))
                .paths(PathSelectors.regex(API_REGEX_PATH))
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(schemeList)
                .securityContexts(Arrays.asList(securityContext()));
    }

    // API Info shown in Swagger UI
    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Digital Bank API",
                "Digital Bank API provides Administration and User functionality through API Endpoints.",
                API_VERSION,
                "Terms of Service",
                new Contact("Digital Bank", "www.demo.io", "digitalbank@demo.io"),
                "License of API",
                "API License URL",
                Collections.emptyList()
        );
    }

    // Security context for JWT authentication
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(
                        Arrays.asList(new SecurityReference(JWT_TOKEN_REF, new AuthorizationScope[0]))
                )
                .forPaths(PathSelectors.regex(API_REGEX_PATH))
                .build();
    }
}