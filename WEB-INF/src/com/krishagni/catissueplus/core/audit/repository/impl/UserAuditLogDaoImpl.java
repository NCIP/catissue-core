package com.krishagni.catissueplus.core.audit.repository.impl;

import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.audit.domain.UserAuditLog;
import com.krishagni.catissueplus.core.audit.repository.UserAuditLogDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class UserAuditLogDaoImpl extends AbstractDao<UserAuditLog> implements UserAuditLogDao  {

	@Override
	@SuppressWarnings("unchecked")
	public Date getUsersLastActivityTime(Long userId, String token) {
		List<Date> results = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_USERS_LAST_ACTIVITY_TIME)
				.setLong("userId", userId)
				.setString("authToken", token)
				.setMaxResults(1)
				.list();

		return results.isEmpty() ? null : results.get(0);
		
	}
	
	private static final String FQN = UserAuditLog.class.getName();
	
	private static final String GET_USERS_LAST_ACTIVITY_TIME = FQN + ".getUsersLastActivityTime";
	
}
