package es.tododev.auth.server.oam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Oam {
	
	private final static Logger log = LogManager.getLogger();
	public final static String APP_TOKEN = "appToken";
	public final static String APP_ID = "appId";
	public final static String GROUP_ID = "groupId";
	public final static String USER_NAME = "username";
	
	public <T> List<T> getByColumns(Map<String,String> columnValue, EntityManager em, Class<T> entity){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = cb.createQuery(entity);
		Root<T> enhancedUser = criteria.from(entity);
		Predicate[] predicates = columnValue.entrySet().stream().map(entry -> cb.equal(enhancedUser.get(entry.getKey()), entry.getValue())).toArray(size -> new Predicate[size]);
		List<T> userApplication = em.createQuery(criteria.select(enhancedUser).where(predicates)).getResultList();
		log.debug(userApplication.size()+" rows obtained from "+entity.getCanonicalName()+" with args "+columnValue);
		return userApplication;
	}
	
	public <T> List<T> getByColumn(String column, String value, EntityManager em, Class<T> entity){
		Map<String,String> columns = new HashMap<>();
		columns.put(column, value);
		return getByColumns(columns, em, entity);
	}
	
}
