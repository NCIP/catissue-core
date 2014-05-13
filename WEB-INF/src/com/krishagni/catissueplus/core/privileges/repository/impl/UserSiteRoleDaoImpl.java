
package com.krishagni.catissueplus.core.privileges.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.privileges.domain.UserSiteRole;
import com.krishagni.catissueplus.core.privileges.repository.UserSiteRoleDao;

public class UserSiteRoleDaoImpl extends AbstractDao<UserSiteRole> implements UserSiteRoleDao {

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public List<Long> getCpIdsByUserAndPrivilege(Long userId, String privilegeConst) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_CPS_BY_USER_PRIVELEGE_CONST);
		query.setLong("userId", userId);
		return query.list();
	}

	private static final String FQN = UserSiteRole.class.getName();

	private static final String GET_CPS_BY_USER_PRIVELEGE_CONST = FQN + ".getCPsByUserAndPrivConstant";

}
