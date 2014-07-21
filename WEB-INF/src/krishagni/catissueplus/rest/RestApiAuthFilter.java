/**
 * 
 */

package krishagni.catissueplus.rest;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.xml.bind.DatatypeConverter;

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.exception.CatissueException;
import edu.wustl.catissuecore.processor.CatissueLoginProcessor;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.domain.LoginResult;
import edu.wustl.processor.LoginProcessor;
import edu.wustl.security.global.Roles;
import edu.wustl.security.privilege.PrivilegeManager;

/**
 * @author Ali Ranalvi
 *
 */
public class RestApiAuthFilter implements Filter
{

	private static final Logger logger = Logger
			.getCommonLogger(RestApiAuthFilter.class);

	private static final String BASIC_AUTH = "Basic ";

	@Override
	public void destroy()
	{
		// do nothing;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpServletResponse httpResp = (HttpServletResponse) resp;

		if (httpReq.getSession(false) == null ||
				httpReq.getSession(false).getAttribute(Constants.SESSION_DATA) == null)
		{
			String authInfo = httpReq.getHeader(HttpHeaders.AUTHORIZATION);
			if (authInfo == null)
			{
				sendRequireAuthResp(httpResp);
				return;
			}

			if (authInfo != null && !authInfo.startsWith(BASIC_AUTH))
			{
				sendRequireAuthResp(httpResp);
				return;
			}

			String authCredentials = authInfo.substring(BASIC_AUTH.length());
			String[] creds = new String(
					DatatypeConverter.parseBase64Binary(authCredentials))
					.split(":");

			if (creds.length != 2)
			{
				sendRequireAuthResp(httpResp);
				return;
			}

			String user = creds[0], password = creds[1];
			try
			{
				User authenticatedUser = authenticate(httpReq, user, password);
				if (authenticatedUser == null)
				{
					logger.debug(user + ": You're not authorized");
					sendRequireAuthResp(httpResp);
					return;
				}
				SessionDataBean sessionDataBean = new SessionDataBean();

				//			httpReq.setAttribute(Constants.CATISSUE_USER_OBJ, authenticatedUser);
				sessionDataBean = setSessionDataBean(authenticatedUser,
						httpReq.getRemoteAddr(), authenticatedUser.getRoleId());
				httpReq.getSession().setAttribute(Constants.SESSION_DATA,
						sessionDataBean);

			}
			catch (AuthenticationException authenticationException)
			{
				logger.error("Authentication exception for " + user,
						authenticationException);
				sendRequireAuthResp(httpResp);
				return;
			}
			catch (ApplicationException applicationException)
			{
				logger.error("Application exception for "
						+ applicationException.getMessage());
				sendInternalErrorResp(httpResp);
				return;
			}
			catch (CatissueException catissueException)
			{
				System.out.println(user + ": caTissue exception: "
						+ catissueException.getMessage());
				sendInternalErrorResp(httpResp);
				return;
			}
		}

		chain.doFilter(req, resp);
	}

	private SessionDataBean setSessionDataBean(final User validUser,
			final String ipAddress, final String userRole)
			throws CatissueException
	{
		final SessionDataBean sessionData = new SessionDataBean();
		sessionData.setAdmin(isAdminUser(validUser.getRoleId()));
		sessionData.setUserName(validUser.getLoginName());
		sessionData.setIpAddress(ipAddress);
		sessionData.setUserId(validUser.getId());
		sessionData.setFirstName(validUser.getFirstName());
		sessionData.setLastName(validUser.getLastName());
		sessionData.setCsmUserId(validUser.getCsmUserId().toString());

		setSecurityParamsInSessionData(sessionData, userRole);
		return sessionData;
	}

	private void setSecurityParamsInSessionData(
			final SessionDataBean sessionData, final String userRole)
			throws CatissueException
	{
		if (userRole != null
				&& (userRole.equalsIgnoreCase(Roles.ADMINISTRATOR) || userRole
						.equals(Roles.SUPERVISOR)))
		{
			sessionData.setSecurityRequired(false);
		}
		else
		{
			sessionData.setSecurityRequired(true);
		}
	}

	private boolean isAdminUser(final String userRole)
	{
		boolean adminUser;
		if (userRole.equalsIgnoreCase(Constants.ADMIN_USER))
		{
			adminUser = true;
		}
		else
		{
			adminUser = false;
		}
		return adminUser;
	}

	@Override
	public void init(FilterConfig config) throws ServletException
	{
	}

	private void sendRequireAuthResp(HttpServletResponse httpResp)
			throws IOException
	{
		httpResp.setHeader(HttpHeaders.WWW_AUTHENTICATE,
				"Basic realm=\"OpenSpecimen\"");
		httpResp.sendError(HttpServletResponse.SC_UNAUTHORIZED,
				"You must supply valid credentials to access the caTissue Plus REST API");
	}

	private void sendInternalErrorResp(HttpServletResponse httpResp)
			throws IOException
	{
		httpResp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}

	private User authenticate(HttpServletRequest request, String user, String password)
			throws AuthenticationException, ApplicationException,
			CatissueException
	{
		LoginCredentials loginCredentials = new LoginCredentials();

		loginCredentials.setLoginName(user);
		loginCredentials.setPassword(password);
		
		LoginResult loginResult = CatissueLoginProcessor.processUserLogin(request, loginCredentials);
		if (loginResult.isAuthenticationSuccess())
		{
			User validUser = CatissueLoginProcessor.getUser(loginResult
					.getAppLoginName());
			if (validUser != null)
				PrivilegeManager.getInstance().getPrivilegeCache(
						validUser.getLoginName());
			return validUser;
		}

		return null;
	}
}
