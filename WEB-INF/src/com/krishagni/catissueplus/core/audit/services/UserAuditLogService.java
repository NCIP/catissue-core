package com.krishagni.catissueplus.core.audit.services;

import java.util.Date;

import com.krishagni.catissueplus.core.audit.domain.UserAuditLog;

public interface UserAuditLogService {
	
	// Internal API's
	
	public void insertUserAuditLog(UserAuditLog userAuditLog);
	
	public Date getUsersLastActivityTime(Long userId, String token);
}
