package com.krishagni.catissueplus.core.audit.services;

import com.krishagni.catissueplus.core.audit.domain.UserApiCallLog;

public interface AuditService {
	
	// Internal APIs
	
	public void insertApiCallLog(UserApiCallLog userAuditLog);
	
	public long getTimeSinceLastApiCall(Long userId, String token);
}
