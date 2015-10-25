package com.krishagni.catissueplus.core.audit.services.impl;

import java.util.Date;

import com.krishagni.catissueplus.core.audit.domain.UserAuditLog;
import com.krishagni.catissueplus.core.audit.services.UserAuditLogService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;

public class UserAuditLogServiceImpl implements UserAuditLogService {
	
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public void insertUserAuditLog(UserAuditLog userAuditLog) {
		daoFactory.getUserAuditLogDao().saveOrUpdate(userAuditLog);
	}

	@Override
	@PlusTransactional
	public Date getUsersLastActivityTime(Long userId, String token) {
		 return daoFactory.getUserAuditLogDao().getUsersLastActivityTime(userId, token);
	}
}
