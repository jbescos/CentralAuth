package es.tododev.auth.server.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.tododev.auth.commons.DigestGenerator;
import es.tododev.auth.commons.dto.ReqAuthorizationDTO;
import es.tododev.auth.commons.dto.RespAuthorizationDTO;
import es.tododev.auth.server.bean.Application;
import es.tododev.auth.server.bean.UserApplication;
import es.tododev.auth.server.bean.UserRoles;
import es.tododev.auth.server.oam.Oam;
import es.tododev.auth.server.provider.UUIDgenerator;

public class AuthorizeService {

	private final static Logger log = LogManager.getLogger();
	private final EntityManager em;
	private final DigestGenerator digestGenerator;
	private final Oam oam;
	private final UUIDgenerator uuid;
	
	@Inject
	public AuthorizeService(EntityManager em, DigestGenerator digestGenerator, Oam oam, UUIDgenerator uuid){
		this.em = em;
		this.digestGenerator = digestGenerator;
		this.oam = oam;
		this.uuid = uuid;
	}
	
	public RespAuthorizationDTO authorize(ReqAuthorizationDTO in){
		RespAuthorizationDTO out = new RespAuthorizationDTO();
		String sign = null;
		em.getTransaction().begin();
		try{
			List<UserApplication> userApplications = oam.getByColumn(Oam.APP_TOKEN, in.getAppToken(), em, UserApplication.class);
			if(userApplications != null && userApplications.size() == 1){
				UserApplication userApplication = userApplications.get(0);
				out.setUsername(userApplication.getUsername());
				UserRoles userAppRole = em.find(UserRoles.class, new UserRoles.PK(userApplication.getUsername(), userApplication.getAppId()));
				if(checkRoleAndDate(userApplication, userAppRole, in)){
					// update expire token
					Application application = em.find(Application.class, userApplication.getAppId());
					userApplication.setExpireDateToken(new Date(userApplication.getExpireDateToken().getTime()+application.getExpireMillisToken()));
					userApplication.setAppToken(uuid.create());
					out.setNewCookie(userApplication.getAppToken());
					sign = digestGenerator.generateDigest(in.getAppId(), application.getPassword(), in.getAppToken(), in.getRole(), in.getRandom());
					log.info("Created a correct sign {}", sign);
				}else{
					log.warn("Not existing application for appId {}", in.getAppId());
				}
			}else{
				log.warn("Not existing user for AppToken {}", in.getAppToken());
			}
			em.getTransaction().commit();
		}catch(Exception e){
			em.getTransaction().rollback();
			log.error("Persist exception", e);
		}
		if(sign == null){
			log.warn("User is not logged, creating a fake signature");
			sign = digestGenerator.digest(UUID.randomUUID().toString());
		}
		out.setSign(sign);
		return out;
	}
	
	private boolean checkRoleAndDate(UserApplication userApplication, UserRoles userAppRole, ReqAuthorizationDTO in){
		Date now = new Date();
		if(now.getTime() < userApplication.getExpireDateToken().getTime()){
			log.debug("Searching for role {} in {}", in.getRole(), userAppRole.getRoles());
			for(String role : userAppRole.getRoles()){
				if(role.toLowerCase().equals(in.getRole().toLowerCase())){
					log.info("Has the needed role");
					return true;
				}
			}
			log.warn("Hasn't the needed role");
		}else{
			log.info("App token has expired, user needs to log again");
			// TODO remove cookie in client side
		}
		return false;
	}
	
}
