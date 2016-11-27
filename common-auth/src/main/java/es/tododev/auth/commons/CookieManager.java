package es.tododev.auth.commons;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CookieManager {

	private final static Logger log = LogManager.getLogger();
	
	public void saveCookie(String appToken, HttpServletRequest request, HttpServletResponse response){
		Cookie cookie = new Cookie(Constants.APP_COOKIE, appToken);
		cookie.setPath(request.getContextPath());
		cookie.setMaxAge((int)(Constants.EXPIRE_COOKIE/1000));
//		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		request.setAttribute(Constants.APP_COOKIE, appToken);
		log.info("Saving cookie {}", appToken);
	}
	
	public String removeCookie(HttpServletRequest request, HttpServletResponse response){
		Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (Constants.APP_COOKIE.equals(cookie.getName())) {
	            	String appToken = cookie.getValue();
	                cookie.setValue(null);
	                cookie.setMaxAge(0);
	                response.addCookie(cookie);
	                log.info("Removing cookie {}", cookie);
	                return appToken;
	            }
	        }
	    }
	    return null;
	}
	
	public String getCookieValue(String cookieName, HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	        	if(cookieName.equals(cookie.getName()))
	        		return cookie.getValue();
	        }
	    }
	    return null;
	}
	
}
