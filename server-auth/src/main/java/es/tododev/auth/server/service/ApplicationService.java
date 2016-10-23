package es.tododev.auth.server.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.tododev.auth.server.aop.Transactional;
import es.tododev.auth.server.bean.Application;
import es.tododev.auth.server.bean.GroupApplications;
import es.tododev.auth.server.dto.ApplicationDto;
import es.tododev.auth.server.oam.Oam;

public class ApplicationService {

	private final static Logger log = LogManager.getLogger();
	private final Oam oam;
	
	@Inject
	public ApplicationService(Oam oam){
		this.oam = oam;
	}
	
	@Transactional
	public void addApplication(EntityManager em, String appToken, String appId, String password, String url, Long expireMillis, String description){
		GroupApplications group = oam.getApplicationByAppToken(em, appToken).getGroupApplications();
		createApplication(group, appId, password, url, expireMillis, description);
	}
	
	private void validateOperation(GroupApplications group, String appId){
		if(group.getGroupId().equals(appId)){
			throw new IllegalArgumentException(appId+" can not be removed because it is the main application too");
		}else if(!group.getApplications().stream().map(app -> app.getAppId()).collect(Collectors.toList()).contains(appId)){
			throw new IllegalArgumentException(appId+" doesn't exist or doesn't belong to the group id "+group.getGroupId());	
		}
	}
	
	@Transactional
	public void deleteApplication(EntityManager em, String appToken, String appId){
		GroupApplications group = oam.getApplicationByAppToken(em, appToken).getGroupApplications();
		validateOperation(group, appId);
		Application application = em.find(Application.class, appId);
		em.remove(application);
	}
	
	public List<ApplicationDto> getApplications(EntityManager em, String appToken){
		GroupApplications group = oam.getApplicationByAppToken(em, appToken).getGroupApplications();
		List<ApplicationDto> applications = group.getApplications().stream().map(app -> new ApplicationDto(app.getAppId(), app.getUrl(), app.getDescription())).collect(Collectors.toList());
		return applications;
	}
	
	@Transactional
	public void update(EntityManager em, String appToken, ApplicationDto dto){
		GroupApplications group = oam.getApplicationByAppToken(em, appToken).getGroupApplications();
		validateOperation(group, dto.getAppId());
		Application application = em.find(Application.class, dto.getAppId());
		application.setDescription(dto.getDescription());
		application.setUrl(dto.getUrl());
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
