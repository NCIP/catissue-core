package com.krishagni.rbac.repository;

import java.util.Map;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.rbac.domain.Group;

public interface GroupDao extends Dao<Group> {
	public Group getGroup(Long groupId);

	public boolean canUserAccess(Map<String,Object> args);
}
 