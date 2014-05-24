package es.tododev.auth.server.resource;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.service.RolesService;

@Path(Constants.ROLES_PATH)
public class RolesResource {

	private final RolesService rolesService;
	
	@Inject
	public RolesResource(RolesService rolesService){
		this.rolesService = rolesService;
	}
	
	@POST
	public Response addRole(@FormParam("username") String username, @FormParam("appId") String appId, @FormParam("role") String role) throws URISyntaxException{
		rolesService.addRole(username, appId, role);
		return Response.seeOther(new URI("/server-auth/layoutit/src/roles.html")).build();
	}
	
}
