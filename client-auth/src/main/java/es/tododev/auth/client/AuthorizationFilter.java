package es.tododev.auth.client;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.commons.DigestGenerator;
import es.tododev.auth.commons.dto.ReqAuthorizationDTO;
import es.tododev.auth.commons.dto.RespAuthorizationDTO;

public class AuthorizationFilter implements Filter{

	public static final String FILTER_AUTH_PATH = "auth";
	public static final String APP_ID = "AppId";
	public static final String APP_PASSWORD = "AppPassword";
	public static final String AUTH_SERVER_URL = "AuthServerURL";
	
	private final ClientConfig clientConfig = new ClientConfig();
	private final Logger log = LoggerFactory.getLogger(getClass());
	private DigestGenerator digestGenerator;
	private String authorizationURL;
	
	private String appId;
	private String appPassword;
	private String authServerURL;
	
	
	@Override
	public void destroy() {
		log.info(getClass().getCanonicalName()+" was destroyed");
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = ((HttpServletRequest)arg0);
		String sharedDomainToken = getSharedDomainToken(request);
		if(sharedDomainToken != null){
			String role = extractRole(request);
			ReqAuthorizationDTO req = createDTO(sharedDomainToken, role);
			RespAuthorizationDTO resp = authorize(req, RespAuthorizationDTO.class);
			if(digestGenerator.generateDigest(appId, appPassword,  sharedDomainToken, role, req.getRandom()).equals(resp.getSign())){
				arg2.doFilter(arg0, arg1);
			}else{
				log.warn(sharedDomainToken+ " not authorized");
				sendError(arg1, 403);
			}
		}else{
			log.info("The user is not logged in the system. Cookie "+Constants.SHARED_DOMAINS_COOKIE+" doesn't exist in this domain");
			sendError(arg1, 401);
		}
	}
	
	private <IN, OUT> OUT authorize(IN input, Class<OUT> out){
		Entity<IN> in = Entity.entity(input, MediaType.APPLICATION_JSON);
		Client client = ClientBuilder.newBuilder().withConfig(clientConfig).build();
		Response response = client.target(authorizationURL).request().post(in);
		return response.readEntity(out);
	}
	
	private String getSharedDomainToken(HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies){
			if(Constants.SHARED_DOMAINS_COOKIE.equals(cookie.getName())){
				return cookie.getValue();
			}
		}
		return null;
	}
	
	private String extractRole(HttpServletRequest request) throws ServletException{
		String role = null;
		String fullPath = request.getRequestURI();
		String[] words = fullPath.split("/");
		for(int i = 0; i < words.length ; i++){
			String word = words[i];
			if(FILTER_AUTH_PATH.equals(FILTER_AUTH_PATH) && i + 1 <= words.length){
				role = word;
				log.debug("The role needed for this path is: "+role);
				break;
			}
		}
		if(role == null){
			throw new ServletException("Can't find the needed role in path. Be sure that your authentication paths has the next format: .../"+FILTER_AUTH_PATH+"/{role}...");
		}
		return role;
	}
	
	private ReqAuthorizationDTO createDTO(String sharedDomainToken, String role){
		ReqAuthorizationDTO dto = new ReqAuthorizationDTO();
		dto.setAppId(appId);
		dto.setRole(role);
		dto.setSharedDomainToken(sharedDomainToken);
		dto.setRandom(UUID.randomUUID().toString());
		return dto;
	}

	private void sendError(ServletResponse arg1, int errorCode) throws IOException{
		HttpServletResponse response = (HttpServletResponse)arg1;
		response.sendError(errorCode);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		log.debug("<filter><filter-name>AuthorizationFilter</filter-name><filter-class>es.tododev.auth.AuthorizationFilter</filter-class><init-param><param-name>AppId</param-name><param-value>The app id</param-value></init-param><init-param><param-name>AppPassword</param-name><param-value>The app password of your app</param-value></init-param><init-param><param-name>AuthServerURL</param-name><param-value>The domain of your auth server</param-value></init-param></filter><filter-mapping><filter-name>AuthorizationFilter</filter-name><url-pattern>*/auth/*</url-pattern></filter-mapping> ");
		appId = checkAndGet(APP_ID, arg0);
		appPassword = checkAndGet(APP_PASSWORD, arg0);
		authServerURL = checkAndGet(AUTH_SERVER_URL, arg0);
		authorizationURL = authServerURL + Constants.AUTHORIZE_PATH;
		try {
			digestGenerator = new DigestGenerator();
		} catch (NoSuchAlgorithmException e) {
			throw new ServletException("Can't start filter because there is no algorithm to make the digest", e);
		}
		clientConfig.register(JacksonFeature.class);
		clientConfig.register(new LoggingFilter(java.util.logging.Logger.getLogger(LoggingFilter.class.getName()), true));
		
		log.info(getClass().getCanonicalName()+" was successfully loaded");
	}
	
	private String checkAndGet(String paramName, FilterConfig arg0) throws ServletException {
		String paramValue = arg0.getInitParameter(paramName);
		if(paramValue == null)
			throw new ServletException(paramName+" is null. Add in your web.xml: <context-param><param-name>"+paramName+"</param-name><param-value>{the value}</param-value></context-param>");
		else
			return paramValue;
	}

}
