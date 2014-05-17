package es.tododev.auth.server.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tododev.auth.commons.DigestGenerator;
import es.tododev.auth.commons.dto.ReqAuthorizationDTO;
import es.tododev.auth.commons.dto.RespAuthorizationDTO;
import es.tododev.auth.server.bean.Application;
import es.tododev.auth.server.bean.User;
import es.tododev.auth.server.config.ContextParams;

public class AuthorizeService {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final EntityManager em;
	private final DigestGenerator digestGenerator;
	private final ContextParams params;
	
	@Inject
	public AuthorizeService(EntityManager em, DigestGenerator digestGenerator, ContextParams params){
		this.em = em;
		this.digestGenerator = digestGenerator;
		this.params = params;
	}
	
	public RespAuthorizationDTO authorize(ReqAuthorizationDTO in){
		RespAuthorizationDTO out = new RespAuthorizationDTO();
		String sign = null;
		em.getTransaction().begin();
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<User> criteria = cb.createQuery(User.class);
			Root<User> enhancedUser = criteria.from(User.class);
			Predicate predicate = cb.equal(enhancedUser.get("sharedDomainToken"), in.getSharedDomainToken());
			List<User> users = em.createQuery(criteria.select(enhancedUser).where(predicate)).getResultList();
			if(users != null && users.size() == 1){
				Application application = em.find(Application.class, in.getAppId());
				User user = users.get(0);
				if(application != null && checkRoleAndDate(user, in)){
					// update expire token
					user.setExpireSharedDomainToken(new Date(user.getExpireSharedDomainToken().getTime()+params.getSharedDomainTokenExpireMillis()));
					sign = digestGenerator.generateDigest(in.getAppId(), application.getPassword(), in.getSharedDomainToken(), in.getRole(), in.getRandom());
					log.info("Created a correct sign {}", sign);
				}else{
					log.warn("Not existing application for appId {}", in.getAppId());
				}
			}else{
				log.warn("Not existing user for sharedDomainToken {}", in.getSharedDomainToken());
			}
			em.getTransaction().commit();
		}catch(Exception e){
			em.getTransaction().rollback();
			log.error("Persist exception", e);
		}finally{
			em.clear();
			em.close();
		}
		if(sign == null){
			log.warn("User is not logged, creating a fake signature");
			sign = digestGenerator.digest(UUID.randomUUID().toString());
		}
		out.setSign(sign);
		return out;
	}
	
	private boolean checkRoleAndDate(User user, ReqAuthorizationDTO in){
		Date now = new Date();
		if(now.getTime() < user.getExpireSharedDomainToken().getTime()){
			for(String role : user.getUserRoles().getRoles()){
				if(role.toLowerCase().equals(in.getRole().toLowerCase())){
					log.info("Has the needed role");
					return true;
				}
			}
			log.warn("Hasn't the needed role");
		}else{
			log.info("sharedDomainToken has expired, user needs to log again");
			// TODO remove cookie in client side
		}
		return false;
	}
	
}
