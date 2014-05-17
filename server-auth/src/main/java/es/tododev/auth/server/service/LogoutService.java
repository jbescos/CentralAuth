package es.tododev.auth.server.service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.server.bean.User;
import es.tododev.auth.server.oam.Oam;

public class LogoutService {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final EntityManager em;
	private final HttpServletResponse response;
	private final HttpServletRequest request;
	private final Oam oam;

	@Inject
	public LogoutService(EntityManager em, @Context HttpServletResponse response, @Context HttpServletRequest request, Oam oam){
		this.em = em;
		this.response = response;
		this.request = request;
		this.oam = oam;
	}
	
	public void logout(){
		em.getTransaction().begin();
		try{
			String sharedDomainToken = removeCrossDomainCookie();
			List<User> users = oam.getUserBySharedDomainToken(sharedDomainToken, em);
			if(users != null && users.size() == 1){
				User user = users.get(0);
				user.setExpireSharedDomainToken(new Date(0));
				user.setSharedDomainToken("logout");
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
	
	private String removeCrossDomainCookie(){
		String sharedDomainToken = "";
		Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (Constants.SHARED_DOMAINS_COOKIE.equals(cookie.getName())) {
	            	log.info("Removing cookie {}", cookie);
	            	sharedDomainToken = cookie.getValue();
	                cookie.setValue(null);
	                cookie.setMaxAge(0);
	                cookie.setPath("/");
	                response.addCookie(cookie);
	            }
	        }
	    }
	    return sharedDomainToken;
	}
	
}
