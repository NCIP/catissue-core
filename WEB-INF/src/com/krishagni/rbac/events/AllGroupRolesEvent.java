package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class AllGroupRolesEvent extends ResponseEvent {
	private GroupDetails group;
	
	public GroupDetails getGroup() {
		return group;
	}

	public void setGroup(GroupDetails group) {
		this.group = group;
	}

	public static AllGroupRolesEvent ok(GroupDetails groupDetails) {
		AllGroupRolesEvent resp = new AllGroupRolesEvent();
		resp.setStatus(EventStatus.OK);
		resp.setGroup(groupDetails);
		return resp;
	}
	
	public static AllGroupRolesEvent badRequest(RbacErrorCode error) {
		return errorResp(EventStatus.BAD_REQUEST, error, null);
	}
	
	public static AllGroupRolesEvent serverError(Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, null, t);
	}
	
	private static AllGroupRolesEvent errorResp(EventStatus status, RbacErrorCode errorCode, Throwable t) {
		AllGroupRolesEvent resp = new AllGroupRolesEvent();
		resp.setupResponseEvent(status, errorCode, t);
		return resp;		
	}
}
