package es.tododev.auth.sample.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/sample")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class DummyResource {
	
	private final static Logger log = LogManager.getLogger();
	
	@GET
	public String noAuthenticaiton(){
		String phrase = "No authentication is needed";
		log.info(phrase);
		return phrase;
	}
	
	@GET
	@Path("/auth/user")
	public String youHaveTheRole(){
		String phrase = "You have the role";
		log.info(phrase);
		return phrase;
	}
	
	@GET
	@Path("/auth/admin")
	public String admin(){
		String phrase = "Reachable for admin only";
		log.info(phrase);
		return phrase;
	}

}
