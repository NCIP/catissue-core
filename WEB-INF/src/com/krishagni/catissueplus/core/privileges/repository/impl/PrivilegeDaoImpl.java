
package com.krishagni.catissueplus.core.privileges.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.privileges.domain.Privilege;
import com.krishagni.catissueplus.core.privileges.repository.PrivilegeDao;

public class PrivilegeDaoImpl extends AbstractDao<Privilege> implements PrivilegeDao {

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public Privilege getPrivilegeByName(String privilegeName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_PRIVILEGE_BY_NAME);
		query.setString("privilegeName", privilegeName);
		List<Privilege> results = query.list();
		return results.isEmpty() ? null : results.get(0);
	}

	private static final String FQN = Privilege.class.getName();

	private static final String GET_PRIVILEGE_BY_NAME = FQN + ".getPrivilegeByName";

}
