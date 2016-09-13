package es.tododev.auth.server.provider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.hk2.api.Factory;

@Singleton
public class EntityManagerProvider implements Factory<EntityManager>{

	private final static Logger log = LogManager.getLogger();
	private final EntityManagerFactory emf;
	
	@Inject
	public EntityManagerProvider(EntityManagerFactory emf){
		this.emf = emf;
	}
	
	@Override
	public void dispose(EntityManager arg0) {
		log.info("Closing entity manager factory");
		emf.close();
	}

	@Override
	public EntityManager provide() {
		log.debug("Providing new instance of EntityManager");
		return emf.createEntityManager();
	}

}
