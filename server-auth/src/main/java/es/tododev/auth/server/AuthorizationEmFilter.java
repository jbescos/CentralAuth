package es.tododev.auth.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import es.tododev.auth.client.AuthorizationFilter;
import es.tododev.auth.client.IAppProvider;
import es.tododev.auth.server.bean.Application;
import es.tododev.auth.server.bean.UserApplication;

public class AuthorizationEmFilter extends AuthorizationFilter{

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
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserApplication> cq = cb.createQuery(UserApplication.class);
		Root<UserApplication> root = cq.from(UserApplication.class);
		cq.select(root);
		// FIXME
		TypedQuery<UserApplication> query = em.createQuery(cq).setParameter(3, appToken);
		UserApplication userApp = query.getSingleResult();
		final Application application = em.find(Application.class, userApp.getAppId());
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
