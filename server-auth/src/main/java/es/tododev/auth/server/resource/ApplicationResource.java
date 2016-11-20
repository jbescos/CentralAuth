package es.tododev.auth.server.resource;

import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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
import es.tododev.auth.server.dto.ApplicationDto;
import es.tododev.auth.server.service.ApplicationService;

@Path(Constants.APPLICATION_RESOURCE)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApplicationResource {
	
	private final ApplicationService applicationService;
	private final EntityManager em;
	private final HttpServletRequest request;
	
	@Inject
	public ApplicationResource(EntityManager em, ApplicationService applicationService, HttpServletRequest request){
		this.em = em;
		this.applicationService = applicationService;
		this.request = request;
	}

	@POST
	public Response addApplication(@QueryParam("appId") String appId, @QueryParam("password") String password, @QueryParam("url") String url, @QueryParam("expireMillis") Long expireMillis, @QueryParam("description") String description) throws URISyntaxException{
		applicationService.addApplication(em, (String)request.getAttribute(Constants.APP_COOKIE), appId, password, url, expireMillis, description);
		return Response.ok(appId).build();
	}
	
	@GET
	public Response getApplications(){
		return Response.ok(applicationService.getApplications(em, (String)request.getAttribute(Constants.APP_COOKIE))).build();
	}
	
	@DELETE
	public Response delete(@QueryParam("appId") String appId){
		applicationService.deleteApplication(em, (String)request.getAttribute(Constants.APP_COOKIE), appId);
		return Response.ok(appId).build();
	}
	
	@PUT
	public Response update(ApplicationDto dto){
		applicationService.update(em, (String)request.getAttribute(Constants.APP_COOKIE), dto);
		return Response.ok(dto.getAppId()).build();
	}
	
}
