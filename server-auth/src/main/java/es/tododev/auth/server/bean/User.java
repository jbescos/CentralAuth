package es.tododev.auth.server.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(indexes = {@Index(name="sharedDomainTokenIdx", columnList = "sharedDomainToken")})
public class User implements Serializable{

	@Id
	private String username;
	@Column(nullable = false, length = 500)
	private String password;
	@Column(nullable = false)
	private String sharedDomainToken;
	@Column(nullable = false)
	private Date expireSharedDomainToken;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "user")
    private List<UserRoles> userRoles = new ArrayList<UserRoles>();
	
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
	public List<UserRoles> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(List<UserRoles> userRoles) {
		this.userRoles = userRoles;
	}
	
}
