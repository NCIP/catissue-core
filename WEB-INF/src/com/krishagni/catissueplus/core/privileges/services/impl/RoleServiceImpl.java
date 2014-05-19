
package com.krishagni.catissueplus.core.privileges.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.UserCreatedEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.privileges.domain.Role;
import com.krishagni.catissueplus.core.privileges.domain.factory.PrivilegeErrorCode;
import com.krishagni.catissueplus.core.privileges.domain.factory.RoleFactory;
import com.krishagni.catissueplus.core.privileges.events.CreateRoleEvent;
import com.krishagni.catissueplus.core.privileges.events.RoleCreatedEvent;
import com.krishagni.catissueplus.core.privileges.events.RoleDetails;
import com.krishagni.catissueplus.core.privileges.events.RoleUpdatedEvent;
import com.krishagni.catissueplus.core.privileges.events.UpdateRoleEvent;
import com.krishagni.catissueplus.core.privileges.services.RoleService;

public class RoleServiceImpl implements RoleService {

	private static final String ROLE_NAME = "role name";

	@Autowired
	private RoleFactory roleFactory;

	@Autowired
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setRoleFactory(RoleFactory roleFactory) {
		this.roleFactory = roleFactory;
	}

	@Override
	@PlusTransactional
	public RoleCreatedEvent createRole(CreateRoleEvent event) {
		try {
			Role role = roleFactory.createRole(event.getRoleDetails());
			ObjectCreationException exceptionHandler = new ObjectCreationException();
			ensureUniqueRoleName(role.getName(), exceptionHandler);
			exceptionHandler.checkErrorAndThrow();
			daoFactory.getRoleDao().saveOrUpdate(role);
			return RoleCreatedEvent.ok(RoleDetails.fromDomain(role));
		}
		catch (ObjectCreationException ce) {
			return RoleCreatedEvent.invalidRequest(PrivilegeErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return RoleCreatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public RoleUpdatedEvent updateRole(UpdateRoleEvent event) {
		try {
			Long roleId = event.getRoleDetails().getId();
			Role oldRole = daoFactory.getRoleDao().getRole(roleId);
			if (oldRole == null) {
				return RoleUpdatedEvent.notFound(roleId);
			}
			Role role = roleFactory.createRole(event.getRoleDetails());
			oldRole.update(role);
			daoFactory.getRoleDao().saveOrUpdate(oldRole);
			return RoleUpdatedEvent.ok(RoleDetails.fromDomain(oldRole));
		}
		catch (CatissueException ce) {
			return RoleUpdatedEvent.invalidRequest(ce.getMessage());
		}
		catch (Exception e) {
			return RoleUpdatedEvent.serverError(e);
		}
	}

	private void ensureUniqueRoleName(String roleName, ObjectCreationException exceptionHandler) {
		if (daoFactory.getRoleDao().getRoleByName(roleName) != null) {
			exceptionHandler.addError(PrivilegeErrorCode.DUPLICATE_ROLE_NAME, ROLE_NAME);
		}
	}

	@Override
	@PlusTransactional
	public List<RoleDetails> getAllRoles() {
		List<RoleDetails> rolesDetails = new ArrayList<RoleDetails>();
		List<Role> roles =  daoFactory.getRoleDao().getAllRoles();
		for (Role role : roles) {
			rolesDetails.add(RoleDetails.fromDomain(role));
		}
		return rolesDetails;
	}

}
