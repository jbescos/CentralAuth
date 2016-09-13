package es.tododev.auth.server.provider;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class ExceptionLogger implements ExceptionMapper<Exception>{

	private final static Logger log = LogManager.getLogger();

	@Override
	public Response toResponse(Exception paramE) {
		log.error("Unexpected error", paramE);
		return Response.status(404).build();
	}
	
}
