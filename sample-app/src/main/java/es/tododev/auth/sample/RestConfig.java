package es.tododev.auth.sample;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.tododev.auth.sample.resource.DummyResource;

public class RestConfig extends ResourceConfig {

	private final Logger log = LoggerFactory.getLogger(getClass());

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
