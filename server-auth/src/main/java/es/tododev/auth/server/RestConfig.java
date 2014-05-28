package es.tododev.auth.server;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tododev.auth.commons.CookieManager;
import es.tododev.auth.commons.DigestGenerator;
import es.tododev.auth.server.config.ContextParams;
import es.tododev.auth.server.oam.Oam;
import es.tododev.auth.server.provider.EntityManagerProvider;
import es.tododev.auth.server.provider.ExceptionLogger;
import es.tododev.auth.server.provider.UUIDgenerator;
import es.tododev.auth.server.resource.AuthorizeResource;
import es.tododev.auth.server.service.ApplicationService;
import es.tododev.auth.server.service.AuthorizeService;
import es.tododev.auth.server.service.LoginService;
import es.tododev.auth.server.service.RolesService;

public class RestConfig extends ResourceConfig {

	private final Logger log = LoggerFactory.getLogger(getClass());

	// For tests
	public RestConfig(Object... injections) {
		for (Object injection : injections) {
			register(injection);
		}
		packages(AuthorizeResource.class.getPackage().getName());
		register(new LoggingFilter(java.util.logging.Logger.getLogger(LoggingFilter.class.getName()), true));
		
		property(ServerProperties.TRACING, "ALL");
		register(ExceptionLogger.class);
		register(JacksonFeature.class);
//		register(MvcFeature.class);
//		property(MvcFeature.TEMPLATE_BASE_PATH, "/");
		log.info("Jersey has been loaded");
	}

	public RestConfig() {
		this(new Binder());
	}

	public static class Binder extends AbstractBinder {

		@Override
		protected void configure() {
			bind(Persistence.createEntityManagerFactory("persistenceConfig")).to(EntityManagerFactory.class);
			bind(AuthorizeService.class).to(AuthorizeService.class);
			bind(DigestGenerator.class).to(DigestGenerator.class).in(Singleton.class);
			bindFactory(EntityManagerProvider.class).to(EntityManager.class);
			bind(ContextParams.class).to(ContextParams.class);
			bind(LoginService.class).to(LoginService.class);
			bind(Oam.class).to(Oam.class);
			bind(RolesService.class).to(RolesService.class);
			bind(ApplicationService.class).to(ApplicationService.class);
			bind(CookieManager.class).to(CookieManager.class);
			bind(UUIDgenerator.class).to(UUIDgenerator.class);
		}

	}

}
