package com.krishagni.rbac.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.rbac.domain.Subject;
import com.krishagni.rbac.domain.SubjectAccess;

public interface SubjectDao extends Dao<Subject> {		
	public boolean canUserPerformOps(Long subjectId, String resource, String[] ops);
	
	public List<SubjectAccess> getAccessList(Long subjectId, String resource, String[] ops);
	
	public List<SubjectAccess> getAccessList(Long subjectId, String resource, String[] ops, String[] siteNames);
	
	public List<SubjectAccess> getAccessList(Long subjectId, Long cpId, String resource, String[] ops);
	
	public Integer removeRolesByCp(Long cpId);
}