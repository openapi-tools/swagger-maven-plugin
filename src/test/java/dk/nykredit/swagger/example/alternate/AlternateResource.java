package dk.nykredit.swagger.example.alternate;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@Path("alternate")
public class AlternateResource {

    @GET
    @ApiOperation("alternate operation")
    public Response list() {
        return null;
    }

}
