package es.tododev.auth.server.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.security.NoSuchAlgorithmException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.commons.DigestGenerator;
import es.tododev.auth.commons.dto.ReqAuthorizationDTO;
import es.tododev.auth.commons.dto.RespAuthorizationDTO;
import es.tododev.auth.server.RestConfig;
import es.tododev.auth.server.provider.ExceptionLogger;
import es.tododev.auth.server.rules.LoggerRule;

public class AuthorizeResourceTest extends JerseyTest {

	private final static Logger log = LogManager.getLogger();
	@Rule
	public final LoggerRule loggerRule = new LoggerRule(getClass());
	
	@Test
	public void authorize(){
		log.info("Testing authorize");
		ReqAuthorizationDTO input = new ReqAuthorizationDTO();
		input.setAppId("appId1");
		input.setRole("admin");
		input.setAppToken("totoken");
		RespAuthorizationDTO output = doPost(RespAuthorizationDTO.class, input, Constants.AUTHORIZE_PATH, Status.OK);
		assertNotNull(output);
		assertNotNull(output.getSign());
	}
	
	@Test
	public void canInstanceDigestGenerator() throws NoSuchAlgorithmException{
		new DigestGenerator();
	}
	
	private <OUT, IN> OUT doPost(Class<OUT> output, IN input, String path, Status expectedStatus){
		Entity<IN> in = Entity.entity(input, MediaType.APPLICATION_JSON);
		Response response = target(path).request().post(in);
		assertEquals(expectedStatus.getStatusCode(), response.getStatus());
		return response.readEntity(output);
	}
	
	@Override
	protected Application configure() {
		super.enable(TestProperties.LOG_TRAFFIC);
		MockitoAnnotations.initMocks(this);
		return new RestConfig(new RestConfig.Binder(), new AbstractBinder() {
			@Override
			protected void configure() {
				
			}
		});
	}

	@Override
	protected void configureClient(ClientConfig config) {
		super.configureClient(config);
		config.property(ServerProperties.TRACING, "ALL");
		config.register(ExceptionLogger.class);
		config.register(JacksonFeature.class);
	}
}
