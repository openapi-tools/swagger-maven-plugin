package io.openapitools.swagger.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Configuring Swagger in compliance with the way the com.github.kongchen Swagger plugin does it.
 */
public class SwaggerConfig {

    /**
     * List of servers for the endpoint.
     */
    @Parameter
    private List<SwaggerServer> servers = Collections.emptyList();

    /**
     * Providing the OpenAPI information description. This might be overridden by ReaderListener or SwaggerDefinition annotation.
     */
    @Parameter
    private SwaggerInfo info;

    /**
     * Convenience for reading the informational description from file instead of embedding it.
     */
    @Parameter
    private File descriptionFile;

    public OpenAPI createSwaggerModel() {
        OpenAPI spec = new OpenAPI();

        servers.forEach(s -> spec.addServersItem(s.createServerModel()));

        if (info != null) {
            spec.setInfo(info.createInfoModel());
        }

        if (descriptionFile != null) {
            if (spec.getInfo() == null) {
                spec.setInfo(new Info());
            }
            try {
                spec.getInfo().setDescription(Files.readAllLines(descriptionFile.toPath()).stream().collect(Collectors.joining("\n")));
            } catch (IOException e) {
                throw new RuntimeException("Unable to read descriptor file " + descriptionFile, e);
            }
        }

        return spec;
    }
}
