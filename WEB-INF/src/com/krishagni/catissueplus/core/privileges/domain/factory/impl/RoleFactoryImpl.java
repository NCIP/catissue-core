
package com.krishagni.catissueplus.core.privileges.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.privileges.PrivilegeType;
import com.krishagni.catissueplus.core.privileges.domain.Privilege;
import com.krishagni.catissueplus.core.privileges.domain.Role;
import com.krishagni.catissueplus.core.privileges.domain.factory.PrivilegeErrorCode;
import com.krishagni.catissueplus.core.privileges.domain.factory.RoleFactory;
import com.krishagni.catissueplus.core.privileges.events.RoleDetails;

public class RoleFactoryImpl implements RoleFactory {

	private static final String PRIVILEGE = "privilege";

	private static final String ROLE_NAME = "role name";

	@Autowired
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Role createRole(RoleDetails details) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();
		Role role = new Role();
		setRoleName(details.getName(), role, exceptionHandler);
		setDescription(details.getDescription(), role);
		setPrivileges(details.getPrivilegeNames(), role, exceptionHandler);
		exceptionHandler.checkErrorAndThrow();
		return role;
	}

	private void setDescription(String description, Role role) {
		role.setDescription(description);
	}

	private void setPrivileges(List<String> privilegeNames, Role role, ObjectCreationException exceptionHandler) {
		Set<Privilege> privileges = new HashSet<Privilege>();

		for (String privilegeName : privilegeNames) {
			if (!PrivilegeType.isValidPrivilegeType(privilegeName)) {
				exceptionHandler.addError(PrivilegeErrorCode.NOT_FOUND, PRIVILEGE);
			}
			Privilege privilege = daoFactory.getPrivilegeDao().getPrivilegeByName(privilegeName);
			privileges.add(privilege);
		}
		role.setPrivileges(privileges);
	}

	private void setRoleName(String roleName, Role role, ObjectCreationException exceptionHandler) {
		if (isBlank(roleName)) {
			exceptionHandler.addError(PrivilegeErrorCode.INVALID_ATTR_VALUE, ROLE_NAME);
			return;
		}
		role.setName(roleName);
	}

}
