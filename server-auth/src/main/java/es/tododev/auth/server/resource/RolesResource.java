package es.tododev.auth.server.resource;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.dto.RolesDTO;
import es.tododev.auth.server.service.RolesService;

@Path(Constants.ROLES_RESOURCE)
public class RolesResource {

	private final RolesService rolesService;
	private final EntityManager em;
	
	@Inject
	public RolesResource(RolesService rolesService, EntityManager em){
		this.rolesService = rolesService;
		this.em = em;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getRoles(@QueryParam("token") String sharedDomainToken, @QueryParam("appId") String appId){
		RolesDTO dto = rolesService.getRoles(em, sharedDomainToken, appId);
		return Response.ok(dto).build();
	}
	
	@POST
	public Response addRole(@FormParam(Constants.USER_NAME_KEY) String username, @FormParam("appId") String appId, @FormParam("role") String role) throws URISyntaxException{
		rolesService.addRole(em, username, appId, role);
		return Response.seeOther(new URI("/server-auth/layoutit/src/roles.html")).build();
	}
	
}
