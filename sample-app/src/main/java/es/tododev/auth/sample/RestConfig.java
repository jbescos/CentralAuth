package es.tododev.auth.sample;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import es.tododev.auth.sample.resource.DummyResource;

public class RestConfig extends ResourceConfig {

	private final static Logger log = LogManager.getLogger();

	// For tests
	public RestConfig(Object... injections) {
		for (Object injection : injections) {
			register(injection);
		}
		packages(DummyResource.class.getPackage().getName());
		register(new LoggingFilter(java.util.logging.Logger.getLogger(LoggingFilter.class.getName()), true));
		
		property(ServerProperties.TRACING, "ALL");
		register(JacksonFeature.class);
		log.info("Jersey has been loaded");
	}

	public RestConfig() {
		this(new Binder());
	}

	public static class Binder extends AbstractBinder {

		@Override
		protected void configure() {
		}

	}

}
