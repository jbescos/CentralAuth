package es.tododev.auth.server.resource;

import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.service.LoginService;

@Path(Constants.LOGOUT_PATH)
public class LogoutResource {

	private final String LOGGED_PAGE = "logout.jsp";
	private final LoginService loginService;
	
	@Inject
	public LogoutResource(LoginService loginService){
		this.loginService = loginService;
	}
	
	@GET
	public Response logout() throws URISyntaxException{
		loginService.logout();
		return Response.ok().entity(new Viewable(LOGGED_PAGE, null)).build();
	}
	
}
