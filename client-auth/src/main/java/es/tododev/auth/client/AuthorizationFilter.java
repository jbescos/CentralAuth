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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.commons.DigestGenerator;
import es.tododev.auth.commons.dto.ReqAuthorizationDTO;
import es.tododev.auth.commons.dto.RespAuthorizationDTO;

public abstract class AuthorizationFilter implements Filter{
	
	private final ClientConfig clientConfig = new ClientConfig();
	private final static Logger log = LogManager.getLogger();
	private DigestGenerator digestGenerator;
	private static final String AUTH_SERVER_URL = "AuthServerURL";
	private String authorizationURL;
	
	protected abstract IAppProvider getProvider(HttpServletRequest request, String appToken);
	
	@Override
	public void destroy() {
		log.info(getClass().getCanonicalName()+" was destroyed");
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest request = ((HttpServletRequest)arg0);
		String appToken = getAppToken(request);
		if(appToken != null){
			IAppProvider appProvider = getProvider(request, appToken);
			String role = extractRole(request);
			ReqAuthorizationDTO req = createDTO(appToken, role, appProvider.getAppId());
			RespAuthorizationDTO resp = authorize(req, RespAuthorizationDTO.class, authorizationURL);
			if(digestGenerator.generateDigest(appProvider.getAppId(), appProvider.getAppPassword(),  appToken, role, req.getRandom()).equals(resp.getSign())){
				arg0.setAttribute(Constants.USER_NAME_KEY, resp.getUsername());
				arg2.doFilter(arg0, arg1);
			}else{
				log.warn(appToken+ " not authorized");
				sendError(arg1, 403);
			}
		}else{
			log.info("The user is not logged in the system. Cookie "+Constants.APP_COOKIE+" doesn't exist in this domain");
			sendError(arg1, 401);
		}
	}
	
	private <IN, OUT> OUT authorize(IN input, Class<OUT> out, String authorizationURL){
		Entity<IN> in = Entity.entity(input, MediaType.APPLICATION_JSON);
		Client client = ClientBuilder.newBuilder().withConfig(clientConfig).build();
		Response response = client.target(authorizationURL).request().accept(MediaType.APPLICATION_JSON).post(in);
		return response.readEntity(out);
	}
	
	private String getAppToken(HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for(Cookie cookie : cookies){
				if(Constants.APP_COOKIE.equals(cookie.getName())){
					return cookie.getValue();
				}
			}
		}
		return request.getParameter(Constants.APP_COOKIE);
	}
	
	private String extractRole(HttpServletRequest request) throws ServletException{
		String fullPath = request.getRequestURI();
		log.debug("Extracting role from path {}", fullPath);
		String role = new ExtractRoleFromPath().extract(fullPath);
		if(role == null){
			throw new ServletException("Can't find the needed role in path. Be sure that your authentication paths has the next format: .../"+ExtractRoleFromPath.FILTER_AUTH_PATH+"/{role}...");
		}
		return role;
	}
	
	private ReqAuthorizationDTO createDTO(String appToken, String role, String appId){
		ReqAuthorizationDTO dto = new ReqAuthorizationDTO();
		dto.setAppId(appId);
		dto.setRole(role);
		dto.setAppToken(appToken);
		dto.setRandom(UUID.randomUUID().toString());
		return dto;
	}

	private void sendError(ServletResponse arg1, int errorCode) throws IOException{
		HttpServletResponse response = (HttpServletResponse)arg1;
		response.sendError(errorCode);
	}
	
	protected String checkAndGet(String paramName, FilterConfig arg0) throws ServletException {
		String paramValue = arg0.getInitParameter(paramName);
		if(paramValue == null)
			throw new ServletException(paramName+" is null. Add in your web.xml: <context-param><param-name>"+paramName+"</param-name><param-value>{the value}</param-value></context-param>");
		else
			return paramValue;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		log.debug("<filter><filter-name>AuthorizationFilter</filter-name><filter-class>es.tododev.auth.client.AuthorizationFilter</filter-class><init-param><param-name>AppId</param-name><param-value>The app id</param-value></init-param><init-param><param-name>AppPassword</param-name><param-value>The app password of your app</param-value></init-param><init-param><param-name>AuthServerURL</param-name><param-value>http://localhost:8080/server-auth/</param-value></init-param></filter><filter-mapping><filter-name>AuthorizationFilter</filter-name><url-pattern>*/auth/*</url-pattern></filter-mapping> ");
		String authServerURL = checkAndGet(AUTH_SERVER_URL, arg0);
		authorizationURL = authServerURL + Constants.AUTHORIZE_REST_PATH;
		try {
			digestGenerator = new DigestGenerator();
		} catch (NoSuchAlgorithmException e) {
			throw new ServletException("Can't start filter because there is no algorithm to make the digest", e);
		}
		clientConfig.register(JacksonFeature.class);
		clientConfig.register(new LoggingFilter(java.util.logging.Logger.getLogger(LoggingFilter.class.getName()), true));
		
		log.info(getClass().getCanonicalName()+" was successfully loaded");
	}

}
