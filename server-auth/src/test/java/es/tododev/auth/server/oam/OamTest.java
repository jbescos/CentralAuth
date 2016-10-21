package es.tododev.auth.server.oam;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import es.tododev.auth.server.bean.UserApplication;

public class OamTest {

	private final static Logger log = LogManager.getLogger();
	public final static EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceConfig");
	
	@Test
	public void getByColumn(){
		EntityManager em = emf.createEntityManager();
		String appToken = "totooken";
		em.getTransaction().begin();
		try{
			UserApplication userApp = new UserApplication();
			userApp.setAppId("appId");
			userApp.setAppToken(appToken);
			userApp.setExpireDateToken(new Date());
			userApp.setUsername("username");
			em.persist(userApp);
			em.getTransaction().commit();
			Oam oam = new Oam();
			UserApplication user = oam.getByColumn(Oam.APP_TOKEN, appToken, em, UserApplication.class).stream().findFirst().get();
			Assert.assertNotNull(user);
		}catch(Exception e){
			em.getTransaction().rollback();
			log.error("Persist exception", e);
		}finally{
			em.clear();
			em.close();
		}
		
	}
	
}
