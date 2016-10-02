package es.tododev.auth.server.resource;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.service.GroupApplicationsService;

@Path(Constants.GROUP_APPLICATIONS_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupApplicationsResource {

	private final GroupApplicationsService groupApplicationsService;
	private final HttpServletRequest request;
	
	@Inject
	public GroupApplicationsResource(GroupApplicationsService groupApplicationsService, HttpServletRequest request){
		this.groupApplicationsService = groupApplicationsService;
		this.request = request;
	}
	
	@POST
	public Response createGroupApplication(@QueryParam("groupId") String groupId) {
		String url = request.getRequestURL().toString();
		// FIXME the user name will not exist there. Try to find the best way to make it possible.
		String username = (String)request.getAttribute(Constants.USER_NAME_KEY);
		groupApplicationsService.create(groupId, username, url);
		return Response.ok().build();
	}
	
}
