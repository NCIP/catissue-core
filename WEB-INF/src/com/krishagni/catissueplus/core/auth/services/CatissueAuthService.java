package com.krishagni.catissueplus.core.auth.services;

import com.krishagni.catissueplus.core.auth.events.LoginDetails;


public interface CatissueAuthService {

	void authenticateUser(LoginDetails loginDetails);
}
