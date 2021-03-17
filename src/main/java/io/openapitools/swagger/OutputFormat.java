package io.openapitools.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.swagger.models.Swagger;
import io.swagger.util.Json;
import io.swagger.util.Yaml;

import java.io.File;
import java.io.IOException;

/**
 * Supported output formats.
 */
public enum OutputFormat {

    JSON(new JSONWriter()),
    YAML(new YAMLWriter());

    private final SwaggerWriter writer;

    OutputFormat(SwaggerWriter writer) {
        this.writer = writer;
    }

    public void write(Swagger swagger, File file, boolean prettyPrint) throws IOException {
        writer.write(swagger, file, prettyPrint);
    }

    /**
     * Interface defining requirements for being able to write out Swagger instance to file.
     */
    @FunctionalInterface
    interface SwaggerWriter {
        void write(Swagger swagger, File file, boolean prettyPrint) throws IOException;
    }

    /**
     * As the Maven plugin plugin does not support lambdas properly a real implementation is needed.
     */
    static class JSONWriter implements SwaggerWriter {

        @Override
        public void write(Swagger swagger, File file, boolean prettyPrint) throws IOException {
            ObjectMapper mapper = Json.mapper();
            if (prettyPrint) {
                mapper.enable(SerializationFeature.INDENT_OUTPUT);
            }
            mapper.writeValue(file, swagger);
        }
    }

    /**
     * As the Maven plugin plugin does not support lambdas properly a real implementation is needed.
     */
    static class YAMLWriter implements SwaggerWriter {

        @Override
        public void write(Swagger swagger, File file, boolean prettyPrint) throws IOException {
            Yaml.mapper().writeValue(file, swagger);
        }
    }
}
