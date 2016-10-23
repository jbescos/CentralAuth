package es.tododev.auth.server.provider;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.hk2.api.Factory;

public class EmFactoryProvider implements Factory<EntityManager>{

	private final static Logger log = LogManager.getLogger();
	private final EntityManagerFactory emf;
	
	@Inject
	public EmFactoryProvider(EntityManagerFactory emf){
		this.emf = emf;
	}
	
	@Override
	public void dispose(EntityManager arg0) {
		log.debug("Closing entity manager: "+arg0.hashCode());
		arg0.clear();
		arg0.close();
	}

	@Override
	public EntityManager provide() {
		EntityManager em = emf.createEntityManager();
		log.debug("Providing new instance of EntityManager: "+em.hashCode());
		return em;
	}

}
