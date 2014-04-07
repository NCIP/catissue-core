package com.krishagni.catissueplus.core.auth.services;

import com.krishagni.catissueplus.core.auth.events.AuthenticateUserEvent;
import com.krishagni.catissueplus.core.auth.events.UserAuthenticatedEvent;

public interface UserAuthenticationService {
	
	UserAuthenticatedEvent authenticateUser(AuthenticateUserEvent event);

}
