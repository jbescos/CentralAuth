package es.tododev.auth.server.oam;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import es.tododev.auth.server.bean.User;

public class Oam {
	
	public List<User> getUserBySharedDomainToken(String sharedDomainToken, EntityManager em){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> criteria = cb.createQuery(User.class);
		Root<User> enhancedUser = criteria.from(User.class);
		Predicate predicate = cb.equal(enhancedUser.get("sharedDomainToken"), sharedDomainToken);
		List<User> users = em.createQuery(criteria.select(enhancedUser).where(predicate)).getResultList();
		return users;
	}
	
}
