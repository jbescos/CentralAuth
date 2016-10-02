package es.tododev.auth.commons.dto;

public class RespAuthorizationDTO {

	private String sign;
	private String username;
	
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
	
}
