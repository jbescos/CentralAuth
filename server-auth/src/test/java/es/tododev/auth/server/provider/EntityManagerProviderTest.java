package es.tododev.auth.server.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.junit.Test;

import es.tododev.auth.server.bean.Login;

public class EntityManagerProviderTest {
	
	private final EntityManagerProvider provider = new EntityManagerProvider(Persistence.createEntityManagerFactory("persistenceConfig"));
	
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
			Login login = new Login();
			login.setLoginId(ID);
			login.setPassword("password");
			em.persist(login);
			em.getTransaction().commit();
			
			Login fromDb = em.find(Login.class, ID);
			assertNotNull(fromDb);
			assertEquals(login.getPassword(), fromDb.getPassword());
		}finally{
			em.clear();
			em.close();
		}
	}
	
}
