package edu.wustl.catissuecore.cacore;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.dao.AbstractUserDetailsAuthenticationProvider;
import org.acegisecurity.userdetails.User;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;

import edu.wustl.catissuecore.client.CaCoreAppServicesDelegator;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;
import gov.nih.nci.system.dao.DAOException;
import gov.nih.nci.system.security.SecurityConstants;

public class CaTissueCSMAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
	/**
	 * Logger object for the class.
	 */
	private static final Logger logger = Logger .getLogger(CaTissueCSMAuthenticationProvider.class.getName());

	private final CaCoreAppServicesDelegator appService = new CaCoreAppServicesDelegator();

	protected CaCoreAppServicesDelegator getApplicationServiceDelegator() {
		return appService;
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		logger.info("Authenticating the user");
		try {
			boolean isAuthentic = getApplicationServiceDelegator().authenticate(userDetails.getUsername(), userDetails.getPassword());
			if(!isAuthentic)
			{
				throw new DAOException("Invalid login credentials for user " + userDetails.getUsername());
			}
		} catch (DAOException e) {
			logger.error("Error occured while authentication the user", e);
			throw new BadCredentialsException(e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Error occured while authentication the user", e);
			throw new BadCredentialsException(e.getMessage(), e);
		}

	}

	@Override
	protected UserDetails retrieveUser(String userName, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		UserDetails userDetails = null;
		try {
			logger.info("Inside method retrieveUser of class CaTissueCSMAuthenticationProvider");
			edu.wustl.catissuecore.domain.User user = AppUtility.getUser(userName);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			authentication.setDetails(userName);
			userDetails = new User(userName, authentication.getCredentials().toString(), true, true, true, true,getDummyGrantedAuthorities());
		} catch (ApplicationException e) {
			logger.error(e);
			throw new UsernameNotFoundException(e.getMessage(), e);
		}
		return userDetails;
	}

	private GrantedAuthority[] getDummyGrantedAuthorities() {
		GrantedAuthority[] grantedAuthorities = new GrantedAuthority[1];
		GrantedAuthority dummyGrantedAuthority = new GrantedAuthorityImpl(SecurityConstants.DUMMY_ROLE);
		grantedAuthorities[0] = dummyGrantedAuthority;
		return grantedAuthorities;
	}

}
