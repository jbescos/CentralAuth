package es.tododev.auth.server.resource;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
	public Response addApplication(@FormParam("appId") String appId, @FormParam("password") String password) throws URISyntaxException{
		applicationService.addApplication(appId, password);
		return Response.seeOther(new URI("../application.html")).build();
	}
	
}
