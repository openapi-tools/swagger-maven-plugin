package dk.nykredit.swagger.example;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import dk.nykredit.swagger.example.model.AccountRepresentation;
import dk.nykredit.swagger.example.model.AccountUpdateRepresentation;
import dk.nykredit.swagger.example.model.AccountsRepresentation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * Exposing account as REST service.
 */
@Path("/accounts")
@Api(value = "/accounts", authorizations = @Authorization("oauth2"))
public class AccountServiceExposure {

    @GET
    @Produces({"application/hal+json"})
    @ApiOperation(value = "List all accounts", nickname = "listAccounts", response = AccountsRepresentation.class)
    public Response list(@Context UriInfo uriInfo, @Context Request request) {
        return Response.ok().build();
    }

    @GET
    @Path("{regNo}-{accountNo}")
    @Produces({"application/hal+json"})
    @ApiOperation(value = "Get single account", nickname = "getAccount", response = AccountRepresentation.class)
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "No account found.")
    })
    public Response get(@PathParam("regNo") @Pattern(regexp = "^[0-9]{4}$") String regNo,
            @PathParam("accountNo") @Pattern(regexp = "^[0-9]+$") String accountNo,
            @Context UriInfo uriInfo, @Context Request request) {
        return Response.ok().build();
    }

    @GET
    @Path("{regNo}-{accountNo}")
    @Produces({"application/hal+json;v=1"})
    @ApiOperation(value = "Version 1", hidden = true)
    public Response getV1() {
        return null;
    }

    @PUT
    @Path("{regNo}-{accountNo}")
    @Produces({"application/hal+json"})
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create new or update existing account", nickname = "updateAccount", response = AccountRepresentation.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "No updating possible")
    })
    public Response createOrUpdate(@PathParam("regNo") @Pattern(regexp = "^[0-9]{4}$") String regNo,
            @PathParam("accountNo") @Pattern(regexp = "^[0-9]+$") String accountNo,
            @Valid AccountUpdateRepresentation account,
            @Context UriInfo uriInfo, @Context Request request) {
        return Response.ok().build();
    }

}
