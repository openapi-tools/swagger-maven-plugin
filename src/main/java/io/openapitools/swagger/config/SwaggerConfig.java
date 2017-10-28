package io.openapitools.swagger.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Collectors;

import io.swagger.models.Info;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Configuring Swagger in compliance with the way the com.github.kongchen Swagger plugin does it.
 */
public class SwaggerConfig {

    /**
     * Comma separated list of supported URI schemes.
     */
    @Parameter
    private String schemes;

    /**
     * Hostname used to access API.
     */
    @Parameter
    private String host;

    /**
     * Base path to prepend to all API operations.
     */
    @Parameter
    private String basePath;

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

    public Swagger createSwaggerModel() {
        Swagger swagger = new Swagger();

        if (schemes != null) {
            Arrays.stream(schemes.split(",")).forEach(scheme -> {
                swagger.scheme(Scheme.forValue(scheme));
            });
        }

        if (host != null) {
            swagger.setHost(host);
        }

        if (basePath != null) {
            swagger.setBasePath(basePath);
        }

        if (info != null) {
            swagger.setInfo(info.createInfoModel());
        }

        if (descriptionFile != null) {
            if (swagger.getInfo() == null) {
                swagger.setInfo(new Info());
            }
            try {
                swagger.getInfo().setDescription(Files.readAllLines(descriptionFile.toPath()).stream().collect(Collectors.joining("\n")));
            } catch (IOException e) {
                throw new RuntimeException("Unable to read descriptor file " + descriptionFile, e);
            }
        }

        return swagger;
    }
}
