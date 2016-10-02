package es.tododev.auth.server;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;

import es.tododev.auth.client.AuthorizationFilter;
import es.tododev.auth.client.IAppProvider;
import es.tododev.auth.server.bean.Application;

public class AuthorizationEmFilter extends AuthorizationFilter{

	private final EntityManagerFactory emf;
	
	public AuthorizationEmFilter(EntityManagerFactory emf){
		this.emf = emf;
	}
	
	public AuthorizationEmFilter() {
		this(Persistence.createEntityManagerFactory(RestConfig.PERSISTENCE_MODEL));
	}
	
	@Override
	protected IAppProvider getProvider(HttpServletRequest request) {
		String appId = extractAppId(request);
		EntityManager em = emf.createEntityManager();
		Application application = em.find(Application.class, appId);
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
	
	private String extractAppId(HttpServletRequest request){
		final String POST_BLOCK = "auth";
		String[] blocks = request.getPathInfo().split("/");
		for(int i=0;i<blocks.length;i++){
			if(POST_BLOCK.equals(blocks[i])){
				if(i>0){
					return blocks[i-1];
				}else{
					throw new IllegalArgumentException("The path "+request.getPathInfo()+" doesn't contain the app id before the /auth section");
				}
			}
		}
		throw new IllegalArgumentException("The path "+request.getPathInfo()+" doesn't contain the /auth section");
	}
	
}
