package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class AllRolesEvent extends ResponseEvent {
	private List<RoleDetails> roles = new ArrayList<RoleDetails>();
	
	public List<RoleDetails> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleDetails> roles) {
		this.roles = roles;
	}

	public static AllRolesEvent ok(List<RoleDetails> allRoles) {
		AllRolesEvent resp = new AllRolesEvent();
		resp.setStatus(EventStatus.OK);
		resp.setRoles(allRoles);
		return resp;
	}
	
	public static AllRolesEvent serverError(Throwable t) {		
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, null, t);
	}
	
	private static AllRolesEvent errorResp(EventStatus status, RbacErrorCode error, Throwable t) {
		AllRolesEvent resp = new AllRolesEvent();
		resp.setupResponseEvent(status, error, t);
		return resp;		
	}
}
