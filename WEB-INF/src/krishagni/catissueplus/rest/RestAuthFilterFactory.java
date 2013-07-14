/**
 * 
 */
package krishagni.catissueplus.rest;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.DatatypeConverter;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

import edu.wustl.auth.exception.AuthenticationException;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.exception.CatissueException;
import edu.wustl.catissuecore.processor.CatissueLoginProcessor;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.domain.LoginCredentials;
import edu.wustl.domain.LoginResult;
import edu.wustl.processor.LoginProcessor;
import edu.wustl.security.privilege.PrivilegeManager;

public class RestAuthFilterFactory implements ResourceFilterFactory {
	
	private static final Logger logger = Logger
			.getCommonLogger(RestAuthFilterFactory.class);

	@Context
	private UriInfo uriInfo;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List create(AbstractMethod method) {
		return Collections.singletonList((ResourceFilter) new Filter());
	}

	private class Filter implements ResourceFilter, ContainerRequestFilter {

		private static final String BASIC_AUTH = "Basic ";

		protected Filter() {
		}

		public ContainerRequestFilter getRequestFilter() {
			return this;
		}

		public ContainerResponseFilter getResponseFilter() {
			return null;
		}

		public ContainerRequest filter(ContainerRequest request) {
			long t1 = 0, t2 = 0;
			try {
				t1 = System.currentTimeMillis();
				logger.debug("Url invoked is " + uriInfo.getPath());
				String authHeader = request.getHeaderValue("Authorization");
				if (authHeader != null && authHeader.startsWith(BASIC_AUTH)) {
					logger.debug("Authorization given:" + authHeader);
					String authCredentials = authHeader.substring(BASIC_AUTH.length());
					String[] creds = new String(DatatypeConverter.parseBase64Binary(authCredentials)).split(":");
					String user = creds[0], password = creds[1];
					//logger.debug(user + ":" + password);
					try {
						boolean isAuthenticated = authenticate(user, password);
						if (isAuthenticated) {
							logger.debug(user + ": You're authorized, Sir.");
							return request;
						} else {
							logger.debug(user + ": You're not authorized");
							throw new WebApplicationException(Response.Status.UNAUTHORIZED);
						}
					} catch (AuthenticationException authenticationException) {
						logger.error("Authentication exception for " + user, authenticationException);
						throw new WebApplicationException(Response.Status.UNAUTHORIZED);
					} catch (ApplicationException applicationException) {
						logger.error("Application exception for " + applicationException.getMessage());
						throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
					} catch (CatissueException catissueException) {
						System.out.println(user + ": caTissue exception: " + catissueException.getMessage());
						throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
					}
				}
				System.out.println("No authorization header in request");			
				throw new WebApplicationException(Response.Status.UNAUTHORIZED);
			} finally {
				t2 = System.currentTimeMillis();
				System.out.println("Time taken: " + (t2 - t1) + " ms");
			}
		}

		private boolean authenticate(String user, String password) throws AuthenticationException, ApplicationException, CatissueException {
			LoginCredentials loginCredentials = new LoginCredentials();

			loginCredentials.setLoginName(user);
			loginCredentials.setPassword(password);
			
			LoginResult loginResult = LoginProcessor.processUserLogin(loginCredentials);
			if (loginResult.isAuthenticationSuccess()) {
				User validUser = CatissueLoginProcessor.getUser(loginResult.getAppLoginName());
				if (validUser != null)
					PrivilegeManager.getInstance().getPrivilegeCache(validUser.getLoginName());
				return true;
			}

			return false;
		}
	}
}