package es.tododev.auth.server.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ContextParams {

	private final Logger log = LoggerFactory.getLogger(getClass());
	public static final String INIT_PARAM_TOKEN_TIME = "sharedDomainTokenExpireMinutes";
	public static final String INIT_PARAM_CROSS_COOKIE_DOMAIN = "crossCookieDomains";
	private final Set<String> crossCookieDomains;
	private final long sharedDomainTokenExpireMillis;
	
	@Inject
	public ContextParams(@Context ServletContext context){
		long minutes = Long.parseLong(context.getInitParameter(INIT_PARAM_TOKEN_TIME));
		sharedDomainTokenExpireMillis = minutes * 60 * 1000;
		String[] domains = context.getInitParameter(INIT_PARAM_CROSS_COOKIE_DOMAIN).split(";");
		crossCookieDomains = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(domains)));
		log.debug("Time to expire shared tokens is {} minutes", minutes);
		log.debug("Cross Cookie Domains: {}", crossCookieDomains);
	}

	public Set<String> getCrossCookieDomains() {
		return crossCookieDomains;
	}

	public long getSharedDomainTokenExpireMillis() {
		return sharedDomainTokenExpireMillis;
	}
	
}
