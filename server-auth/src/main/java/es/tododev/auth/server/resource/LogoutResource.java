package es.tododev.auth.server.resource;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.service.LogoutService;

@Path(Constants.LOGOUT_PATH)
public class LogoutResource {

	private final LogoutService logoutService;
	
	@Inject
	public LogoutResource(LogoutService logoutService){
		this.logoutService = logoutService;
	}
	
	@GET
	public Response logout() throws URISyntaxException{
		logoutService.logout();
		return Response.seeOther(new URI("../logout.html")).build();
	}
	
}
