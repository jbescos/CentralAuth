package es.tododev.auth.server.resource;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.service.LoginService;

@Path(Constants.LOGOUT_PATH)
public class LogoutResource {

	private final LoginService loginService;
	
	@Inject
	public LogoutResource(LoginService loginService){
		this.loginService = loginService;
	}
	
	@GET
	public Response logout() throws ServletException, IOException {
		try {
			List<String> urls = loginService.logout();
			return Response.ok(urls).build();
		} catch (LoginException e) {
			return Response.status(403).build();
		}
	}
	
}
