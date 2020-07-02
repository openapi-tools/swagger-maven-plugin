package io.openapitools.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OpenApiSorterTest {

    @Test
    public void testSort() {
        ObjectSchema schema = new ObjectSchema();
        schema.addProperties("s2", new StringSchema());
        schema.addProperties("s1", new StringSchema());

        Components components = new Components()
                .addSchemas("s1", schema)
                .addResponses("k2", new ApiResponse())
                .addResponses("k1", new ApiResponse())
                .addParameters("k2", new Parameter())
                .addParameters("k1", new Parameter())
                .addExamples("k2", new Example())
                .addExamples("k1", new Example())
                .addRequestBodies("k2", new RequestBody())
                .addRequestBodies("k1", new RequestBody())
                .addHeaders("k2", new Header())
                .addHeaders("k1", new Header())
                .addSecuritySchemes("k2", new SecurityScheme())
                .addSecuritySchemes("k1", new SecurityScheme())
                .addLinks("k2", new Link())
                .addLinks("k1", new Link())
                .addCallbacks("k2", new Callback())
                .addCallbacks("k1", new Callback());

        Paths paths = new Paths()
                .addPathItem("p2", new PathItem())
                .addPathItem("p1", new PathItem());

        OpenAPI api = new OpenAPI()
                .components(components)
                .paths(paths);

        api = OpenAPISorter.sort(api);

        assertEquals("s1", api.getComponents().getSchemas()
                .values().stream()
                .findAny().get().getProperties()
                .keySet().stream().findFirst().get());
        assertEquals("k1", api.getComponents().getResponses().keySet().stream().findFirst().get());
        assertEquals("k1", api.getComponents().getParameters().keySet().stream().findFirst().get());
        assertEquals("k1", api.getComponents().getExamples().keySet().stream().findFirst().get());
        assertEquals("k1", api.getComponents().getRequestBodies().keySet().stream().findFirst().get());
        assertEquals("k1", api.getComponents().getHeaders().keySet().stream().findFirst().get());
        assertEquals("k1", api.getComponents().getSecuritySchemes().keySet().stream().findFirst().get());
        assertEquals("k1", api.getComponents().getLinks().keySet().stream().findFirst().get());
        assertEquals("k1", api.getComponents().getCallbacks().keySet().stream().findFirst().get());
        assertEquals("p1", api.getPaths().keySet().stream().findFirst().get());
    }
}
