package es.tododev.auth.server.resource;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.service.ApplicationService;

@Path(Constants.APPLICATION_PATH)
public class ApplicationResource {
	
	private final ApplicationService applicationService;
	
	@Inject
	public ApplicationResource(ApplicationService applicationService){
		this.applicationService = applicationService;
	}

	@POST
	public Response addApplication(@PathParam("groupId") String groupId, @QueryParam("appId") String appId, @QueryParam("password") String password, @QueryParam("url") String url, @QueryParam("expireMillis") Long expireMillis, @QueryParam("description") String description) throws URISyntaxException{
		applicationService.addApplication(groupId, appId, password, url, expireMillis, description);
		return Response.seeOther(new URI("/server-auth/layoutit/src/application.html")).build();
	}
	
}
