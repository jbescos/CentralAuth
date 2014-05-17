package es.tododev.auth.server;

import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import es.tododev.auth.server.resource.AuthorizeResource;

public class RestConfig extends ResourceConfig{

private final Log log = LogFactory.getLog(getClass());
	
	// For tests
 	public RestConfig(Object ... injections){
 		for(Object injection : injections){
 			register(injection);
 		}
 		packages(AuthorizeResource.class.getPackage().getName());
 		register(new LoggingFilter(Logger.getLogger(LoggingFilter.class.getName()), true));
 		
        property(ServerProperties.TRACING, "ALL");

        register(JacksonFeature.class);
 		log.info("Jersey has been loaded");
 	}
 	
 	public RestConfig(){
 		this(new Binder());
 	}
	 	
 	public static class Binder extends AbstractBinder{

		@Override
		protected void configure() {
		}
 		
 	}
 	
}
