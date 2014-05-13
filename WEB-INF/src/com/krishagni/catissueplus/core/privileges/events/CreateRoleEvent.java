
package com.krishagni.catissueplus.core.privileges.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateRoleEvent extends RequestEvent {

	private RoleDetails roleDetails;

	public RoleDetails getRoleDetails() {
		return roleDetails;
	}

	public void setRoleDetails(RoleDetails roleDetails) {
		this.roleDetails = roleDetails;
	}

}
