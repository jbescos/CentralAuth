package es.tododev.auth.server.service;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.tododev.auth.server.bean.Application;
import es.tododev.auth.server.bean.GroupApplications;

public class ApplicationService {

	private final static Logger log = LogManager.getLogger();
	private final EntityManager em;
	
	@Inject
	public ApplicationService(EntityManager em){
		this.em = em;
	}
	
	public void addApplication(String groupId, String appId, String password, String url, Long expireMillis, String description){
		em.getTransaction().begin();
		try{
			GroupApplications group = em.find(GroupApplications.class, groupId);
			createApplication(group, appId, password, url, expireMillis, description);
			em.getTransaction().commit();
		}catch(Exception e){
			em.getTransaction().rollback();
			log.error("Persist exception", e);
		}finally{
			em.clear();
			em.close();
		}
	}
	
	public Application createApplication(GroupApplications group, String appId, String password, String url, Long expireMillis, String description){
		Application application = new Application();
		application.setAppId(appId);
		application.setPassword(password);
		application.setExpireMillisToken(expireMillis);
		application.setUrl(url);
		application.setDescription(description);
		group.getApplications().add(application);
		return application;
	}
	
}
