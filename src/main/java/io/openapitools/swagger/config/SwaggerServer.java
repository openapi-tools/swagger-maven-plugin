package io.openapitools.swagger.config;

import io.swagger.v3.oas.models.servers.Server;
import org.apache.maven.plugins.annotations.Parameter;

public class SwaggerServer {

    @Parameter
    private String url;

    @Parameter
    private String description;

    public Server createServerModel() {
        Server server = new Server();
        server.setUrl(url);
        server.setDescription(description);
        return server;
    }

}
