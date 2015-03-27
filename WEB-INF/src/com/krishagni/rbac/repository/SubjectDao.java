package com.krishagni.rbac.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.rbac.domain.Subject;
import com.krishagni.rbac.events.CpSiteInfo;
import com.krishagni.rbac.events.UserAccessCriteria;

public interface SubjectDao extends Dao<Subject> {
	public Subject getSubject(Long subjectId);
	
	public List<CpSiteInfo> getCpSiteForOpExecution(UserAccessCriteria uac);
	
	public boolean canUserPerformOp(UserAccessCriteria uac);
}
