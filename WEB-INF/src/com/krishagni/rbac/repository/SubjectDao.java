package com.krishagni.rbac.repository;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.rbac.domain.Subject;
import com.krishagni.rbac.events.UserAccessInformation;

public interface SubjectDao extends Dao<Subject> {
	public boolean canUserAccess(UserAccessInformation accessInfo);
}
