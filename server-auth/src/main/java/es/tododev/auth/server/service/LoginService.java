package es.tododev.auth.server.service;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.commons.DigestGenerator;
import es.tododev.auth.server.bean.User;
import es.tododev.auth.server.config.ContextParams;

public class LoginService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final EntityManager em;
	private final DigestGenerator digestGenerator;
	private final ContextParams params;
	private final HttpServletResponse response;

	@Inject
	public LoginService(EntityManager em, ContextParams params, DigestGenerator digestGenerator, @Context HttpServletResponse response){
		this.em = em;
		this.digestGenerator = digestGenerator;
		this.params = params;
		this.response = response;
	}
	
	public boolean successLogin(String username, String password){
		User user = null;
		boolean success = false;
		em.getTransaction().begin();
		try{
			user = em.find(User.class, username);
			if(user != null && user.getPassword().equals(digestGenerator.digest(password))){
				String sharedDomainToken = UUID.randomUUID().toString();
				setCookies(sharedDomainToken);
				Date expireSharedDomainToken = new Date(System.currentTimeMillis() + params.getSharedDomainTokenExpireMillis());
				user.setExpireSharedDomainToken(expireSharedDomainToken);
				user.setSharedDomainToken(sharedDomainToken);
				success = true;
			}else{
				log.info("User not match");
			}
			em.getTransaction().commit();
		}catch(Exception e){
			em.getTransaction().rollback();
			log.error("Persist exception", e);
		}finally{
			em.clear();
			em.close();
		}
		return success;
	}
	
	private void setCookies(String sharedDomainToken){
		for(String domain : params.getCrossCookieDomains()){
			Cookie cookie = new Cookie(Constants.SHARED_DOMAINS_COOKIE, sharedDomainToken);
			cookie.setDomain(domain);
			response.addCookie(cookie);
			log.debug("Setting cookie in domain {}", domain);
		}
	}
	
	public boolean register(String username, String password){
		boolean success = false;
		em.getTransaction().begin();
		try{
			User user = em.find(User.class, username);
			if(user == null){
				String sharedDomainToken = UUID.randomUUID().toString();
				user = new User();
				user.setExpireSharedDomainToken(new Date(System.currentTimeMillis() + params.getSharedDomainTokenExpireMillis()));
				user.setPassword(password);
				user.setSharedDomainToken(sharedDomainToken);
				user.setUsername(username);
				em.persist(user);
				setCookies(sharedDomainToken);
			}else{
				log.warn("This user already exists");
			}
			em.getTransaction().commit();
		}catch(Exception e){
			em.getTransaction().rollback();
			log.error("Persist exception", e);
		}finally{
			em.clear();
			em.close();
		}
		return success;
	}
	
}
