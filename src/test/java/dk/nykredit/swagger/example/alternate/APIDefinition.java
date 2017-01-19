package dk.nykredit.swagger.example.alternate;

import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@SwaggerDefinition(host = "service.nykredit.it",
    schemes = SwaggerDefinition.Scheme.HTTPS,
    info = @Info(title = "My Title", version = "1.0.0"))
public interface APIDefinition {
}
