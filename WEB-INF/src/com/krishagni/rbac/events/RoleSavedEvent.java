package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class RoleSavedEvent extends ResponseEvent {
	private RoleDetails role;

	public RoleDetails getRole() {
		return role;
	}

	public void setRole(RoleDetails role) {
		this.role = role;
	}

	public static RoleSavedEvent ok(RoleDetails role) {
		RoleSavedEvent resp = new RoleSavedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setRole(role);
		return resp;
	}
	
	public static RoleSavedEvent notFound(RoleDetails role) {
		RoleSavedEvent resp = new RoleSavedEvent();
		resp.setRole(role);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
	
	public static RoleSavedEvent badRequest(RbacErrorCode error, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, error, t);
	}
	
	public static RoleSavedEvent serverError(Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, null, t);
	}
	
	private static RoleSavedEvent errorResp(EventStatus status, RbacErrorCode error, Throwable t) {
		RoleSavedEvent resp = new RoleSavedEvent();
		resp.setupResponseEvent(status, error, t);
		return resp;		
	}
}
