package es.tododev.auth.commons;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

public class CookieManager {

	public void saveCookie(String sharedDomainToken, HttpServletResponse response){
		Cookie cookie = new Cookie(Constants.SHARED_DOMAINS_COOKIE, sharedDomainToken);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	public String removeCooke(HttpServletRequest request, HttpServletResponse response){
		Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (Constants.SHARED_DOMAINS_COOKIE.equals(cookie.getName())) {
	            	String sharedDomainToken = cookie.getValue();
	                cookie.setValue(null);
	                cookie.setMaxAge(0);
	                cookie.setPath("/");
	                response.addCookie(cookie);
	                return sharedDomainToken;
	            }
	        }
	    }
	    return null;
	}
	
}
