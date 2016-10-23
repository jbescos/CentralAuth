package es.tododev.auth.server.dto;

public class ApplicationDto {

	private String appId;
	private String url;
	private String description;
	
	public ApplicationDto() {
		super();
	}
	
	public ApplicationDto(String appId, String url, String description) {
		super();
		this.appId = appId;
		this.url = url;
		this.description = description;
	}
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
