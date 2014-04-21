
package com.krishagni.catissueplus.core.auth.services;

import com.krishagni.catissueplus.core.auth.events.LoginDetails;

import edu.wustl.auth.exception.AuthenticationException;

public interface CatissueAuthService {

	void authenticateUser(LoginDetails loginDetails);

	void authenticateUserByLdap(LoginDetails loginDetails) throws AuthenticationException;
}
