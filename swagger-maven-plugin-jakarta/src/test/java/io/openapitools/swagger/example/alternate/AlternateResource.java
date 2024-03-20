package io.openapitools.swagger.example.alternate;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("alternate")
public class AlternateResource {

    @GET
    public Response list() {
        return null;
    }

}
