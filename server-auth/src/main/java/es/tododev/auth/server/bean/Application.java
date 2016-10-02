package es.tododev.auth.server.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@SuppressWarnings("serial")
@Entity
public class Application implements Serializable{

	@Id
	private String appId;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String url;
	@Column(nullable = false)
	private long expireMillisToken = 1000*60*30;
	private String description;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "application")
    private List<UserRoles> userRoles = new ArrayList<UserRoles>();
	@ManyToOne
	private GroupApplications groupApplications;
	
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
	public List<UserRoles> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(List<UserRoles> userRoles) {
		this.userRoles = userRoles;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public GroupApplications getGroupApplications() {
		return groupApplications;
	}
	public void setGroupApplications(GroupApplications groupApplications) {
		this.groupApplications = groupApplications;
	}
	public long getExpireMillisToken() {
		return expireMillisToken;
	}
	public void setExpireMillisToken(long expireMillisToken) {
		this.expireMillisToken = expireMillisToken;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
