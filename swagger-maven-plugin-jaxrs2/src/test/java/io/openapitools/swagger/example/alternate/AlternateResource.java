package io.openapitools.swagger.example.alternate;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("alternate")
public class AlternateResource {

    @GET
    public Response list() {
        return null;
    }

}
