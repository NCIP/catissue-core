
package com.krishagni.catissueplus.core.auth.services.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import com.krishagni.catissueplus.core.auth.domain.AuthErrorCode;
import com.krishagni.catissueplus.core.auth.services.AuthenticationService;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

@Configurable
public class OpenSpecimenAuthServiceImpl implements AuthenticationService {
	
	@Autowired
	private AuthenticationManager authManager;
	
	public OpenSpecimenAuthServiceImpl(Map<String, String> props) {
		
	}

	@Override
	public void authenticate(String username, String password) {
		try{
			UsernamePasswordAuthenticationToken authenticationToken =
				new UsernamePasswordAuthenticationToken(username, password);
		
			authManager.authenticate(authenticationToken);
		} catch(AuthenticationException e) {
			throw OpenSpecimenException.userError(AuthErrorCode.INVALID_CREDENTIALS);
		}
	}

}
