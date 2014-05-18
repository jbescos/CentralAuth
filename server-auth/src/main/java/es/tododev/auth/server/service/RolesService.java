package es.tododev.auth.server.service;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tododev.auth.server.bean.Application;
import es.tododev.auth.server.bean.User;
import es.tododev.auth.server.bean.UserRoles;


public class RolesService {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final EntityManager em;
	
	@Inject
	public RolesService(EntityManager em){
		this.em = em;
	}
	
	public void addRole(String username, String appId, String role){
		em.getTransaction().begin();
		try{
			User user = em.find(User.class, username);
			Application app = em.find(Application.class, appId);
			if(user != null && app != null){
				UserRoles userRoles = new UserRoles();
				userRoles.setAppId(appId);
				userRoles.getApps().add(app);
				userRoles.getRoles().add(role);
				userRoles.setUsername(username);
				userRoles.getUsers().add(user);
				user.setUserRoles(userRoles);
				app.setAppsRoles(userRoles);
				em.persist(userRoles);
			}
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
