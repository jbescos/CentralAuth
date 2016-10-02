package es.tododev.auth.server.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.tododev.auth.server.bean.Application;
import es.tododev.auth.server.bean.User;
import es.tododev.auth.server.bean.UserApplication;
import es.tododev.auth.server.bean.UserRoles;
import es.tododev.auth.server.dto.RolesDTO;
import es.tododev.auth.server.oam.Oam;


public class RolesService {

	private final static Logger log = LogManager.getLogger();
	private final EntityManager em;
	private final Oam oam;
	
	@Inject
	public RolesService(EntityManager em, Oam oam){
		this.em = em;
		this.oam = oam;
	}
	
	public RolesDTO getRoles(String appToken, String appId){
		RolesDTO dto = new RolesDTO();
		dto.setProperties(new HashMap<String, String>());
		dto.setStatus(RolesDTO.KO);
		em.getTransaction().begin();
		try{
			List<UserApplication> userApplications = oam.getByColumn(Oam.APP_TOKEN, appToken, em, UserApplication.class);
			if(userApplications != null && userApplications.size() == 1){
				UserApplication userApplication = userApplications.get(0);
				UserRoles userRoles = em.find(UserRoles.class, new UserRoles.PK(userApplication.getUsername(), appId));
				if(userRoles != null){
					dto.setRoles(new HashSet<String>(userRoles.getRoles()));
					dto.setStatus(RolesDTO.OK);
				}
			}
			em.getTransaction().commit();
		}catch(Exception e){
			dto.setStatus(RolesDTO.ERR);
			em.getTransaction().rollback();
			log.error("Persist exception", e);
		}finally{
			em.clear();
			em.close();
		}
		return dto;
	}
	
	public void addRole(EntityManager em, Application app, String username, String role){
		User user = em.find(User.class, username);
		if(user != null && app != null){
			UserRoles userRoles = em.find(UserRoles.class, new UserRoles.PK(username, app.getAppId()));
			if(userRoles == null){
				userRoles = new UserRoles();
				userRoles.setAppId(app.getAppId());
				userRoles.setApplication(app);
				userRoles.setUsername(username);
				userRoles.setUser(user);
				user.getUserRoles().add(userRoles);
				app.getUserRoles().add(userRoles);
				em.persist(userRoles);
			}
			userRoles.getRoles().add(role);
		}else{
			throw new IllegalArgumentException("The user "+username+" or the application doesn't exist");
		}
	}
	
	public void addRole(String username, String appId, String role){
		em.getTransaction().begin();
		try{
			Application app = em.find(Application.class, appId);
			addRole(em, app, username, role);
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
