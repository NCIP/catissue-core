package com.krishagni.catissueplus.core.auth.services;

import com.krishagni.catissueplus.core.auth.events.LoginDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface UserAuthenticationService {	
	ResponseEvent<Boolean> authenticateUser(RequestEvent<LoginDetail> req);
}
