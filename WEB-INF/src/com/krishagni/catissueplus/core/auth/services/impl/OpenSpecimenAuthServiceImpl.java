
package com.krishagni.catissueplus.core.auth.services.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import com.krishagni.catissueplus.core.auth.domain.AuthErrorCode;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class OpenSpecimenAuthServiceImpl extends AbstractAuthProvider {

	@Override
	public void authenticate(String username, String password) {
		try{
			AuthenticationManager authManager = getAuthManager();
			UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(username, password);
		
			authManager.authenticate(authenticationToken);
		} catch(AuthenticationException e) {
			throw OpenSpecimenException.userError(AuthErrorCode.INVALID_CREDENTIALS);
		}
	}
	
	private AuthenticationManager getAuthManager() {
		return (AuthenticationManager) OpenSpecimenAppCtxProvider.getAppCtx().getBean("authenticationManager");
	}
}
