
package com.krishagni.catissueplus.core.privileges.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.privileges.domain.UserCPRole;

public interface UserCPRoleDao extends Dao<UserCPRole> {

	List<UserCPRole> getCPUserRoleByCpAndUser(Long cpId, Long userId);

	public List<Long> getCpIdsByUserAndPrivilege(Long userId, String privilegeConst);

}
