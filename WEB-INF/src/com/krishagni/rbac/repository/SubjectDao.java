package com.krishagni.rbac.repository;

import java.util.Map;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.rbac.domain.Subject;

public interface SubjectDao extends Dao<Subject> {
	public Subject getSubject(Long subjectId);
	
	public boolean canUserAccess(Map<String,Object> args);
}
