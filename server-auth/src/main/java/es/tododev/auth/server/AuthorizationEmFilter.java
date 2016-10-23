package es.tododev.auth.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.tododev.auth.client.AuthorizationFilter;
import es.tododev.auth.client.IAppProvider;
import es.tododev.auth.server.bean.Application;
import es.tododev.auth.server.oam.Oam;

public class AuthorizationEmFilter extends AuthorizationFilter{

	private final static Logger log = LogManager.getLogger();
	private final EntityManagerFactory emf;
	
	public AuthorizationEmFilter(EntityManagerFactory emf){
		this.emf = emf;
	}
	
	public AuthorizationEmFilter() {
		this(Persistence.createEntityManagerFactory(RestConfig.PERSISTENCE_MODEL));
	}
	
	@Override
	protected IAppProvider getProvider(HttpServletRequest request, String appToken) {
		EntityManager em = emf.createEntityManager();
		log.debug("Loading app by "+appToken);
		Oam oam = new Oam();
		final Application application = oam.getApplicationByAppToken(em, appToken);
		em.clear();
		em.close();
		return new IAppProvider() {
			@Override
			public String getAppPassword() {
				return application.getPassword();
			}
			@Override
			public String getAppId() {
				return application.getAppId();
			}
		};
	}
	
}
