package com.krishagni.catissueplus.core.privileges.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.privileges.domain.UserSiteRole;


public interface UserSiteRoleDao extends Dao<UserSiteRole> {
	
	public List<Long> getCpIdsByUserAndPrivilege(Long userId, String privilegeConst);

}
