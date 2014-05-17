package es.tododev.auth.server.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(indexes = {@Index(name="sharedDomainTokenIdx", columnList = "sharedDomainToken")})
public class User implements Serializable{

	@Id
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String sharedDomainToken;
	@Column(nullable = false)
	private Date expireSharedDomainToken;
	@ManyToOne
	private UserRoles userRoles;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public UserRoles getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(UserRoles userRoles) {
		this.userRoles = userRoles;
	}
	public String getSharedDomainToken() {
		return sharedDomainToken;
	}
	public void setSharedDomainToken(String sharedDomainToken) {
		this.sharedDomainToken = sharedDomainToken;
	}
	public Date getExpireSharedDomainToken() {
		return expireSharedDomainToken;
	}
	public void setExpireSharedDomainToken(Date expireSharedDomainToken) {
		this.expireSharedDomainToken = expireSharedDomainToken;
	}
	
}
