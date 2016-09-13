package es.tododev.auth.server.resource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.commons.dto.ReqAuthorizationDTO;
import es.tododev.auth.server.service.AuthorizeService;

@Path(Constants.AUTHORIZE_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorizeResource {
	
	private final static Logger log = LogManager.getLogger();
	private final AuthorizeService service;
	
	@Inject
	public AuthorizeResource(AuthorizeService service){
		this.service = service;
		log.debug("Creating instance of {}", this);
	}

	// curl -H "Accept: application/json" -H "Content-type: application/json" -X POST -d '{"appId":"app1","sharedDomainToken":"65c7a515-592b-47ca-a1d0-45f8d8419fa1","role":"","random":"57b7aa10-7df9-4182-8a54-e9e95ef2bdee"}' http://localhost:8080/server-auth/rest/authorize
	@POST
	public Response authorize(ReqAuthorizationDTO dto){
		return Response.ok(service.authorize(dto)).build();
	}
	
}
