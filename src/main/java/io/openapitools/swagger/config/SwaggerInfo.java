package io.openapitools.swagger.config;

import io.swagger.v3.oas.models.info.Info;

import java.util.Map;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * Configuring the Swagger info properties.
 */
public class SwaggerInfo {

    @Parameter
    private String title;

    @Parameter
    private String version;

    @Parameter
    private String description;

    @Parameter
    private String termsOfService;

    @Parameter
    private SwaggerContact contact;

    @Parameter
    private SwaggerLicense license;

    @Parameter
    private Map<String, Object> extensions;

    public Info createInfoModel() {
        Info info = new Info();

        if (title != null) {
            info.setTitle(title);
        }

        if (version != null) {
            info.setVersion(version);
        }

        if (description != null) {
            info.setDescription(description);
        }

        if (termsOfService != null) {
            info.setTermsOfService(termsOfService);
        }

        if (contact != null) {
            info.setContact(contact.createContactModel());
        }

        if (license != null) {
            info.setLicense(license.createLicenseModel());
        }

        if (extensions != null && !extensions.isEmpty()) {
            info.setExtensions(extensions);
        }

        return info;
    }
}
