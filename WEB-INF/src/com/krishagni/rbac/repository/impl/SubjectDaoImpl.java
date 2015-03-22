package com.krishagni.rbac.repository.impl;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.rbac.domain.Subject;
import com.krishagni.rbac.events.UserAccessInformation;
import com.krishagni.rbac.repository.SubjectDao;

public class SubjectDaoImpl extends AbstractDao<Subject> implements SubjectDao {
	private static final String FQN = Subject.class.getName();
	
	private static final String CAN_USER_ACCESS = FQN + ".getRolesforUserAccess";

	@Override
	public Subject getSubject(Long subjectId) {
		return (Subject) sessionFactory.getCurrentSession()
				.get(Subject.class, subjectId);
	}

	@Override
	public boolean canUserAccess(UserAccessInformation info) {
		return (!(sessionFactory.getCurrentSession()
				.getNamedQuery(CAN_USER_ACCESS)
				.setLong("subjectId" , info.subjectId())
				.setString("resourceName", info.resourceName())
				.setString("operationName", info.operationName())
				.setLong("cpId",  info.cpId())
				.setLong("siteId", info.siteId())
				.setLong("resourceInstanceId", info.resourceInstanceId())
				.list().isEmpty()));
	}
}
