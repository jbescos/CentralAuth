package es.tododev.auth.server.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@SuppressWarnings("serial")
@Entity
public class User implements Serializable{

	@Id
	private String username;
	@Column(nullable = false, length = 500)
	private String password;
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
	public List<UserRoles> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(List<UserRoles> userRoles) {
		this.userRoles = userRoles;
	}
	
}
