package es.tododev.auth.server.resource;

import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.service.ApplicationService;

@Path(Constants.APPLICATION_RESOURCE)
public class ApplicationResource {
	
	private final ApplicationService applicationService;
	
	@Inject
	public ApplicationResource(ApplicationService applicationService){
		this.applicationService = applicationService;
	}

	@POST
	public Response addApplication(@CookieParam(Constants.APP_COOKIE) String appToken, @QueryParam("appId") String appId, @QueryParam("password") String password, @QueryParam("url") String url, @QueryParam("expireMillis") Long expireMillis, @QueryParam("description") String description) throws URISyntaxException{
		applicationService.addApplication(appToken, appId, password, url, expireMillis, description);
		return Response.ok(appId).build();
	}
	
	@GET
	public Response getApplications(@CookieParam(Constants.APP_COOKIE) String appToken){
		return Response.ok(applicationService.getApplications(appToken)).build();
	}
	
	@DELETE
	public Response delete(@CookieParam(Constants.APP_COOKIE) String appToken, @QueryParam("appId") String appId){
		applicationService.deleteApplication(appToken, appId);
		return Response.ok(appId).build();
	}
	
}
