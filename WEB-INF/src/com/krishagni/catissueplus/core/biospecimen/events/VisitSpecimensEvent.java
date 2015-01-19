package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class VisitSpecimensEvent extends ResponseEvent {
	private Long cprId;
	
	private Long eventId;
	
	private List<SpecimenDetail> specimens;

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public List<SpecimenDetail> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(List<SpecimenDetail> specimens) {
		this.specimens = specimens;
	}
	
	public static VisitSpecimensEvent ok(Long cprId, Long eventId, Long visitId, List<SpecimenDetail> specimens) {
		VisitSpecimensEvent resp = new VisitSpecimensEvent();
		resp.setCprId(cprId);
		resp.setEventId(eventId);
		resp.setSpecimens(specimens);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static VisitSpecimensEvent notFound(Long cprId, Long eventId, Long visitId) {
		VisitSpecimensEvent resp = new VisitSpecimensEvent();
		resp.setCprId(cprId);
		resp.setEventId(eventId);		
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;		
	}
	
	public static VisitSpecimensEvent serverError(Long cprId, Long eventId, Long visitId, Throwable e) {
		VisitSpecimensEvent resp = new VisitSpecimensEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		return resp;		
	}	
}
