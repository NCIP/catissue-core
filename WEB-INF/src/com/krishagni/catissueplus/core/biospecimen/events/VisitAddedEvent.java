package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class VisitAddedEvent extends ResponseEvent {
	private Long cprId;
	
	private VisitDetail visit;

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}
	
	public VisitDetail getVisit() {
		return visit;
	}
	
	public void setVisit(VisitDetail visit) {
		this.visit = visit;
	}

	public static VisitAddedEvent ok(VisitDetail visit) {
		VisitAddedEvent resp = new VisitAddedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setVisit(visit);
		resp.setCprId(visit.getCprId());
		return resp;
	}
	
	public static VisitAddedEvent notFound(Long cprId) {
		VisitAddedEvent resp = new VisitAddedEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setCprId(cprId);
		return resp;
	}
	
	public static VisitAddedEvent notAuthorized() {
		VisitAddedEvent resp = new VisitAddedEvent();
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		return resp;
	}

	public static VisitAddedEvent invalidRequest(String message, ErroneousField... fields) {
		VisitAddedEvent resp = new VisitAddedEvent();
		resp.setErroneousFields(fields);
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}

	public static VisitAddedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		VisitAddedEvent resp = new VisitAddedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}
}
