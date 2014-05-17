package es.tododev.auth.server.resource;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.service.LoginService;

@Path(Constants.LOGIN_PATH)
public class LoginResource {

	private final LoginService loginService;
	
	@Inject
	public LoginResource(LoginService loginService){
		this.loginService = loginService;
	}
	
	@POST
	public Response login(@FormParam("username") String username, @FormParam("password") String password) throws URISyntaxException{
		String pageResponse = null;
		if(loginService.successLogin(username, password)){
			pageResponse = "../logged.html";
		}else{
			pageResponse = "../login.html";
		}
		return Response.seeOther(new URI(pageResponse)).build();
	}
	
	@POST
	@Path("/register")
	public Response register(@FormParam("username") String username, @FormParam("password1") String password1, @FormParam("password2") String password2) throws URISyntaxException{
		String pageResponse = null;
		if(password1.equals(password2) && loginService.register(username, password1)){
			pageResponse = "../logged.html";
		}else{
			pageResponse = "../register.html";
		}
		return Response.seeOther(new URI(pageResponse)).build();
	}
	
}
