package es.tododev.auth.server.resource;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.tododev.auth.commons.Constants;

@Path(Constants.COOKIE_RESOURCE)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CookieResource {

	private final static Logger log = LogManager.getLogger();
	
	private final HttpServletRequest request;
	
	@Inject
	public CookieResource(HttpServletRequest request){
		this.request = request;
	}
	
	@GET
	@Path("/login")
	public Response login(){
		String username = (String) request.getAttribute(Constants.USER_NAME_KEY);
		log.info("{} doing login", username);
		return Response.ok().build();
	}
	
	@GET
	@Path("/logout")
	public Response logout(){
		String username = (String) request.getAttribute(Constants.USER_NAME_KEY);
		log.info("{} doing logout", username);
		return Response.ok().build();
	}
	
}
