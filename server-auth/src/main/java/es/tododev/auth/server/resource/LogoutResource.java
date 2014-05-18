package es.tododev.auth.server.resource;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.service.LoginService;

@Path(Constants.LOGOUT_PATH)
public class LogoutResource {

	private final String LOGGED_PAGE = "/logout.jsp";
	private final LoginService loginService;
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	
	@Inject
	public LogoutResource(LoginService loginService, @Context HttpServletRequest request, @Context HttpServletResponse response){
		this.loginService = loginService;
		this.request = request;
		this.response = response;
	}
	
	@GET
	public void logout() throws ServletException, IOException {
		loginService.logout();
		request.getRequestDispatcher(LOGGED_PAGE).forward(request, response);
	}
	
}
