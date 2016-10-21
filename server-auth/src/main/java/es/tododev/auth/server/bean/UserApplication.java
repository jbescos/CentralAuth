package es.tododev.auth.server.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@IdClass(UserApplication.PK.class)
@Table(indexes = {@Index(name="appTokenIdx", columnList = "appToken")})
public class UserApplication implements Serializable {

	public final static String GET_BY_APP_TOKEN = "GET_BY_APP_TOKEN";
	@Id
	private String username;
	@Id
	private String appId;
	@Column(nullable = false)
	private String groupId;
	@Column(nullable = false)
	private String appToken;
	@Column(nullable = false)
	private Date expireDateToken;
	
	public Date getExpireDateToken() {
		return expireDateToken;
	}
	public void setExpireDateToken(Date expireDateToken) {
		this.expireDateToken = expireDateToken;
	}
	public String getAppToken() {
		return appToken;
	}
	public void setAppToken(String appToken) {
		this.appToken = appToken;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public static class PK implements Serializable {
		private String username;
		private String appId;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getAppId() {
			return appId;
		}
		public void setAppId(String appId) {
			this.appId = appId;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((appId == null) ? 0 : appId.hashCode());
			result = prime * result + ((username == null) ? 0 : username.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PK other = (PK) obj;
			if (appId == null) {
				if (other.appId != null)
					return false;
			} else if (!appId.equals(other.appId))
				return false;
			if (username == null) {
				if (other.username != null)
					return false;
			} else if (!username.equals(other.username))
				return false;
			return true;
		}
	}
	
}
