package es.tododev.auth.server.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Template;

import es.tododev.auth.commons.Constants;

@Path(Constants.LOGOUT_PATH)
@Template
public class LogoutResource {

	@GET
	public Response logout(){
		return null;
	}
	
}
