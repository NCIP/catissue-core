package com.krishagni.rbac.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class AllSubjectRolesEvent extends ResponseEvent {
	private SubjectDetails subject;
	
	public SubjectDetails getSubject() {
		return subject;
	}

	public void setSubject(SubjectDetails subject) {
		this.subject = subject;
	}

	public static AllSubjectRolesEvent ok(SubjectDetails subject) {
		AllSubjectRolesEvent resp = new AllSubjectRolesEvent();
		resp.setStatus(EventStatus.OK);
		resp.setSubject(subject);
		return resp;
	}
	
	public static AllSubjectRolesEvent badRequest(RbacErrorCode error) {
		return errorResp(EventStatus.BAD_REQUEST, error, null);
	}
	
	public static AllSubjectRolesEvent serverError(Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, null, t);
	}
	
	private static AllSubjectRolesEvent errorResp(EventStatus status, RbacErrorCode errorCode, Throwable t) {
		AllSubjectRolesEvent resp = new AllSubjectRolesEvent();
		resp.setupResponseEvent(status, errorCode, t);
		return resp;		
	}
}
