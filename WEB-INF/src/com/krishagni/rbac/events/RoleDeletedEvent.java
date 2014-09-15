package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class RoleDeletedEvent extends ResponseEvent {
	private String roleName;
	
	private RoleDetails role;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public RoleDetails getRole() {
		return role;
	}

	public void setRole(RoleDetails roleDetails) {
		this.role = roleDetails;
	}

	public static RoleDeletedEvent ok(RoleDetails role) {
		RoleDeletedEvent resp = new RoleDeletedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setRole(role);
		return resp;
	}
	
	public static RoleDeletedEvent notFound(String roleName) {
		RoleDeletedEvent resp = new RoleDeletedEvent();
		resp.setRoleName(roleName);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
	
	public static RoleDeletedEvent badRequest(RbacErrorCode error, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, error, t);
	}
	
	public static RoleDeletedEvent serverError(Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, null, t);
	}
	
	private static RoleDeletedEvent errorResp(EventStatus status, RbacErrorCode error, Throwable t) {
		RoleDeletedEvent resp = new RoleDeletedEvent();
		resp.setupResponseEvent(status, error, t);
		return resp;		
	}
}
