package es.tododev.auth.server.service;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tododev.auth.server.bean.Application;

public class ApplicationService {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final EntityManager em;
	
	@Inject
	public ApplicationService(EntityManager em){
		this.em = em;
	}
	
	public void addApplication(String appId, String password){
		em.getTransaction().begin();
		try{
			Application application = new Application();
			application.setAppId(appId);
			application.setPassword(password);
			em.persist(application);
			em.getTransaction().commit();
		}catch(Exception e){
			em.getTransaction().rollback();
			log.error("Persist exception", e);
		}finally{
			em.clear();
			em.close();
		}
	}
	
}
