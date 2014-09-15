package com.krishagni.rbac.repository.impl;

import java.util.Map;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.rbac.domain.Group;
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
	public boolean canUserAccess(Map<String,Object> args) {
		return (!(sessionFactory.getCurrentSession()
				.getNamedQuery(CAN_USER_ACCESS)
				.setLong("groupId" , (Long) args.get("groupId"))
				.setString("resourceName", (String) args.get("resource"))
				.setString("operationName", (String) args.get("operation"))
				.setLong("dsoId", (Long) args.get("dsoId"))
				.setLong("resourceInstanceId", (Long) args.get("resourceInstanceId"))
				.list().isEmpty()));
	}

}
