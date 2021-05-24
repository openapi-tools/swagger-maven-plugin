package io.openapitools.swagger.example.manipulate;

import io.swagger.v3.jaxrs2.ReaderListener;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.integration.api.OpenApiReader;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;

@OpenAPIDefinition
public class PathManipulator implements ReaderListener {
    @Override
    public void beforeScan(OpenApiReader openApiReader, OpenAPI openAPI) {

    }

    @Override
    public void afterScan(OpenApiReader openApiReader, OpenAPI openAPI) {
        if ("Path Manipulation".equals(openAPI.getInfo().getTitle())) {
            Paths paths = new Paths();
            openAPI.getPaths().forEach((path, item) -> {
                paths.put(path.replace("accounts", "").replace("//", "/"), item);
            });
            openAPI.setPaths(paths);
        }
    }
}
