package com.krishagni.catissueplus.core.auth.services;

import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.auth.events.LoginDetail;
import com.krishagni.catissueplus.core.auth.events.TokenDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public interface UserAuthenticationService {	
	public ResponseEvent<Map<String, Object>> authenticateUser(RequestEvent<LoginDetail> req);
	
	public ResponseEvent<User> validateToken(RequestEvent<TokenDetail> req);
	
	public ResponseEvent<UserSummary> getCurrentLoggedInUser();
	
	public ResponseEvent<String> removeToken(RequestEvent<String> req);
}