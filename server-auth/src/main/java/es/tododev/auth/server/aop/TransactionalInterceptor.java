package es.tododev.auth.server.aop;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TransactionalInterceptor implements MethodInterceptor {

	private final static Logger log = LogManager.getLogger();
	
	@Override
	public Object invoke(MethodInvocation arg0) throws Throwable {
		EntityManager em = null;
		Object[] args = arg0.getArguments();
		for(Object arg : args){
			if(arg instanceof EntityManager){
				em = (EntityManager)arg;
				break;
			}
		}
		if(em != null){
			int hashCode = em.hashCode();
			log.debug("Open transaction in EntityManager "+hashCode);
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			try{
				Object result = arg0.proceed();
				tx.commit();
				log.debug("Commit transaction in EntityManager "+hashCode);
				return result;
			}catch(Throwable e){
				log.error("Rolling back transaction in EntityManager "+hashCode, e);
				tx.rollback();
				throw e;
			}
		}else{
			log.warn("No EntityManager found in the arguments of the method "+arg0.getMethod().getName()+". This operation will not be transactional.");
			return arg0.proceed();
		}	
	}

}
