package es.tododev.auth.server.service;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.bean.Application;
import es.tododev.auth.server.bean.GroupApplications;
import es.tododev.auth.server.provider.UUIDgenerator;

public class GroupApplicationsService {

	private final static Logger log = LogManager.getLogger();
	private final EntityManager em;
	private final UUIDgenerator uuid;
	private final ApplicationService applicationService;
	private final RolesService rolesService;
	private final static long EXPIRE_COOKIE = 10*1000*60;
	private final static String DESCRIPTION = "This is the administration panel to manage the roles and the applications";
	
	@Inject
	public GroupApplicationsService(EntityManager em, UUIDgenerator uuid, ApplicationService applicationService, RolesService rolesService){
		this.em = em;
		this.uuid = uuid;
		this.applicationService = applicationService;
		this.rolesService = rolesService;
	}
	
	public void create(String groupId, String username, String url){
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		try{
			GroupApplications groupApplications = new GroupApplications();
			groupApplications.setGroupId(groupId);
			Application application = applicationService.createApplication(groupApplications, groupId, uuid.create(), url, EXPIRE_COOKIE, DESCRIPTION);
			log.debug("Persisting "+groupApplications);
			em.persist(groupApplications);
			tx.commit();
			rolesService.addRole(username, application.getAppId(), Constants.ADMIN_ROLE);
		}catch(Exception e){
			tx.rollback();
			log.error("Persist exception", e);
		}finally{
			em.clear();
			em.close();
		}
	}
	
}
