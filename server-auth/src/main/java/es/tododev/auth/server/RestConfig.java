package es.tododev.auth.server;

import java.util.logging.Level;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import es.tododev.auth.commons.CookieManager;
import es.tododev.auth.commons.DigestGenerator;
import es.tododev.auth.server.oam.Oam;
import es.tododev.auth.server.provider.AllowCrossDomain;
import es.tododev.auth.server.provider.EmFactoryProvider;
import es.tododev.auth.server.provider.ExceptionLogger;
import es.tododev.auth.server.provider.UUIDgenerator;
import es.tododev.auth.server.resource.AuthorizeResource;
import es.tododev.auth.server.service.ApplicationService;
import es.tododev.auth.server.service.AuthorizeService;
import es.tododev.auth.server.service.GroupApplicationsService;
import es.tododev.auth.server.service.LoginService;
import es.tododev.auth.server.service.RolesService;

public class RestConfig extends ResourceConfig {

	private final static Logger log = LogManager.getLogger();
	public final static String PERSISTENCE_MODEL = "persistenceConfig";

	// For tests
	public RestConfig(Object... injections) {
		for (Object injection : injections) {
			register(injection);
		}
		packages(AuthorizeResource.class.getPackage().getName());
		LoggingFeature logger = new LoggingFeature(new java.util.logging.Logger("Logger", "logger"){
			@Override
			public void log(Level level, String msg) {
				log.debug(msg);
			}

			@Override
			public boolean isLoggable(Level level) {
				return true;
			}
		});
		register(logger);
		
//		property(ServerProperties.TRACING, "ALL");
		register(ExceptionLogger.class);
		register(JacksonFeature.class);
		register(AllowCrossDomain.class);
		log.info("Jersey has been loaded");
	}

	public RestConfig() {
		this(new Binder());
	}

	public static class Binder extends AbstractBinder {

		@Override
		protected void configure() {
			bind(Persistence.createEntityManagerFactory(PERSISTENCE_MODEL)).to(EntityManagerFactory.class);
			bind(AuthorizeService.class).to(AuthorizeService.class);
			bind(DigestGenerator.class).to(DigestGenerator.class).in(Singleton.class);
			bindFactory(EmFactoryProvider.class).to(EntityManager.class);
			bind(LoginService.class).to(LoginService.class);
			bind(Oam.class).to(Oam.class);
			bind(RolesService.class).to(RolesService.class);
			bind(ApplicationService.class).to(ApplicationService.class);
			bind(GroupApplicationsService.class).to(GroupApplicationsService.class);
			bind(CookieManager.class).to(CookieManager.class);
			bind(UUIDgenerator.class).to(UUIDgenerator.class);
		}

	}

}
