package com.krishagni.catissueplus.core.de.events;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.domain.CpCatalogSetting;

public class CpCatalogSettingDetail {
	private Long id;

	private CollectionProtocolSummary cp;
	
	private SavedQuerySummary query;
	
	private UserSummary createdBy;
	
	private Date creationTime;
	
	private UserSummary lastUpdatedBy;
	
	private Date lastUpdateTime;
	
	private String activityStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CollectionProtocolSummary getCp() {
		return cp;
	}

	public void setCp(CollectionProtocolSummary cp) {
		this.cp = cp;
	}

	public SavedQuerySummary getQuery() {
		return query;
	}

	public void setQuery(SavedQuerySummary query) {
		this.query = query;
	}

	public UserSummary getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserSummary createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public UserSummary getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(UserSummary lastUpdatedBy) {
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

	public static CpCatalogSettingDetail from(CpCatalogSetting setting) {
		CpCatalogSettingDetail result = new CpCatalogSettingDetail();
		result.setId(setting.getId());
		result.setCp(CollectionProtocolSummary.from(setting.getCp()));
		result.setQuery(SavedQuerySummary.fromSavedQuery(setting.getQuery()));
		result.setCreatedBy(UserSummary.from(setting.getCreatedBy()));
		result.setCreationTime(setting.getCreationTime());
		
		if (setting.getLastUpdatedBy() != null) {
			result.setLastUpdatedBy(UserSummary.from(setting.getLastUpdatedBy()));
			result.setLastUpdateTime(setting.getLastUpdateTime());			
		}
			
		result.setActivityStatus(setting.getActivityStatus());
		return result;		
	}	
}