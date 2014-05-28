package es.tododev.auth.server.dto;

import java.util.Map;
import java.util.Set;

public class RolesDTO {

	public final static String OK = "OK";
	public final static String KO = "KO";
	public final static String ERR = "ERROR";
	private String status;
	private Set<String> roles;
	private Map<String,String> properties;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
}
