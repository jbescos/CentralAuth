package es.tododev.auth.server.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.tododev.auth.server.aop.Transactional;
import es.tododev.auth.server.bean.Application;
import es.tododev.auth.server.bean.User;
import es.tododev.auth.server.bean.UserApplication;
import es.tododev.auth.server.bean.UserRoles;
import es.tododev.auth.server.dto.RolesDTO;
import es.tododev.auth.server.oam.Oam;


public class RolesService {

	private final static Logger log = LogManager.getLogger();
	private final Oam oam;
	
	@Inject
	public RolesService(Oam oam){
		this.oam = oam;
	}
	
	@Transactional
	public RolesDTO getRoles(EntityManager em, String appToken, String appId){
		RolesDTO dto = new RolesDTO();
		dto.setProperties(new HashMap<String, String>());
		dto.setStatus(RolesDTO.KO);
		List<UserApplication> userApplications = oam.getByColumn(Oam.APP_TOKEN, appToken, em, UserApplication.class);
		if(userApplications != null && userApplications.size() == 1){
			UserApplication userApplication = userApplications.get(0);
			UserRoles userRoles = em.find(UserRoles.class, new UserRoles.PK(userApplication.getUsername(), appId));
			if(userRoles != null){
				dto.setRoles(new HashSet<String>(userRoles.getRoles()));
				dto.setStatus(RolesDTO.OK);
			}
		}
		return dto;
	}
	
	public UserRoles addRole(EntityManager em, User user, Application app, String username, String ... roles){
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
			}
			for(String role : roles){
				userRoles.getRoles().add(role);
			}
			return userRoles;
		}else{
			throw new IllegalArgumentException("The user "+username+" or the application "+app+" doesn't exist");
		}
	}
	
	@Transactional
	public void addRole(EntityManager em, String username, String appId, String ... roles){
		Application app = em.find(Application.class, appId);
		User user = em.find(User.class, username);
		UserRoles userRoles = addRole(em, user, app, username, roles);
		em.persist(userRoles);
	}
	
}
