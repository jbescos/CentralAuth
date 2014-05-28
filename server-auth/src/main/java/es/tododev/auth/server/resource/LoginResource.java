package es.tododev.auth.server.resource;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.service.LoginService;

@Path(Constants.LOGIN_PATH)
//@Produces(MediaType.TEXT_HTML) 
public class LoginResource {

	private final LoginService loginService;
	private final String LOGIN_PAGE = "/login.html";
	private final String REGISTER_PAGE = "/layoutit/src/register.html";
	public static final String LOGGED_PAGE = "/layoutit/src/logged.jsp";
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	
	@Inject
	public LoginResource(LoginService loginService, @Context HttpServletRequest request, @Context HttpServletResponse response){
		this.loginService = loginService;
		this.request = request;
		this.response = response;
	}
	
	@POST
	public void login(@FormParam("username") String username, @FormParam("password") String password) throws ServletException, IOException{
		String pageResponse = null;
		if(loginService.successLogin(username, password)){
			pageResponse = LOGGED_PAGE;
		}else{
			pageResponse = LOGIN_PAGE;
		}
		request.getRequestDispatcher(pageResponse).forward(request, response);
	}
	
	@POST
	@Path("/register")
	public void register(@FormParam("username") String username, @FormParam("password1") String password1, @FormParam("password2") String password2) throws ServletException, IOException {
		String pageResponse = null;
		if(password1.equals(password2) && loginService.register(username, password1)){
			pageResponse = LOGGED_PAGE;
		}else{
			pageResponse = REGISTER_PAGE;
		}
		request.getRequestDispatcher(pageResponse).forward(request, response);
	}
	
}
