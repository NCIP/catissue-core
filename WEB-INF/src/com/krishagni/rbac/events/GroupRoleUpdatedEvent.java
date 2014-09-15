package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class GroupRoleUpdatedEvent extends ResponseEvent {
	private GroupDetails groupDetails;
	
	public GroupDetails getGroupDetails() {
		return groupDetails;
	}

	public void setGroupDetails(GroupDetails groupDetails) {
		this.groupDetails = groupDetails;
	}

	public static GroupRoleUpdatedEvent ok(GroupDetails groupDetails) {
		GroupRoleUpdatedEvent resp = new GroupRoleUpdatedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setGroupDetails(groupDetails);
		return resp;
	}
	
	public static GroupRoleUpdatedEvent badRequest(RbacErrorCode error, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, error, t);
	}
	
	public static GroupRoleUpdatedEvent serverError(Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, null, t);
	}
	
	private static GroupRoleUpdatedEvent errorResp(EventStatus status, RbacErrorCode error, Throwable t) {
		GroupRoleUpdatedEvent resp = new GroupRoleUpdatedEvent();
		resp.setupResponseEvent(status, error, t);
		return resp;		
	}
}
