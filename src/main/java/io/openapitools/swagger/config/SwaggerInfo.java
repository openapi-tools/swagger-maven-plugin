package io.openapitools.swagger.config;

import io.swagger.models.Info;
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

        return info;
    }
}
