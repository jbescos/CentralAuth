package es.tododev.auth.server.resource;

import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.service.LoginService;

@Path(Constants.LOGIN_PATH)
@Produces(MediaType.TEXT_HTML) 
public class LoginResource {

	private final LoginService loginService;
	private final String LOGIN_PAGE = "login.html";
	private final String REGISTER_PAGE = "register.html";
	private final String LOGGED_PAGE = "logged.jsp";
	
	@Inject
	public LoginResource(LoginService loginService){
		this.loginService = loginService;
	}
	
	@POST
	public Response login(@FormParam("username") String username, @FormParam("password") String password) throws URISyntaxException{
		String pageResponse = null;
		if(loginService.successLogin(username, password)){
			pageResponse = LOGGED_PAGE;
		}else{
			pageResponse = LOGIN_PAGE;
		}
		return Response.ok().entity(new Viewable(pageResponse, null)).build();
	}
	
	@POST
	@Path("/register")
	public Response register(@FormParam("username") String username, @FormParam("password1") String password1, @FormParam("password2") String password2) throws URISyntaxException{
		String pageResponse = null;
		if(password1.equals(password2) && loginService.register(username, password1)){
			pageResponse = LOGGED_PAGE;
		}else{
			pageResponse = REGISTER_PAGE;
		}
		return Response.ok().entity(new Viewable(pageResponse, null)).build();
	}
	
}
