package es.tododev.auth.client;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.commons.CookieManager;

@SuppressWarnings("serial")
public class CookieMgrServlet extends HttpServlet {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void init() throws ServletException {
		log.info("CookieMgrServlet is up. You need this servlet if this app is hosted in a different domain than de auth-server. Make sure that auth-server has this path in init-param {}", Constants.AUTHORIZE_REST_PATH);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		CookieManager mgr = new CookieManager();
		String sharedDomainToken = req.getParameter(Constants.SHARED_DOMAINS_COOKIE);
		if(sharedDomainToken != null){
			// create
			mgr.saveCookie(sharedDomainToken, resp);
			log.info("Creating the cookie in {} with value {}", req.getPathInfo(), sharedDomainToken);
		}else{
			// remove
			mgr.removeCooke(req, resp);
			log.info("Removing the cookie in {}", req.getPathInfo());
		}
	}

}
