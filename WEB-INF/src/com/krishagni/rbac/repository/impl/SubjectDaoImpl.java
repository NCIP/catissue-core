package com.krishagni.rbac.repository.impl;

import java.util.Map;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.rbac.domain.Subject;
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
	public boolean canUserAccess(Map<String,Object> args) {
		return (!(sessionFactory.getCurrentSession()
				.getNamedQuery(CAN_USER_ACCESS)
				.setLong("subjectId" , (Long) args.get("subjectId"))
				.setString("resourceName", (String) args.get("resourceName"))
				.setString("operationName", (String) args.get("operationName"))
				.setLong("dsoId",  (Long) args.get("dsoId"))
				.setLong("resourceInstanceId", (Long) args.get("resourceInstanceId"))
				.list().isEmpty()));
	}
}
