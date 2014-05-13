
package com.krishagni.catissueplus.core.privileges.services;

import java.util.List;

import com.krishagni.catissueplus.core.privileges.events.CreateRoleEvent;
import com.krishagni.catissueplus.core.privileges.events.RoleCreatedEvent;
import com.krishagni.catissueplus.core.privileges.events.RoleDetails;
import com.krishagni.catissueplus.core.privileges.events.RoleUpdatedEvent;
import com.krishagni.catissueplus.core.privileges.events.UpdateRoleEvent;

public interface RoleService {

	public RoleCreatedEvent createRole(CreateRoleEvent event);

	public RoleUpdatedEvent updateRole(UpdateRoleEvent event);

	public List<RoleDetails> getAllRoles();
}
