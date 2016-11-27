package es.tododev.auth.server.provider;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class AllowCrossDomain implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
		// FIXME check if it will work in other domains
		response.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:8081");
		response.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, *");
		response.getHeaders().add("Access-Control-Allow-Credentials", "true");
		response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
	}

}
