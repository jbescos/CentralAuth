package es.tododev.auth.server.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.tododev.auth.server.bean.Application;
import es.tododev.auth.server.bean.GroupApplications;
import es.tododev.auth.server.dto.ApplicationDto;
import es.tododev.auth.server.oam.Oam;

public class ApplicationService {

	private final static Logger log = LogManager.getLogger();
	private final EntityManager em;
	
	@Inject
	public ApplicationService(EntityManager em){
		this.em = em;
	}
	
	public void addApplication(String appToken, String appId, String password, String url, Long expireMillis, String description){
		em.getTransaction().begin();
		try{
			Oam oam = new Oam();
			GroupApplications group = oam.getApplicationByAppToken(em, appToken).getGroupApplications();
			createApplication(group, appId, password, url, expireMillis, description);
			em.getTransaction().commit();
		}catch(Exception e){
			em.getTransaction().rollback();
			log.error("Persist exception", e);
		}
	}
	
	private void validateOperation(GroupApplications group, String appId){
		if(group.getGroupId().equals(appId)){
			throw new IllegalArgumentException(appId+" can not be removed because it is the main application too");
		}else if(!group.getApplications().stream().map(app -> app.getAppId()).collect(Collectors.toList()).contains(appId)){
			throw new IllegalArgumentException(appId+" doesn't exist or doesn't belong to the group id "+group.getGroupId());	
		}
	}
	
	public void deleteApplication(String appToken, String appId){
		Oam oam = new Oam();
		GroupApplications group = oam.getApplicationByAppToken(em, appToken).getGroupApplications();
		validateOperation(group, appId);
		em.getTransaction().begin();
		try{
			Application application = em.find(Application.class, appId);
			em.remove(application);
			em.getTransaction().commit();
		}catch(Exception e){
			em.getTransaction().rollback();
			log.error("Persist exception", e);
		}
	}
	
	public List<ApplicationDto> getApplications(String appToken){
		Oam oam = new Oam();
		GroupApplications group = oam.getApplicationByAppToken(em, appToken).getGroupApplications();
		List<ApplicationDto> applications = group.getApplications().stream().map(app -> new ApplicationDto(app.getAppId(), app.getUrl(), app.getDescription())).collect(Collectors.toList());
		return applications;
	}
	
	public void update(String appToken, ApplicationDto dto){
		Oam oam = new Oam();
		GroupApplications group = oam.getApplicationByAppToken(em, appToken).getGroupApplications();
		validateOperation(group, dto.getAppId());
		em.getTransaction().begin();
		try{
			Application application = em.find(Application.class, dto.getAppId());
			application.setDescription(dto.getDescription());
			application.setUrl(dto.getUrl());
			em.getTransaction().commit();
		}catch(Exception e){
			em.getTransaction().rollback();
			log.error("Persist exception", e);
		}
	}
	
	public Application createApplication(GroupApplications group, String appId, String password, String url, Long expireMillis, String description){
		Application application = new Application();
		application.setAppId(appId);
		application.setPassword(password);
		application.setExpireMillisToken(expireMillis);
		application.setUrl(url);
		application.setDescription(description);
		application.setGroupApplications(group);
		group.getApplications().add(application);
		return application;
	}
	
}
