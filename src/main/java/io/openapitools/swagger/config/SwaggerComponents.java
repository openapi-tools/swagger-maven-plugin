package io.openapitools.swagger.config;

import java.util.Collections;
import java.util.List;

import org.apache.maven.plugins.annotations.Parameter;

import io.swagger.v3.oas.models.Components;

public class SwaggerComponents {

    /**
     * Security schemes (under Comtonents)
     */
    @Parameter
    private List<SwaggerSecurityScheme> securitySchemes = Collections.emptyList();

    // TODO: implement schemas, responses, ... from
    // https://github.com/OAI/OpenAPI-Specification/blob/3.0.1/versions/3.0.1.md#componentsObject

    public Components createComponentsModel() {
        Components components = new Components();

        if (securitySchemes != null && !securitySchemes.isEmpty()) {
            securitySchemes.forEach(s -> components.addSecuritySchemes(s.getName(), s.createSecuritySchemaModel()));
        }

        return components;
    }
}
