package com.krishagni.catissueplus.core.audit.services;

import com.krishagni.catissueplus.core.audit.domain.UserApiCallLog;

public interface AuditService {
	
	// Internal API's
	
	public void insertUserApiCallLog(UserApiCallLog userAuditLog);
	
	public long getTimeSinceLastApiCall(Long userId, String token);
}
