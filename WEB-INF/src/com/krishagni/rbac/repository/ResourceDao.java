package com.krishagni.rbac.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.rbac.domain.Resource;

public interface ResourceDao extends Dao<Resource> {
	public Resource getResourceByName(String resourceName);
	
	public Resource getResource(Long resourceId);
	
	public List<Resource> getResources(ResourceListCriteria criteria);
}
