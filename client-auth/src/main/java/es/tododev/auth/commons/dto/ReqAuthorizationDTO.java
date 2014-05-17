package es.tododev.auth.commons.dto;

public class ReqAuthorizationDTO {

	private String appId;
	private String sharedDomainToken;
	private String role;
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getSharedDomainToken() {
		return sharedDomainToken;
	}
	public void setSharedDomainToken(String sharedDomainToken) {
		this.sharedDomainToken = sharedDomainToken;
	}
	
}
