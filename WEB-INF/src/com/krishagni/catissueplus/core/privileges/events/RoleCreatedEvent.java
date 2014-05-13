
package com.krishagni.catissueplus.core.privileges.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class RoleCreatedEvent extends ResponseEvent {

	private RoleDetails roleDetails;

	public RoleDetails getRoleDetails() {
		return roleDetails;
	}

	public void setRoleDetails(RoleDetails roleDetails) {
		this.roleDetails = roleDetails;
	}

	public static RoleCreatedEvent ok(RoleDetails details) {
		RoleCreatedEvent event = new RoleCreatedEvent();
		event.setRoleDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static RoleCreatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		RoleCreatedEvent resp = new RoleCreatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static RoleCreatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		RoleCreatedEvent resp = new RoleCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

}
