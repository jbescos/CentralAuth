package es.tododev.auth.server.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
public class Application implements Serializable{

	@Id
	private String appId;
	@Column(nullable = false)
	private String password;
	@ManyToOne
	private UserRoles appsRoles;
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public UserRoles getAppsRoles() {
		return appsRoles;
	}
	public void setAppsRoles(UserRoles appsRoles) {
		this.appsRoles = appsRoles;
	}
	
	
}
