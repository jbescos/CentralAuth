package es.tododev.auth.server.resource;

import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.aop.Transactional;
import es.tododev.auth.server.dto.ApplicationDto;
import es.tododev.auth.server.service.ApplicationService;

@Path(Constants.APPLICATION_RESOURCE)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApplicationResource {
	
	private final ApplicationService applicationService;
	private final EntityManager em;
	
	@Inject
	public ApplicationResource(EntityManager em, ApplicationService applicationService){
		this.em = em;
		this.applicationService = applicationService;
	}

	@POST
	public Response addApplication(@CookieParam(Constants.APP_COOKIE) String appToken, @QueryParam("appId") String appId, @QueryParam("password") String password, @QueryParam("url") String url, @QueryParam("expireMillis") Long expireMillis, @QueryParam("description") String description) throws URISyntaxException{
		applicationService.addApplication(em, appToken, appId, password, url, expireMillis, description);
		return Response.ok(appId).build();
	}
	
	@GET
	public Response getApplications(@CookieParam(Constants.APP_COOKIE) String appToken){
		return Response.ok(applicationService.getApplications(em, appToken)).build();
	}
	
	@DELETE
	public Response delete(@CookieParam(Constants.APP_COOKIE) String appToken, @QueryParam("appId") String appId){
		applicationService.deleteApplication(em, appToken, appId);
		return Response.ok(appId).build();
	}
	
	@PUT
	public Response update(@CookieParam(Constants.APP_COOKIE) String appToken, ApplicationDto dto){
		applicationService.update(em, appToken, dto);
		return Response.ok(dto.getAppId()).build();
	}
	
}
