package es.tododev.auth.server.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tododev.auth.server.bean.Application;
import es.tododev.auth.server.bean.User;
import es.tododev.auth.server.bean.UserRoles;
import es.tododev.auth.server.dto.RolesDTO;
import es.tododev.auth.server.oam.Oam;


public class RolesService {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final EntityManager em;
	private final Oam oam;
	
	@Inject
	public RolesService(EntityManager em, Oam oam){
		this.em = em;
		this.oam = oam;
	}
	
	public RolesDTO getRoles(String sharedDomainToken, String appId){
		RolesDTO dto = new RolesDTO();
		dto.setProperties(new HashMap<String, String>());
		dto.setStatus(RolesDTO.KO);
		em.getTransaction().begin();
		try{
			List<User> users = oam.getUserBySharedDomainToken(sharedDomainToken, em);
			if(users != null && users.size() == 1){
				User user = users.get(0);
				UserRoles userRoles = em.find(UserRoles.class, new UserRoles.PK(user.getUsername(), appId));
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
	
	public void addRole(String username, String appId, String role){
		em.getTransaction().begin();
		try{
			User user = em.find(User.class, username);
			Application app = em.find(Application.class, appId);
			if(user != null && app != null){
				UserRoles userRoles = em.find(UserRoles.class, new UserRoles.PK(username, appId));
				if(userRoles == null){
					userRoles = new UserRoles();
					userRoles.setAppId(appId);
					userRoles.setApplication(app);
					userRoles.setUsername(username);
					userRoles.setUser(user);
					user.getUserRoles().add(userRoles);
					app.getUserRoles().add(userRoles);
					em.persist(userRoles);
				}
				userRoles.getRoles().add(role);
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
