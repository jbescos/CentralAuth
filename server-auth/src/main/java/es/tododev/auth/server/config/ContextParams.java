package es.tododev.auth.server.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

@Singleton
public class ContextParams {

	public static final String INIT_PARAM_TOKEN_TIME = "sharedDomainTokenExpireMinutes";
	public static final String INIT_PARAM_CROSS_COOKIE_DOMAIN = "crossCookieDomains";
	private final Set<String> crossCookieDomains;
	private final long sharedDomainTokenExpireMillis;
	
	@Inject
	public ContextParams(@Context ServletContext context){
		long minutes = Long.parseLong((String) context.getAttribute(INIT_PARAM_TOKEN_TIME));
		sharedDomainTokenExpireMillis = minutes * 60 * 1000;
		String[] domains = ((String) context.getAttribute(INIT_PARAM_CROSS_COOKIE_DOMAIN)).split(";");
		crossCookieDomains = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(domains)));
	}

	public Set<String> getCrossCookieDomains() {
		return crossCookieDomains;
	}

	public long getSharedDomainTokenExpireMillis() {
		return sharedDomainTokenExpireMillis;
	}
	
}
