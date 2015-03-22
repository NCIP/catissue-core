package com.krishagni.rbac.repository;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.rbac.domain.Group;
import com.krishagni.rbac.events.UserAccessInformation;

public interface GroupDao extends Dao<Group> {
	public Group getGroup(Long groupId);

	public boolean canUserAccess(UserAccessInformation info);
}
 