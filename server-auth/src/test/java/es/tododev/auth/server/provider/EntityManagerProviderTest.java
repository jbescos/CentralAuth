package es.tododev.auth.server.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.Test;

import es.tododev.auth.server.bean.User;

public class EntityManagerProviderTest {
	
	private final EmFactoryProvider provider = new EmFactoryProvider(Persistence.createEntityManagerFactory("persistenceConfig"));
	
	@Test
	public void entityManagerNotNull(){
		assertNotNull(provider.provide());
	}
	
	@Test
	public void insert(){
		final String ID = "login id";
		EntityManager em = provider.provide();
		try{
			em.getTransaction().begin();
			User login = new User();
			login.setUsername(ID);
			login.setPassword("password");
			em.persist(login);
			em.getTransaction().commit();
			
			User fromDb = em.find(User.class, ID);
			assertNotNull(fromDb);
			assertEquals(login.getPassword(), fromDb.getPassword());
		}finally{
			em.clear();
			em.close();
		}
	}
	
}
