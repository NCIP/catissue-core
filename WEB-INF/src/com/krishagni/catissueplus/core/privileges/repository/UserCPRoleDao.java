
package com.krishagni.catissueplus.core.privileges.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;
import com.krishagni.catissueplus.core.privileges.domain.UserCPRole;
import com.krishagni.catissueplus.core.privileges.events.UserPrivDetail;

public interface UserCPRoleDao extends Dao<UserCPRole> {

	List<UserCPRole> getCPUserRoleByCpAndUser(Long cpId, Long userId);

	List<Long> getCpIdsByUserId(Long userId);
	List<Long> getSiteIdsByUserId(Long userId);
	List<Long> getAllCpIds();
	List<Long> getCpIdBySiteId(Long siteId);
	List<Long> getSiteIdsByCpId(Long cpId);
	UserPrivDetail getUserPrivDetail(Long userId);

}
