
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;

public class VisitSummary implements Comparable<VisitSummary> {
	private Long id;
	
	private Long eventId;

	private String name;
	
	private String label;
	
	private int calendarPoint;
	
	private String status;
	
	private Date visitDate;
	
	private Date anticipatedVisitDate;
	
	private int anticipatedSpecimens;
	
	private int collectedSpecimens;
	
	private int uncollectedSpecimens;
	
	private int unplannedSpecimens;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getCalendarPoint() {
		return calendarPoint;
	}

	public void setCalendarPoint(int calendarPoint) {
		this.calendarPoint = calendarPoint;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public Date getAnticipatedVisitDate() {
		return anticipatedVisitDate;
	}

	public void setAnticipatedVisitDate(Date anticipatedVisitDate) {
		this.anticipatedVisitDate = anticipatedVisitDate;
	}

	public int getAnticipatedSpecimens() {
		return anticipatedSpecimens;
	}

	public void setAnticipatedSpecimens(int anticipatedSpecimens) {
		this.anticipatedSpecimens = anticipatedSpecimens;
	}

	public int getCollectedSpecimens() {
		return collectedSpecimens;
	}

	public void setCollectedSpecimens(int collectedSpecimens) {
		this.collectedSpecimens = collectedSpecimens;
	}

	public int getUncollectedSpecimens() {
		return uncollectedSpecimens;
	}

	public void setUncollectedSpecimens(int uncollectedSpecimens) {
		this.uncollectedSpecimens = uncollectedSpecimens;
	}

	public int getUnplannedSpecimens() {
		return unplannedSpecimens;
	}

	public void setUnplannedSpecimens(int unplannedSpecimens) {
		this.unplannedSpecimens = unplannedSpecimens;
	}

	@Override
	public int compareTo(VisitSummary other) {
		Date thisVisit = visitDate != null ? visitDate : anticipatedVisitDate;
		Date otherVisit = other.visitDate != null ? other.visitDate : other.anticipatedVisitDate;		
		return thisVisit.compareTo(otherVisit);
	}	
}
