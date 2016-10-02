package es.tododev.auth.client;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class AuthoriationXmlFilter extends AuthorizationFilter {

	private static final String APP_ID = "AppId";
	private static final String APP_PASSWORD = "AppPassword";
	private IAppProvider appProvider;
	
	@Override
	protected IAppProvider getProvider(HttpServletRequest request) {
		return appProvider;
	}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		super.init(arg0);
		String appId = checkAndGet(APP_ID, arg0);
		String appPassword = checkAndGet(APP_PASSWORD, arg0);
		this.appProvider = new IAppProvider() {
			@Override
			public String getAppPassword() {
				return appPassword;
			}
			@Override
			public String getAppId() {
				return appId;
			}
		};
	}

}
