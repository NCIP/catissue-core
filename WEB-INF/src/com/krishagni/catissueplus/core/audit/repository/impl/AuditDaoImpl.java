package com.krishagni.catissueplus.core.audit.repository.impl;

import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.audit.domain.UserApiCallLog;
import com.krishagni.catissueplus.core.audit.repository.AuditDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class AuditDaoImpl extends AbstractDao<UserApiCallLog> implements AuditDao {

	@Override
	@SuppressWarnings("unchecked")
	public Date getLatestApiCallTime(Long userId, String token) {
		List<Date> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_LATEST_API_CALL_TIME)
				.setLong("userId", userId)
				.setString("authToken", token)
				.list();
		
		return result.isEmpty() ? null : result.get(0);
	}
	
	private static final String FQN = UserApiCallLog.class.getName();
	
	private static final String GET_LATEST_API_CALL_TIME = FQN + ".getLatestApiCallTime";
}
