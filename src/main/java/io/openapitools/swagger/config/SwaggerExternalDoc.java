package io.openapitools.swagger.config;

import java.util.Map;

import org.apache.maven.plugins.annotations.Parameter;

import io.swagger.v3.oas.models.ExternalDocumentation;

public class SwaggerExternalDoc {

    /**
     * A short description of the target documentation. CommonMark syntax MAY be
     * used for rich text representation.
     */
    @Parameter
    private String description;

    /**
     * REQUIRED. The URL for the target documentation. Value MUST be in the format
     * of a URL.
     */
    @Parameter(required = true)
    private String url;

    @Parameter
    private Map<String, Object> extensions;

    public ExternalDocumentation createExternalDocModel() {
        ExternalDocumentation externalDoc = new ExternalDocumentation();

        if (description != null) {
            externalDoc.setDescription(description);
        }

        if (url != null) {
            externalDoc.setUrl(url);
        }

        if (extensions != null && !extensions.isEmpty()) {
            externalDoc.setExtensions(extensions);
        }

        return externalDoc;
    }

}
