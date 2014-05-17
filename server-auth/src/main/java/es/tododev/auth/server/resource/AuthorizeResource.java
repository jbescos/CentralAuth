package es.tododev.auth.server.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.commons.dto.ReqAuthorizationDTO;
import es.tododev.auth.server.service.AuthorizeService;

@Path(Constants.AUTHORIZE_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorizeResource {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final AuthorizeService service;
	
	@Inject
	public AuthorizeResource(AuthorizeService service){
		this.service = service;
		log.debug("Creating instance of {}", this);
	}

	@POST
	public Response authorize(ReqAuthorizationDTO dto){
		return Response.ok(service.authorize(dto)).build();
	}
	
}
