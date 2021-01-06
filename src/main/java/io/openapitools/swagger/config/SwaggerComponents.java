package io.openapitools.swagger.config;

import java.util.Map;

import org.apache.maven.plugins.annotations.Parameter;

import io.swagger.v3.oas.models.Components;

public class SwaggerComponents {

    /**
     * Security schemes (under Comtonents)
     */
    @Parameter
    private Map<String,SwaggerSecurityScheme> securitySchemes;

    /**
     * Schema (under Components)
     */
    @Parameter
    private Map<String, SwaggerSchema> schemas;

    // TODO: implement schemas, responses, ... from
    // https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#componentsObject

    public Components createComponentsModel() {
        final Components components = new Components();

        if (securitySchemes != null && !securitySchemes.isEmpty()) {
            securitySchemes.forEach((key, value) -> components.addSecuritySchemes(key, value.createSecuritySchemaModel()));
        }

        if (schemas != null && !schemas.isEmpty()) {
            schemas.forEach((key, value) -> components.addSchemas(key, value.createSchemaModel()));
        }

        return components;
    }
}
