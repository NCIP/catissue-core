package com.krishagni.catissueplus.core.de.domain;

import java.util.Calendar;
import java.util.Date;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.common.util.AuthUtil;

public class CpCatalogSetting extends BaseEntity {
	private CollectionProtocol cp;
	
	private SavedQuery query;
	
	private User createdBy;
	
	private Date creationTime;
	
	private User lastUpdatedBy;
	
	private Date lastUpdateTime;
	
	private String activityStatus;

	public CollectionProtocol getCp() {
		return cp;
	}

	public void setCp(CollectionProtocol cp) {
		this.cp = cp;
	}

	public SavedQuery getQuery() {
		return query;
	}

	public void setQuery(SavedQuery query) {
		this.query = query;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public User getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(User lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	
	public void update(CpCatalogSetting other) {
		setQuery(other.getQuery());
		setLastUpdatedBy(AuthUtil.getCurrentUser());
		setLastUpdateTime(Calendar.getInstance().getTime());
		setActivityStatus(other.getActivityStatus());
	}	
}