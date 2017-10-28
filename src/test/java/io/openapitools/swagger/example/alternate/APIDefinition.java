package io.openapitools.swagger.example.alternate;

import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@SwaggerDefinition(host = "service.example.it",
    schemes = SwaggerDefinition.Scheme.HTTPS,
    info = @Info(title = "My Title", version = "1.0.0"))
public interface APIDefinition {
}
