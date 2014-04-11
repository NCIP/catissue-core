package com.krishagni.catissueplus.core.de.events;

import java.util.Date;

import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;

public class SavedQuerySummary {

	private Long id;

	private String title;

	private UserSummary createdBy;

	private UserSummary lastModifiedBy;
	
	private Date lastModifiedOn;
	
	private Date lastRunOn;
	
	private Long lastRunCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public UserSummary getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserSummary createdBy) {
		this.createdBy = createdBy;
	}

	public UserSummary getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(UserSummary lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedOn() {
		return lastModifiedOn;
	}

	public void setLastModifiedOn(Date lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}
	
	public Date getLastRunOn() {
		return lastRunOn;
	}

	public void setLastRunOn(Date lastRunOn) {
		this.lastRunOn = lastRunOn;
	}

	public Long getLastRunCount() {
		return lastRunCount;
	}

	public void setLastRunCount(Long lastRunCount) {
		this.lastRunCount = lastRunCount;
	}
		
	public static SavedQuerySummary fromSavedQuery(SavedQuery savedQuery){
		SavedQuerySummary querySummary = new SavedQuerySummary();
		querySummary.setId(savedQuery.getId());
		querySummary.setTitle(savedQuery.getTitle());
		querySummary.setCreatedBy(UserSummary.fromUser(savedQuery.getCreatedBy()));
		querySummary.setLastModifiedBy(UserSummary.fromUser(savedQuery.getLastUpdatedBy()));
		querySummary.setLastModifiedOn(savedQuery.getLastUpdated());
		querySummary.setLastRunCount(savedQuery.getLastRunCount());		
		return querySummary;
	}
}
