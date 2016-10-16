package es.tododev.auth.server.resource;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.service.LoginService;

@Path(Constants.LOGIN_RESOURCE)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginResource {

	private final static Logger log = LogManager.getLogger();
	private final LoginService loginService;
	
	@Inject
	public LoginResource(LoginService loginService){
		this.loginService = loginService;
	}
	
	@GET
	@Path("/login")
	public Response login(@QueryParam("appId") String appId, @QueryParam("username") String username, @QueryParam("password") String password) throws Exception{
		log.debug("User "+username+" doing login");
		try {
			List<String> urls = loginService.successLogin(username, password, appId);
			return Response.ok(urls).build();
		} catch (LoginException e) {
			return Response.status(403).build();
		}
		
	}
	
	@GET
	@Path("/register")
	public Response register(@QueryParam("appId") String appId, @QueryParam("username") String username, @QueryParam("password1") String password1, @QueryParam("password2") String password2) throws ServletException, IOException {
		if(password1.equals(password2)){
			try {
				List<String> urls = loginService.register(username, password1, appId);
				return Response.ok(urls).build();
			} catch (LoginException e) {
				log.error("User "+username, e);
				return Response.status(403).build();
			}
		}
		return Response.status(403).build();
	}
	
}
