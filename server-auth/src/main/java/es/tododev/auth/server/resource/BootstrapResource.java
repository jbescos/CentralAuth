package es.tododev.auth.server.resource;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.service.GroupApplicationsService;
import es.tododev.auth.server.service.LoginService;

@Path(Constants.BOOTSTRAP)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BootstrapResource {

	private final static Logger log = LogManager.getLogger();
	private final GroupApplicationsService groupApplicationsService;
	private final LoginService loginService;
	private final UriInfo uriInfo;
	
	@Inject
	public BootstrapResource(UriInfo uriInfo, GroupApplicationsService groupApplicationsService, LoginService loginService) {
		this.uriInfo = uriInfo;
		this.groupApplicationsService = groupApplicationsService;
		this.loginService = loginService;
	}
	
	@GET
	public Response init(@QueryParam("username") String username, @QueryParam("password") String password){
		try{
			List<String> urls = loginService.register(username, password, null);
			String url = uriInfo.getBaseUri().getPath();
			log.info("URL: "+url);
			groupApplicationsService.create(Constants.MAIN_APP, username, url);
			log.info("Group created: "+Constants.MAIN_APP);
			return Response.ok(urls).build();
		}catch(Exception e){
			log.error("Can not run bootstrap");
			return Response.status(500).build();
		}
	}
	
}
