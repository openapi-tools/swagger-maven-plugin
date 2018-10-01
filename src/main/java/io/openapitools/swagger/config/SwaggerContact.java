package io.openapitools.swagger.config;

import io.swagger.v3.oas.models.info.Contact;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Configuring Swagger contact properties.
 */
public class SwaggerContact {

    @Parameter
    private String name;

    @Parameter
    private String url;

    @Parameter
    private String email;

    public Contact createContactModel() {
        Contact contact = new Contact();

        if (name != null) {
            contact.setName(name);
        }

        if (url != null) {
            contact.setUrl(url);
        }

        if (email != null) {
            contact.setEmail(email);
        }

        return contact;
    }
}
