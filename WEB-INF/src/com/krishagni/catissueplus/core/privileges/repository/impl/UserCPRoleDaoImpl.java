
package com.krishagni.catissueplus.core.privileges.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.privileges.domain.UserCPRole;
import com.krishagni.catissueplus.core.privileges.repository.UserCPRoleDao;

public class UserCPRoleDaoImpl extends AbstractDao<UserCPRole> implements UserCPRoleDao {

	private static final String FQN = UserCPRole.class.getName();

	private static final String GET_USERCPROLE_BY_CP_AND_USER = FQN + ".getUserCPRoleByCpAndUser";

	private static final String GET_CPS_BY_USER_PRIVELEGE_CONST = FQN + ".getCPsByUserAndPrivConstant";

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public List<UserCPRole> getCPUserRoleByCpAndUser(Long cpId, Long userId) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_USERCPROLE_BY_CP_AND_USER);
		query.setLong("cpId", cpId);
		query.setLong("userId", userId);
		return query.list();
	}

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public List<Long> getCpIdsByUserAndPrivilege(Long userId, String privilegeConst) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_CPS_BY_USER_PRIVELEGE_CONST);
		query.setLong("userId", userId);
		query.setString("privilegeConst", privilegeConst);
		return query.list();
	}
}
