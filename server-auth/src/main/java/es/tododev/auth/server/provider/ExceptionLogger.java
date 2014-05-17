package es.tododev.auth.server.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class ExceptionLogger implements ExceptionMapper<Exception>{

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public Response toResponse(Exception paramE) {
		log.error("Unexpected error", paramE);
		return Response.status(404).build();
	}
	
}
