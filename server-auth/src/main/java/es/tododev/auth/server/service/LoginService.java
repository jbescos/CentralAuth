package es.tododev.auth.server.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.commons.CookieManager;
import es.tododev.auth.commons.DigestGenerator;
import es.tododev.auth.server.bean.Application;
import es.tododev.auth.server.bean.User;
import es.tododev.auth.server.bean.UserApplication;
import es.tododev.auth.server.oam.Oam;
import es.tododev.auth.server.provider.UUIDgenerator;

public class LoginService {
	
	private final static Logger log = LogManager.getLogger();
	private final EntityManager em;
	private final DigestGenerator digestGenerator;
	private final HttpServletResponse response;
	private final CookieManager cookieMgr;
	private final HttpServletRequest request;
	private final Oam oam;
	private final UUIDgenerator uuid;

	@Inject
	public LoginService(UUIDgenerator uuid, EntityManager em, DigestGenerator digestGenerator, @Context HttpServletResponse response, @Context HttpServletRequest request, CookieManager cookieMgr, Oam oam){
		this.em = em;
		this.digestGenerator = digestGenerator;
		this.response = response;
		this.cookieMgr = cookieMgr;
		this.request = request;
		this.oam = oam;
		this.uuid = uuid;
	}
	
	public List<String> successLogin(String username, String password, String appId) throws LoginException, Exception{
		List<String> urls = Collections.emptyList();
		em.getTransaction().begin();
		try{
			urls = loginOperations(username, password, appId);
			em.getTransaction().commit();
		}catch(LoginException e){
			em.getTransaction().commit();
			log.error("Login failure", e);
			throw e;
		}catch(Exception e){
			em.getTransaction().rollback();
			log.error("Persist exception", e);
			throw e;
		}finally{
			em.clear();
			em.close();
		}
		return urls;
	}
	
	private void removeAppTokens(Application application, String username){
		Map<String,String> columnValue = new HashMap<>();
		columnValue.put(Oam.GROUP_ID, application.getGroupApplications().getGroupId());
		columnValue.put(Oam.USER_NAME, username);
		List<UserApplication> userApplications = oam.getByColumns(columnValue, em, UserApplication.class);
		for(UserApplication userApplication : userApplications){
			em.remove(userApplication);
		}
	}
	
	private List<String> createAppTokensAndResponse(Application application, String username){
		long currentMillis = System.currentTimeMillis();
		List<String> urls = new ArrayList<>();
		for(Application app : application.getGroupApplications().getApplications()){
			UserApplication userApp = new UserApplication();
			String appToken = uuid.create();
			userApp.setAppId(app.getAppId());
			userApp.setAppToken(appToken);
			userApp.setExpireDateToken(new Date(app.getExpireMillisToken() + currentMillis));
			userApp.setGroupId(application.getGroupApplications().getGroupId());
			userApp.setUsername(username);
			em.persist(userApp);
			String url = getUrl(appToken, app, true);
			urls.add(url);
		}
		return urls;
	}
	
	private List<String> loginOperations(String username, String password, String appId) throws LoginException{
		User user = em.find(User.class, username);
		if(user != null && user.getPassword().equals(digestGenerator.digest(password))){
			if(appId != null){
				Application application = em.find(Application.class, appId);
				if(application != null){
					removeAppTokens(application, username);
					return createAppTokensAndResponse(application, username);
				}else{
					log.warn("App Id {} doesn't exist", appId);
					throw new LoginException("The application "+appId+" doesn't exist in the system.");
				}
			}
			return Collections.emptyList();
		}else{
			log.info("User not match");
		}
		throw new LoginException("Invalid user, password or both.");
	}
	
	public List<String> register(String username, String password, String appId) throws LoginException{
		em.getTransaction().begin();
		try{
			User user = em.find(User.class, username);
			if(user == null){
				user = new User();
				user.setPassword(digestGenerator.digest(password));
				user.setUsername(username);
				em.persist(user);
				return loginOperations(username, password, appId);
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
		throw new LoginException("Can not register");
	}
	
	public List<String> logout() throws LoginException{
		String appSystemToken = cookieMgr.getCookieValue(Constants.APP_COOKIE, request);
		cookieMgr.removeCookie(request, response);
		em.getTransaction().begin();
		try{
			List<UserApplication> userApplications = oam.getByColumn(Oam.APP_TOKEN, appSystemToken, em, UserApplication.class);
			if(userApplications != null && userApplications.size() == 1){
				UserApplication userApplication = userApplications.get(0);
				Application application = em.find(Application.class, userApplication.getAppId());
				removeAppTokens(application, userApplication.getUsername());
				List<String> urls = new ArrayList<>();
				for(Application app : application.getGroupApplications().getApplications()){
					String url = getUrl(null, app, false);
					urls.add(url);
				}
				return urls;
			}
			em.getTransaction().commit();
		}catch(Exception e){
			em.getTransaction().rollback();
			log.error("Persist exception", e);
		}finally{
			em.clear();
			em.close();
		}
		return Collections.emptyList();
	}
	
	private String getUrl(String appToken, Application app, boolean login){
		String path = login ? app.getUrl()+Constants.LOGIN_PATH : app.getUrl()+Constants.LOGOUT_PATH;
		UriBuilder builder = new JerseyUriBuilder().path(path);
		if(appToken != null)
			builder.queryParam(Constants.APP_COOKIE, appToken);
		String cookiePathMgrValue = builder.build().toString();
		log.debug("Url: {}", cookiePathMgrValue);
		return cookiePathMgrValue;
	}
	
}
