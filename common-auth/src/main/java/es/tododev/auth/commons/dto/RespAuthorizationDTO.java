package es.tododev.auth.commons.dto;

public class RespAuthorizationDTO {

	private String sign;
	private String username;
	private String newCookie;
	
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNewCookie() {
		return newCookie;
	}
	public void setNewCookie(String newCookie) {
		this.newCookie = newCookie;
	}
	
}
