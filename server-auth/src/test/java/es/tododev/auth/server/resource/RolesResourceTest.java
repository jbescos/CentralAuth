package es.tododev.auth.server.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.internal.util.collection.MultivaluedStringMap;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.tododev.auth.commons.Constants;
import es.tododev.auth.commons.CookieManager;
import es.tododev.auth.commons.DigestGenerator;
import es.tododev.auth.server.RestConfig;
import es.tododev.auth.server.dto.RolesDTO;
import es.tododev.auth.server.oam.Oam;
import es.tododev.auth.server.provider.EmFactoryProvider;
import es.tododev.auth.server.provider.ExceptionLogger;
import es.tododev.auth.server.provider.UUIDgenerator;
import es.tododev.auth.server.rules.LoggerRule;
import es.tododev.auth.server.service.ApplicationService;
import es.tododev.auth.server.service.AuthorizeService;
import es.tododev.auth.server.service.LoginService;
import es.tododev.auth.server.service.RolesService;

public class RolesResourceTest extends JerseyTest {

	@Rule
	public final LoggerRule loggerRule = new LoggerRule(getClass());
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;;
	@Mock
	private UUIDgenerator uuid;
	
	private final String USER_NAME = "username";
	private final String PASSWORD = "userpassword";
	private final String SHARED_DOMAIN_TOKEN = "totoken";
	private final String APP_ID = "appId";
	private final String ROLE = "admin";
	private final Set<String> ROLES = new LinkedHashSet<String>(Arrays.asList(ROLE));
	
	@Test
	public void dummy(){
		
	}
	
	@Test
	@Ignore // TODO
	public void getAllRoles(){
		addApp();
		addUserToApp();
		register();
		
		Response response = target(Constants.ROLES_PATH).queryParam("token", SHARED_DOMAIN_TOKEN).queryParam("appId", APP_ID).request().get();
		assertEquals(Status.OK, response.getStatus());
		RolesDTO dto = response.readEntity(RolesDTO.class);
		assertNotNull(dto);
		assertEquals(RolesDTO.OK, dto.getStatus());
		assertEquals(ROLES, dto.getRoles());
		assertTrue(dto.getProperties().isEmpty());
	}
	
	private void addUserToApp(){
		MultivaluedMap<String, String> formParams = new MultivaluedStringMap();
		formParams.add(Constants.USER_NAME_KEY, USER_NAME);
		formParams.add("appId", APP_ID);
		formParams.add("role", ROLE);
		Entity<MultivaluedMap<String, String>> form = Entity.entity(formParams, MediaType.APPLICATION_FORM_URLENCODED);
		Response response = target(Constants.ROLES_PATH).request().post(form);
		assertEquals(Status.OK, response.getStatus());
	}
	
	private void addApp(){
		MultivaluedMap<String, String> formParams = new MultivaluedStringMap();
		formParams.add("appId", APP_ID);
		formParams.add("password", "pass app");
		Entity<MultivaluedMap<String, String>> form = Entity.entity(formParams, MediaType.APPLICATION_FORM_URLENCODED);
		Response response = target(Constants.APPLICATION_PATH).request().post(form);
		assertEquals(Status.OK, response.getStatus());
	}
	
	private void register(){
		MultivaluedMap<String, String> formParams = new MultivaluedStringMap();
		formParams.add("username", USER_NAME);
		formParams.add("password1", PASSWORD);
		formParams.add("password2", PASSWORD);
		Entity<MultivaluedMap<String, String>> form = Entity.entity(formParams, MediaType.APPLICATION_FORM_URLENCODED);
		Response response = target(Constants.LOGIN_RESOURCE+"/register").request().post(form);
		assertEquals(Status.OK, response.getStatus());
	}
	
	@Override
	protected Application configure() {
		super.enable(TestProperties.LOG_TRAFFIC);
		MockitoAnnotations.initMocks(this);
		RequestDispatcher dispatcher = mock(RequestDispatcher.class);
		when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
		return new RestConfig(new AbstractBinder() {
			
			@Override
			protected void configure() {
				bind(Persistence.createEntityManagerFactory("persistenceConfig")).to(EntityManagerFactory.class);
				bind(AuthorizeService.class).to(AuthorizeService.class);
				bind(DigestGenerator.class).to(DigestGenerator.class).in(Singleton.class);
				bindFactory(EmFactoryProvider.class).to(EntityManager.class);
				bind(LoginService.class).to(LoginService.class);
				bind(Oam.class).to(Oam.class);
				bind(RolesService.class).to(RolesService.class);
				bind(ApplicationService.class).to(ApplicationService.class);
				bind(CookieManager.class).to(CookieManager.class);
				
				bind(request).to(HttpServletRequest.class);
				bind(response).to(HttpServletResponse.class);
				bind(uuid).to(UUIDgenerator.class);
			}
		});
	}

	@Override
	protected void configureClient(ClientConfig config) {
		super.configureClient(config);
		config.property(ServerProperties.TRACING, "ALL");
		config.register(new LoggingFilter(java.util.logging.Logger.getLogger(LoggingFilter.class.getName()), true));
		config.register(ExceptionLogger.class);
		config.register(JacksonFeature.class);
	}
	
}
