package es.tododev.auth.commons;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieManager {

	public void saveCookie(String appToken, HttpServletResponse response){
		Cookie cookie = new Cookie(Constants.APP_COOKIE, appToken);
//		cookie.setPath("/");
		response.addCookie(cookie);
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
