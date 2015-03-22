package com.krishagni.rbac.repository.impl;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.rbac.domain.Group;
import com.krishagni.rbac.events.UserAccessInformation;
import com.krishagni.rbac.repository.GroupDao;

public class GroupDaoImpl extends AbstractDao<Group> implements GroupDao {
	private static final String FQN = Group.class.getName();
	
	private static final String CAN_USER_ACCESS = FQN + ".getRolesforGroupAccess";
	@Override
	public Group getGroup(Long groupId) {
		return (Group) sessionFactory.getCurrentSession()
				.get(Group.class, groupId);
	}

	@Override
	public boolean canUserAccess(UserAccessInformation info) {
		return (!(sessionFactory.getCurrentSession()
				.getNamedQuery(CAN_USER_ACCESS)
				.setLong("groupId" , info.groupId())
				.setString("resourceName", info.resourceName())
				.setString("operationName", info.operationName())
				.setLong("cpId",  info.cpId())
				.setLong("siteId", info.siteId())
				.setLong("resourceInstanceId", info.resourceInstanceId())
				.list().isEmpty()));
	}

}
