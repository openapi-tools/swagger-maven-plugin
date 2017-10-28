package io.openapitools.swagger.config;

import io.swagger.models.License;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Configuring Swagger license.
 */
public class SwaggerLicense {

    @Parameter
    private String name;

    @Parameter
    private String url;

    public License createLicenseModel() {
        License license = new License();

        if (name != null) {
            license.setName(name);
        }

        if (url != null) {
            license.setUrl(url);
        }

        return license;
    }
}
