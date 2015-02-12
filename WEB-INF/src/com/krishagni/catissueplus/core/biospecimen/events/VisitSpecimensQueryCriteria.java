package com.krishagni.catissueplus.core.biospecimen.events;


public class VisitSpecimensQueryCriteria {
	private Long cprId;
	
	private Long eventId;
	
	private Long visitId;

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

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}
}
