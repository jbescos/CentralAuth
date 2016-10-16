package es.tododev.auth.commons;

public class Constants {

	// FIXME Obtain it dynamically
	public static final String APP_URL = "http://localhost:8080/server-auth";
	// Role to allow create applications, roles and to assign roles to the users
	public static final String MAIN_APP = "main";
	public static final String ADMIN_ROLE = "admin";
	public static final String USER_ROLE = "user";
	public static final String USER_NAME_KEY = "username";
	// Unique cookie per application with the value of one unique string. This string identifies the user.
	public static final String APP_COOKIE = "appCookie";
	public static final String AUTHORIZE_REST_PATH = "rest/authorize";
	public static final String AUTHORIZE_PATH = "/authorize";
	public static final String LOGIN_CALLBACK_PATH = "/rest/auth/"+USER_ROLE+"/cookiemgr/login";
	public static final String LOGOUT_CALLBACK_PATH = "/rest/auth/"+USER_ROLE+"/cookiemgr/logout";
	public static final String ROLES_PATH = "/{groupId}/auth/"+ADMIN_ROLE+"/roles";
	public static final String APPLICATION_PATH = "/{groupId}/auth/"+ADMIN_ROLE+"/application";
	public static final String LOGIN_RESOURCE = "/account";
	
	public static String getMainAPP(String userName){
		return Constants.MAIN_APP+"_"+userName;
	}
	
}
