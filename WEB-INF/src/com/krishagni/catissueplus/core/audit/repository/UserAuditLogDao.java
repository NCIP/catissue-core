package com.krishagni.catissueplus.core.audit.repository;

import java.util.Date;

import com.krishagni.catissueplus.core.audit.domain.UserAuditLog;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface UserAuditLogDao extends Dao<UserAuditLog> {
	
	public Date getUsersLastActivityTime(Long userId, String token);

}

