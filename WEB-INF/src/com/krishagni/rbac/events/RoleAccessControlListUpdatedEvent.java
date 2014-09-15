package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class RoleAccessControlListUpdatedEvent extends ResponseEvent {
	private String roleName;
	
	private RoleDetails roleDetails;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public RoleDetails getRoleDetails() {
		return roleDetails;
	}

	public void setRoleDetails(RoleDetails roleDetails) {
		this.roleDetails = roleDetails;
	}

	public static RoleAccessControlListUpdatedEvent ok(RoleDetails roleDetails) {
		RoleAccessControlListUpdatedEvent resp = new RoleAccessControlListUpdatedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setRoleDetails(roleDetails);
		return resp;
	}
	
	public static RoleAccessControlListUpdatedEvent notFound(String roleName) {
		RoleAccessControlListUpdatedEvent resp = new RoleAccessControlListUpdatedEvent();
		resp.setRoleName(roleName);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
	
	public static RoleAccessControlListUpdatedEvent badRequest(String message, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, message, t);
	}
	
	public static RoleAccessControlListUpdatedEvent badRequest(ObjectCreationException e) {
		return errorResp(EventStatus.BAD_REQUEST, e.getMessage(), e);
	}	
	
	public static RoleAccessControlListUpdatedEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static RoleAccessControlListUpdatedEvent errorResp(EventStatus status, String message, Throwable t) {
		RoleAccessControlListUpdatedEvent resp = new RoleAccessControlListUpdatedEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;		
	}
}
