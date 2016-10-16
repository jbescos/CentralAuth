package es.tododev.auth.client;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.commons.CookieManager;

public class CallbackFilter implements Filter{

	private final static Logger log = LogManager.getLogger();
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)arg0;
		HttpServletResponse resp = (HttpServletResponse)arg1;
		CookieManager mgr = new CookieManager();
		String appCookie = req.getParameter(Constants.APP_COOKIE);
		if(appCookie != null){
			// create
			mgr.saveCookie(appCookie, resp);
			log.info("Creating the cookie with value {}", appCookie);
		}else{
			// remove
			mgr.removeCookie(req, resp);
			log.info("Removing the cookie");
		}
		arg2.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		log.info("CallbackFilter is up. You need this filter to manage login and logout in your app. Make sure that auth-server has this path in init-param {}", Constants.AUTHORIZE_REST_PATH);
	}

}
