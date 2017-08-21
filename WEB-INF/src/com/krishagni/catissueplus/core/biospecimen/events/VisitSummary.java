
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Date;

import org.apache.commons.lang3.ObjectUtils;

import com.krishagni.catissueplus.core.common.events.UserSummary;

public class VisitSummary implements Comparable<VisitSummary> {
	private Long id;

	private Long cpId;
	
	private Long eventId;

	private String name;
	
	private String eventLabel;
	
	private Integer eventPoint;
	
	private String status;
	
	private Date visitDate;
	
	private Date anticipatedVisitDate;
	
	private int anticipatedSpecimens;
	
	private int collectedSpecimens;
	
	private int uncollectedSpecimens;
	
	private int unplannedSpecimens;

	private String missedReason;

	private UserSummary missedBy;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
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

	public String getEventLabel() {
		return eventLabel;
	}

	public void setEventLabel(String eventLabel) {
		this.eventLabel = eventLabel;
	}

	public Integer getEventPoint() {
		return eventPoint;
	}

	public void setEventPoint(Integer eventPoint) {
		this.eventPoint = eventPoint;
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

	public String getMissedReason() {
		return missedReason;
	}

	public void setMissedReason(String missedReason) {
		this.missedReason = missedReason;
	}

	public UserSummary getMissedBy() {
		return missedBy;
	}

	public void setMissedBy(UserSummary missedBy) {
		this.missedBy = missedBy;
	}

	@Override
	public int compareTo(VisitSummary other) {
		int result = ObjectUtils.compare(this.eventPoint, other.eventPoint, true);
		if (result != 0) {
			return result;
		}

		result = ObjectUtils.compare(this.eventId, other.eventId, true);
		if (result != 0) {
			return result;
		}

		Date thisVisitDate = visitDate != null ? visitDate : anticipatedVisitDate;
		Date otherVisitDate = other.visitDate != null ? other.visitDate : other.anticipatedVisitDate;
		return ObjectUtils.compare(thisVisitDate, otherVisitDate, true);
	}
}
