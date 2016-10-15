package es.tododev.auth.commons;

public class Constants {

	// Role to allow create applications, roles and to assign roles to the users
	public static final String MAIN_APP = "main";
	public static final String ADMIN_ROLE = "admin";
	public static final String USER_NAME_KEY = "username";
	// Unique cookie per appliation with the value of one unique string. This string identifies the user.
	public static final String APP_COOKIE = "appCookie";
	public static final String AUTHORIZE_REST_PATH = "rest/authorize";
	public static final String AUTHORIZE_PATH = "/authorize";
	public static final String LOGIN_PATH = "/auth/user/cookiemgr/login";
	public static final String LOGOUT_PATH = "/auth/user/cookiemgr/logout";
	public static final String GROUP_APPLICATIONS_PATH = "/auth/user/groupApplications";
	public static final String ROLES_PATH = "/{groupId}/auth/"+ADMIN_ROLE+"/roles";
	public static final String APPLICATION_PATH = "/{groupId}/auth/"+ADMIN_ROLE+"/application";
	public static final String BOOTSTRAP = "/bootstrap";
	
}
