package es.tododev.auth.server.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.commons.CookieManager;
import es.tododev.auth.commons.DigestGenerator;
import es.tododev.auth.server.bean.User;
import es.tododev.auth.server.config.ContextParams;
import es.tododev.auth.server.oam.Oam;

public class LoginService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final EntityManager em;
	private final DigestGenerator digestGenerator;
	private final ContextParams params;
	private final HttpServletResponse response;
	private final CookieManager cookieMgr;
	private final HttpServletRequest request;
	private final Oam oam;

	@Inject
	public LoginService(EntityManager em, ContextParams params, DigestGenerator digestGenerator, @Context HttpServletResponse response, @Context HttpServletRequest request, CookieManager cookieMgr, Oam oam){
		this.em = em;
		this.digestGenerator = digestGenerator;
		this.params = params;
		this.response = response;
		this.cookieMgr = cookieMgr;
		this.request = request;
		this.oam = oam;
	}
	
	public boolean successLogin(String username, String password){
		User user = null;
		boolean success = false;
		em.getTransaction().begin();
		try{
			user = em.find(User.class, username);
			if(user != null && user.getPassword().equals(digestGenerator.digest(password))){
				String sharedDomainToken = UUID.randomUUID().toString();
				cookieMgr.saveCookie(sharedDomainToken, response);
				Date expireSharedDomainToken = new Date(System.currentTimeMillis() + params.getSharedDomainTokenExpireMillis());
				user.setExpireSharedDomainToken(expireSharedDomainToken);
				user.setSharedDomainToken(sharedDomainToken);
				success = true;
				addAttributes(sharedDomainToken, username);
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
	
	public boolean register(String username, String password){
		boolean success = false;
		em.getTransaction().begin();
		try{
			User user = em.find(User.class, username);
			if(user == null){
				String sharedDomainToken = UUID.randomUUID().toString();
				user = new User();
				user.setExpireSharedDomainToken(new Date(System.currentTimeMillis() + params.getSharedDomainTokenExpireMillis()));
				user.setPassword(digestGenerator.digest(password));
				user.setSharedDomainToken(sharedDomainToken);
				user.setUsername(username);
				em.persist(user);
				cookieMgr.saveCookie(sharedDomainToken, response);
				addAttributes(sharedDomainToken, username);
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
	
	public void logout(){
		String username = null;
		em.getTransaction().begin();
		try{
			String sharedDomainToken = cookieMgr.removeCooke(request, response);
			if(sharedDomainToken != null){
				List<User> users = oam.getUserBySharedDomainToken(sharedDomainToken, em);
				if(users != null && users.size() == 1){
					User user = users.get(0);
					user.setExpireSharedDomainToken(new Date(0));
					user.setSharedDomainToken("logout");
					username = user.getUsername();
				}
			}
			addAttributes(null, username);
			em.getTransaction().commit();
		}catch(Exception e){
			em.getTransaction().rollback();
			log.error("Persist exception", e);
		}finally{
			em.clear();
			em.close();
		}
	}
	
	private void addAttributes(String sharedDomainToken, String username){
		int i = 0;
		for(String cookiePathMgr : params.getCrossCookieDomains()){
			UriBuilder builder = new JerseyUriBuilder().path(cookiePathMgr);
			if(sharedDomainToken != null)
				builder.queryParam(Constants.SHARED_DOMAINS_COOKIE, sharedDomainToken);
			if(username != null)
				builder.queryParam(Constants.USER_NAME_KEY, username);
			String cookiePathMgrValue = builder.build().toString();
			request.setAttribute(Integer.toString(i), cookiePathMgrValue);
			log.debug("Adding attribute {} = {}", i, cookiePathMgrValue);
			i++;
		}
	}
	
}
