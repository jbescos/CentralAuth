package es.tododev.auth.server.service;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.bean.Application;
import es.tododev.auth.server.bean.GroupApplications;
import es.tododev.auth.server.bean.User;
import es.tododev.auth.server.provider.UUIDgenerator;

public class GroupApplicationsService {

	private final static Logger log = LogManager.getLogger();
	private final UUIDgenerator uuid;
	private final ApplicationService applicationService;
	private final RolesService rolesService;
	private final static String DESCRIPTION = "This is the administration panel to manage the roles and the applications";
	
	@Inject
	public GroupApplicationsService(UUIDgenerator uuid, ApplicationService applicationService, RolesService rolesService){
		this.uuid = uuid;
		this.applicationService = applicationService;
		this.rolesService = rolesService;
	}
	
	public GroupApplications create(User user, String groupId, String username, String url){
		GroupApplications groupApplications = new GroupApplications();
		groupApplications.setGroupId(groupId);
		Application application = applicationService.createApplication(groupApplications, groupId, uuid.create(), url, Constants.EXPIRE_COOKIE, DESCRIPTION);
		rolesService.addRole(user, application, username, Constants.ADMIN_ROLE, Constants.USER_ROLE);
		log.debug("Group application: {}", groupApplications);
		return groupApplications;
	}
	
}
