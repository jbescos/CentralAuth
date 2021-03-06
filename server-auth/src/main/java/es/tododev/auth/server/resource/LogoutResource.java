package es.tododev.auth.server.resource;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.service.LoginService;

@Path(Constants.LOGOUT_CALLBACK_PATH)
public class LogoutResource {

	private final LoginService loginService;
	private final EntityManager em;
	
	@Inject
	public LogoutResource(EntityManager em, LoginService loginService){
		this.loginService = loginService;
		this.em = em;
	}
	
	@GET
	public Response logout() throws ServletException, IOException {
		try {
			List<String> urls = loginService.logout(em);
			return Response.ok(urls).build();
		} catch (LoginException e) {
			return Response.status(403).build();
		}
	}
	
}
