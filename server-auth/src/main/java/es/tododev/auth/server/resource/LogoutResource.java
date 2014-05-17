package es.tododev.auth.server.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.tododev.auth.commons.Constants;

@Path(Constants.LOGOUT_PATH)
@Produces(MediaType.TEXT_HTML)
@Consumes(MediaType.TEXT_HTML)
public class LogoutResource {

	@GET
	public Response logout(){
		return null;
	}
	
}
