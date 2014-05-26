
package com.krishagni.catissueplus.core.privileges.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class RoleUpdatedEvent extends ResponseEvent {

	private RoleDetails roleDetails;

	public RoleDetails getRoleDetails() {
		return roleDetails;
	}

	public void setRoleDetails(RoleDetails roleDetails) {
		this.roleDetails = roleDetails;
	}

	public static RoleUpdatedEvent ok(RoleDetails details) {
		RoleUpdatedEvent event = new RoleUpdatedEvent();
		event.setRoleDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static RoleUpdatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		RoleUpdatedEvent resp = new RoleUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static RoleUpdatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		RoleUpdatedEvent resp = new RoleUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static RoleUpdatedEvent notFound(Long roleId) {
		RoleUpdatedEvent resp = new RoleUpdatedEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}
