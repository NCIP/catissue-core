package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class SubjectRoleUpdatedEvent extends ResponseEvent {
	private SubjectDetails subject;
	
	public SubjectDetails getSubject() {
		return subject;
	}

	public void setSubject(SubjectDetails subject) {
		this.subject = subject;
	}

	public static SubjectRoleUpdatedEvent ok(SubjectDetails subject) {
		SubjectRoleUpdatedEvent resp = new SubjectRoleUpdatedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setSubject(subject);
		return resp;
	}
	
	public static SubjectRoleUpdatedEvent badRequest(RbacErrorCode error, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, error, t);
	}
	
	public static SubjectRoleUpdatedEvent serverError(Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, null, t);
	}
	
	private static SubjectRoleUpdatedEvent errorResp(EventStatus status, RbacErrorCode error, Throwable t) {
		SubjectRoleUpdatedEvent resp = new SubjectRoleUpdatedEvent();
		resp.setupResponseEvent(status, error, t);
		return resp;		
	}
}
