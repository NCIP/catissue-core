package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class RoleStatusUpdatedEvent extends ResponseEvent {
	private RoleDetails roleSummary;
	
	private Long roleId;
	
	public RoleDetails getRoleSummary() {
		return roleSummary;
	}

	public void setRoleSummary(RoleDetails roleSummary) {
		this.roleSummary = roleSummary;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public static RoleStatusUpdatedEvent ok(RoleDetails roleSummary) {
		RoleStatusUpdatedEvent resp = new RoleStatusUpdatedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setRoleSummary(roleSummary);
		return resp;
	}
	
	public static RoleStatusUpdatedEvent notFound(RoleDetails roleSummary) {
		RoleStatusUpdatedEvent resp = new RoleStatusUpdatedEvent();
		resp.setRoleSummary(roleSummary);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
	
	public static RoleStatusUpdatedEvent notFound(Long roleId) {
		RoleStatusUpdatedEvent resp = new RoleStatusUpdatedEvent();
		RoleDetails roleSummary = new RoleDetails();
		roleSummary.setId(roleId);
		resp.setRoleSummary(roleSummary);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
	
	public static RoleStatusUpdatedEvent badRequest(String message, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, message, t);
	}
	
	public static RoleStatusUpdatedEvent badRequest(ObjectCreationException e) {
		return errorResp(EventStatus.BAD_REQUEST, e.getMessage(), e);
	}	
	
	public static RoleStatusUpdatedEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}
	
	private static RoleStatusUpdatedEvent errorResp(EventStatus status, String message, Throwable t) {
		RoleStatusUpdatedEvent resp = new RoleStatusUpdatedEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		
		if (t instanceof ObjectCreationException) {
			ObjectCreationException oce = (ObjectCreationException)t;
			resp.setErroneousFields(oce.getErroneousFields());
			resp.setException(null);
		}
		return resp;		
	}

}
